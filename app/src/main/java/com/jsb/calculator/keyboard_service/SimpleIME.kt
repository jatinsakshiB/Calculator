package com.jsb.calculator.keyboard_service

import android.animation.Animator
import android.content.Context
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.jsb.calculator.R
import com.jsb.calculator.databinding.KeyboardBinding
import com.jsb.calculator.manager.LocalStorage
import com.jsb.calculator.view_manager.CalculatorKeyboardViewManager
import com.jsb.calculator.view_manager.NumberKeyboardViewManager
import com.jsb.calculator.view_manager.TextKeyboardViewManager

class SimpleIME : InputMethodService() {

    lateinit var binding: KeyboardBinding

    var countDownTimer: CountDownTimer? = null


    private lateinit var editorInfo: EditorInfo
    private var calculatorKeyboardViewManager: CalculatorKeyboardViewManager? = null
    private var textKeyboardViewManager: TextKeyboardViewManager? = null
    private var numberKeyboardViewManager: NumberKeyboardViewManager? = null

    
    
    override fun onStartInput(attribute: EditorInfo, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        editorInfo = attribute
    }

    override fun onStartInputView(info: EditorInfo, restarting: Boolean) {
        super.onStartInputView(info, restarting)

        calculatorKeyboardViewManager?.refresh()
        textKeyboardViewManager?.refresh(currentInputConnection)
        numberKeyboardViewManager?.refresh()
    }



    override fun onCreateInputView(): View {
        binding = KeyboardBinding.inflate(layoutInflater)


        calculatorKeyboardViewManager = CalculatorKeyboardViewManager(this, binding.calculatorKeyboard)

        textKeyboardViewManager = TextKeyboardViewManager(this, currentInputConnection, binding.normalKeyboard)

        numberKeyboardViewManager = NumberKeyboardViewManager(this, binding.numPadKeyboard)

        when (LocalStorage(this).getDefKeyboard()) {
            "calculator" -> {
                showCalculatorKeyboard()
            }
            "number" -> {
                showNumberKeyboard()
            }
            else -> {
                showNormalKeyboard()
            }
        }

        return binding.root
    }


    fun showToast(s: String?) {
        binding.toast.visibility = View.VISIBLE
        binding.toast.alpha = 1f
        binding.toast.text = s
        binding.toast.animate().alpha(1.0f).setDuration(2500)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    binding.toast.animate().alpha(0.0f).setDuration(500)
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animator: Animator) {}
                            override fun onAnimationEnd(animator: Animator) {
                                binding.toast.visibility = View.GONE
                            }

                            override fun onAnimationCancel(animator: Animator) {}
                            override fun onAnimationRepeat(animator: Animator) {}
                        })
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

        Firebase.analytics.logEvent("keyboard_toast", Bundle().apply {
            putString("message", s)
        })
    }

    fun addText(s: String?){
        currentInputConnection.commitText(s, 1)
        playSound()
    }

    fun addBackspace(s: View){
        fun backspace(){
            val selectedText: CharSequence? = currentInputConnection.getSelectedText(0)
            if (selectedText.isNullOrEmpty()) {
                // no selection, so delete previous character
                currentInputConnection.deleteSurroundingText(1, 0)
            } else {
                // delete the selection
                currentInputConnection.commitText("", 1)
            }
        }
        s.setOnClickListener {
            backspace()
            playSound()
        }
        s.setOnLongClickListener {
            countDownTimer = object : CountDownTimer((50000 * 50).toLong(), 50) {
                override fun onTick(millisUntilFinished: Long) {
                    if (!s.isPressed) {
                        countDownTimer?.cancel()
                    } else {
                        backspace()
                    }
                }
                override fun onFinish() {}
            }
            countDownTimer!!.start()
            true
        }
    }

    fun addDoneButton(iv: ImageView){
        val actionId = editorInfo.imeOptions and EditorInfo.IME_MASK_ACTION
        val inputType = editorInfo.inputType and InputType.TYPE_MASK_FLAGS

        if (InputType.TYPE_TEXT_FLAG_MULTI_LINE == inputType
            || InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE == inputType) {
            iv.setImageResource(R.drawable.ic_new_line_arrow)
            iv.setOnClickListener {
                addText("\n")
            }
        } else {
            when (actionId) {
                EditorInfo.IME_ACTION_SEND -> iv.setImageResource(R.drawable.ic_send)
                EditorInfo.IME_ACTION_DONE -> iv.setImageResource(R.drawable.ic_done)
                EditorInfo.IME_ACTION_NEXT -> iv.setImageResource(R.drawable.ic_arrow_right_alt)
                EditorInfo.IME_ACTION_GO -> iv.setImageResource(R.drawable.ic_arrow_forward)
                EditorInfo.IME_ACTION_SEARCH -> iv.setImageResource(R.drawable.ic_search)
                EditorInfo.IME_ACTION_PREVIOUS -> iv.setImageResource(R.drawable.ic_arroe_left_alt)
                else -> iv.setImageResource(R.drawable.ic_arrow_forward)
            }
            iv.setOnClickListener {
                currentInputConnection.performEditorAction(actionId)
                playSound()
            }
        }

    }
    fun playSound(veb: Long = 30){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(veb, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibration = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibration.vibrate(VibrationEffect.createOneShot(veb, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibration.vibrate(veb)
            }
        }
        println("test setVibrate")
    }

    fun showCalculatorKeyboard() {
        binding.normalKeyboard.root.visibility = View.GONE
        binding.numPadKeyboard.root.visibility = View.GONE
        binding.calculatorKeyboard.root.visibility = View.VISIBLE
        LocalStorage(this).saveDefKeyboard("calculator")
    }

    fun showNormalKeyboard() {
        binding.normalKeyboard.root.visibility = View.VISIBLE
        binding.numPadKeyboard.root.visibility = View.GONE
        binding.calculatorKeyboard.root.visibility = View.GONE
        LocalStorage(this).saveDefKeyboard("normal")
    }

    fun showNumberKeyboard() {
        binding.normalKeyboard.root.visibility = View.GONE
        binding.numPadKeyboard.root.visibility = View.VISIBLE
        binding.calculatorKeyboard.root.visibility = View.GONE
        LocalStorage(this).saveDefKeyboard("number")
    }


}