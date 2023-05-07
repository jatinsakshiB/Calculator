package com.jsb.calculator.view_manager

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.inputmethodservice.InputMethodService
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import com.jsb.calculator.classes.Fonts
import com.jsb.calculator.keyboard_service.SimpleIME
import com.jsb.calculator.R
import com.jsb.calculator.classes.DoubleClickListener
import com.jsb.calculator.databinding.KeyboardAbcBinding
import com.jsb.calculator.manager.LocalStorage
import com.jsb.calculator.modules.MyFont
import java.util.Locale

class TextKeyboardViewManager(
    var context: SimpleIME,
    var currentInputConnection: InputConnection?,
    private var binding: KeyboardAbcBinding
) : OnTouchListener {


    private lateinit var textToSpeech: TextToSpeech

    private var capsStatus: Int = 0

    var font: MyFont = LocalStorage(context).getFont()
    
    init {
        setUpTextToSpeech()
        binding.btTextToSpeech.isEnabled = false
        setUpClicks()

    }

    fun refresh(currentInputConnection: InputConnection?){
        this.currentInputConnection = currentInputConnection
        context.addDoneButton(binding.btKeyEnter)
        setNormalKeys()
        setUpTextToSpeech()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpClicks() {
        setNormalKeys()
        binding.btShowKeyboardCalculator.setOnClickListener {
            context.showCalculatorKeyboard()
        }

        binding.btTextToSpeech.setOnClickListener {
            if (currentInputConnection != null){
                try {
                    val text = currentInputConnection!!.getExtractedText(ExtractedTextRequest(), 0).text.toString()
                    if (text.isNotEmpty()) {
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                } catch (e: Exception) {
                    context.showToast("Failed to use text-to-speech feature.")
                }
            }
        }

        binding.btCopy.setOnClickListener {
            if (currentInputConnection != null){
                val clipboard = context.getSystemService(InputMethodService.CLIPBOARD_SERVICE) as ClipboardManager
                val text = if (!currentInputConnection!!.getSelectedText(0).isNullOrEmpty()){
                    currentInputConnection!!.getSelectedText(0)
                }else{
                    try {
                        val text = currentInputConnection!!.getExtractedText(ExtractedTextRequest(), 0).text.toString()
                        currentInputConnection!!.setSelection(text.length, 0)
                        text
                    } catch (e: Exception) {
                        ""
                    }
                }
                if (text.isNotEmpty()) {
                    val clip = ClipData.newPlainText(
                        "jsb:" + System.currentTimeMillis(),
                        text
                    )
                    clipboard.setPrimaryClip(clip)
                    context.showToast("Text copied")
                }
            }
        }

        binding.btPaste.setOnClickListener {
            val clipboard = context.getSystemService(InputMethodService.CLIPBOARD_SERVICE) as ClipboardManager
            try {
                val clipboardText = clipboard.primaryClip!!.getItemAt(0)
                if (clipboardText != null && clipboardText.text != null) {
                    context.addText(clipboardText.text.toString())
                } else {
                    context.showToast("Nothing in clipboard")
                }
            } catch (e: Exception) {
                e.message?.let { it1 -> context.showToast(it1) }
            }
        }

        context.addBackspace(binding.btBackspace)
        context.addDoneButton(binding.btKeyEnter)

        binding.btSpace.setOnLongClickListener {
            val imeManager =
                context.getSystemService(InputMethodService.INPUT_METHOD_SERVICE) as InputMethodManager
            imeManager.showInputMethodPicker()
            true
        }

        binding.btSpace.setOnClickListener {
            context.addText(" ")
        }
        binding.btComa.setOnTouchListener(this)
        binding.btDot2.setOnTouchListener(this)
        binding.btKey27.setOnTouchListener(this)
        binding.btKey1.setOnTouchListener(this)
        binding.btKey2.setOnTouchListener(this)
        binding.btKey3.setOnTouchListener(this)
        binding.btKey4.setOnTouchListener(this)
        binding.btKey5.setOnTouchListener(this)
        binding.btKey6.setOnTouchListener(this)
        binding.btKey7.setOnTouchListener(this)
        binding.btKey8.setOnTouchListener(this)
        binding.btKey9.setOnTouchListener(this)
        binding.btKey10.setOnTouchListener(this)
        binding.btKey11.setOnTouchListener(this)
        binding.btKey12.setOnTouchListener(this)
        binding.btKey13.setOnTouchListener(this)
        binding.btKey14.setOnTouchListener(this)
        binding.btKey15.setOnTouchListener(this)
        binding.btKey16.setOnTouchListener(this)
        binding.btKey17.setOnTouchListener(this)
        binding.btKey18.setOnTouchListener(this)
        binding.btKey19.setOnTouchListener(this)
        binding.btKey20.setOnTouchListener(this)
        binding.btKey21.setOnTouchListener(this)
        binding.btKey22.setOnTouchListener(this)
        binding.btKey23.setOnTouchListener(this)
        binding.btKey24.setOnTouchListener(this)
        binding.btKey25.setOnTouchListener(this)
        binding.btKey26.setOnTouchListener(this)

        if (LocalStorage(context).hasFont()){
            binding.actionsLl2.addView(newText("CUSTOM") {
                this.font = LocalStorage(context).getFont()
                setNormalKeys()
            })
        }

        Fonts.fontList.forEach {
            addFontBt(it)
        }
    }

    private fun addFontBt(font: MyFont) {
        binding.actionsLl2.addView(newText(font.bigA + font.bigB + font.bigC + font.bigD) {
            this.font = font
            setNormalKeys()
        })
    }

    private fun newText(text: String, onClickListener: View.OnClickListener): TextView {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val textView = TextView(context)
        textView.textSize = 20f
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.setPadding(20, 0, 20, 0)
        textView.setBackgroundResource(R.drawable.button_r1)
        textView.layoutParams = layoutParams
        textView.setOnClickListener(onClickListener)
        return textView
    }

    private fun setQ123Keys() {
        binding.btKey1.text = "1"
        binding.btKey2.text = "2"
        binding.btKey3.text = "3"
        binding.btKey4.text = "4"
        binding.btKey5.text = "5"
        binding.btKey6.text = "6"
        binding.btKey7.text = "7"
        binding.btKey8.text = "8"
        binding.btKey9.text = "9"
        binding.btKey10.text = "0"
        binding.btKey27.text = "@"
        binding.btKey11.text = "#"
        binding.btKey12.text = "$"
        binding.btKey13.text = "_"
        binding.btKey14.text = "&"
        binding.btKey15.text = "-"
        binding.btKey16.text = "+"
        binding.btKey17.text = "("
        binding.btKey18.text = ")"
        binding.btKey19.text = "/"
        binding.btKey20.text = "*"
        binding.btKey21.text = "\""
        binding.btKey22.text = "'"
        binding.btKey23.text = ":"
        binding.btKey24.text = ";"
        binding.btKey25.text = "!"
        binding.btKey26.text = "?"
        binding.btEmoji.textSize = 14f
        binding.btEmoji.text = "12\n34"
        binding.btEmoji.setOnClickListener { context.showNumberKeyboard() }
        binding.btQ123.text = "ABC"
        binding.btQ123.setOnClickListener {
            setNormalKeys()
            context.playSound()
        }
        binding.btComa.text = ","
        binding.btDot2.text = "."
        binding.btKey27.visibility = View.VISIBLE
        binding.btCaps.setImageResource(R.drawable.ic_text_spc)
        binding.btCaps.setOnClickListener {
            setSPCKeys()
            context.playSound()
        }
    }

    private fun setSPCKeys() {
        binding.btKey1.text = "~"
        binding.btKey2.text = "`"
        binding.btKey3.text = "|"
        binding.btKey4.text = "•"
        binding.btKey5.text = "√"
        binding.btKey6.text = "π"
        binding.btKey7.text = "÷"
        binding.btKey8.text = "×"
        binding.btKey9.text = "="
        binding.btKey10.text = "^"
        binding.btKey27.text = "$"
        binding.btKey11.text = "₹"
        binding.btKey12.text = "€"
        binding.btKey13.text = "¥"
        binding.btKey14.text = "£"
        binding.btKey15.text = "₿"
        binding.btKey16.text = "°"
        binding.btKey17.text = "{"
        binding.btKey18.text = "}"
        binding.btKey19.text = "\\"
        binding.btKey20.text = "%"
        binding.btKey21.text = "©"
        binding.btKey22.text = "®"
        binding.btKey23.text = "™"
        binding.btKey24.text = "✓"
        binding.btKey25.text = "["
        binding.btKey26.text = "]"
        binding.btComa.text = "<"
        binding.btDot2.text = ">"
        binding.btEmoji.textSize = 14f
        binding.btEmoji.text = "12\n34"
        binding.btQ123.text = "ABC"
        binding.btQ123.setOnClickListener {
            setNormalKeys()
            context.playSound()
        }
        binding.btEmoji.setOnClickListener { context.showNumberKeyboard() }
        binding.btKey27.visibility = View.VISIBLE
        binding.btCaps.setImageResource(R.drawable.ic_text_q123)
        binding.btCaps.setOnClickListener {
            setQ123Keys()
            context.playSound()
        }
    }

    private fun setNormalKeys() {
        binding.btComa.text = ","
        binding.btDot2.text = "."
        binding.btEmoji.textSize = 20f
        binding.btEmoji.text = "\uD83D\uDE00"
        binding.btEmoji.setOnClickListener { context.showToast("Coming soon") }
        binding.btQ123.text = "?123"
        binding.btQ123.setOnClickListener {
            setQ123Keys()
            context.playSound()
        }
        binding.btKey27.visibility = View.GONE
        binding.btCaps.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick() {
                capsStatus = 2
                setCapsKeys(true)
            }

            override fun onSingleClick() {
                if (capsStatus == 0){
                    capsStatus = 1
                    setCapsKeys(true)
                }else {
                    capsStatus = 0
                    setCapsKeys(false)
                }
                context.playSound()
            }
        })

        setCapsKeys(capsStatus == 1 || capsStatus == 2)
    }


    private fun setCapsKeys(caps: Boolean) {
        when (capsStatus) {
            0 -> {
                binding.btCaps.setImageResource(R.drawable.ic_caps1)
            }
            1 -> {
                binding.btCaps.setImageResource(R.drawable.ic_caps2)
            }
            2 -> {
                binding.btCaps.setImageResource(R.drawable.ic_caps3)
            }
        }
        if (caps) {
            binding.btKey1.text = font.bigQ
            binding.btKey2.text = font.bigW
            binding.btKey3.text = font.bigE
            binding.btKey4.text = font.bigR
            binding.btKey5.text = font.bigT
            binding.btKey6.text = font.bigY
            binding.btKey7.text = font.bigU
            binding.btKey8.text = font.bigI
            binding.btKey9.text = font.bigO
            binding.btKey10.text = font.bigP
            binding.btKey11.text = font.bigA
            binding.btKey12.text = font.bigS
            binding.btKey13.text = font.bigD
            binding.btKey14.text = font.bigF
            binding.btKey15.text = font.bigG
            binding.btKey16.text = font.bigH
            binding.btKey17.text = font.bigJ
            binding.btKey18.text = font.bigK
            binding.btKey19.text = font.bigL
            binding.btKey20.text = font.bigZ
            binding.btKey21.text = font.bigX
            binding.btKey22.text = font.bigC
            binding.btKey23.text = font.bigV
            binding.btKey24.text = font.bigB
            binding.btKey25.text = font.bigN
            binding.btKey26.text = font.bigM
        } else {
            binding.btKey1.text = font.q
            binding.btKey2.text = font.w
            binding.btKey3.text = font.e
            binding.btKey4.text = font.r
            binding.btKey5.text = font.t
            binding.btKey6.text = font.y
            binding.btKey7.text = font.u
            binding.btKey8.text = font.i
            binding.btKey9.text = font.o
            binding.btKey10.text = font.p
            binding.btKey11.text = font.a
            binding.btKey12.text = font.s
            binding.btKey13.text = font.d
            binding.btKey14.text = font.f
            binding.btKey15.text = font.g
            binding.btKey16.text = font.h
            binding.btKey17.text = font.j
            binding.btKey18.text = font.k
            binding.btKey19.text = font.l
            binding.btKey20.text = font.z
            binding.btKey21.text = font.x
            binding.btKey22.text = font.c
            binding.btKey23.text = font.v
            binding.btKey24.text = font.b
            binding.btKey25.text = font.n
            binding.btKey26.text = font.m
        }
    }
   

    private fun setUpTextToSpeech() {
        textToSpeech = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                binding.btTextToSpeech.isEnabled = true
            }
        }
        textToSpeech.language = Locale.getDefault()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        if (view == null) return false
        if (motionEvent == null) return false
        val text = (view as TextView).text.toString()
        when (motionEvent.action) {
            MotionEvent.ACTION_UP -> {
                context.addText(text)
                binding.previewRoot.visibility = View.GONE
                if (capsStatus == 1){
                    capsStatus = 0
                    setCapsKeys(false)
                }
            }
            MotionEvent.ACTION_DOWN -> {
                val lp = FrameLayout.LayoutParams(view.getWidth(), view.getHeight() * 2)
                binding.previewRoot.layoutParams = lp
                binding.previewTv.text = text
                binding.previewRoot.visibility = View.VISIBLE
                binding.previewRoot.y = (view.getParent() as View).y - view.getHeight()
                binding.previewRoot.x = view.getX()
            }
            MotionEvent.ACTION_CANCEL -> {
                binding.previewRoot.visibility = View.GONE
            }
        }
        return true
    }

}