package com.jsb.calculator.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jsb.calculator.R
import com.jsb.calculator.databinding.ActivityCustomizeKeyboardBinding
import com.jsb.calculator.databinding.DialogEachKeyBinding
import com.jsb.calculator.manager.LocalStorage
import com.jsb.calculator.modules.MyFont

class CustomizeKeyboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCustomizeKeyboardBinding
    
    private var isCapes = false
    private lateinit var font: MyFont
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomizeKeyboardBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        binding.btCCaall.isEnabled = false
        binding.btTextToSpeech.isEnabled = false
        binding.btCopy.isEnabled = false
        binding.btPaste.isEnabled = false
        binding.btBackspace.isEnabled = false
        binding.btQ123.isEnabled = false
        binding.btComa.isEnabled = false
        binding.btEmoji.isEnabled = false
        binding.btSpace.isEnabled = false
        binding.btDot2.isEnabled = false
        binding.btKeyEnter.isEnabled = false
        binding.btCaps.setOnClickListener {
            isCapes = !isCapes
            setCaps(isCapes)
        }
        font = LocalStorage(this).getFont()
        setCaps(isCapes)
        addOnClick(binding.btKey1, "q")
        addOnClick(binding.btKey2, "w")
        addOnClick(binding.btKey3, "e")
        addOnClick(binding.btKey4, "r")
        addOnClick(binding.btKey5, "t")
        addOnClick(binding.btKey6, "y")
        addOnClick(binding.btKey7, "u")
        addOnClick(binding.btKey8, "i")
        addOnClick(binding.btKey9, "o")
        addOnClick(binding.btKey10, "p")
        addOnClick(binding.btKey11, "a")
        addOnClick(binding.btKey12, "s")
        addOnClick(binding.btKey13, "d")
        addOnClick(binding.btKey14, "f")
        addOnClick(binding.btKey15, "g")
        addOnClick(binding.btKey16, "h")
        addOnClick(binding.btKey17, "j")
        addOnClick(binding.btKey18, "k")
        addOnClick(binding.btKey19, "l")
        addOnClick(binding.btKey20, "z")
        addOnClick(binding.btKey21, "x")
        addOnClick(binding.btKey22, "c")
        addOnClick(binding.btKey23, "v")
        addOnClick(binding.btKey24, "b")
        addOnClick(binding.btKey25, "n")
        addOnClick(binding.btKey26, "a")
    }

    private fun addOnClick(bt: TextView, name: String) {
        bt.setOnClickListener {
            showDialog(name)
        }
    }


    private fun showDialog(name: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Key")
        val keyBinding = DialogEachKeyBinding.inflate(
            layoutInflater
        )

        val field = font.javaClass.getDeclaredField(name)
        field.isAccessible = true
        val bigField = font.javaClass.getDeclaredField("big${name.uppercase()}")
        bigField.isAccessible = true
        keyBinding.lket.setText(field.get(font) as String)
        keyBinding.uket.setText(bigField.get(font) as String)
        builder.setView(keyBinding.root)
        builder.setPositiveButton("SAVE") { dialog: DialogInterface?, which: Int ->
            val upper = keyBinding.uket.text.toString()
            val lover = keyBinding.lket.text.toString()
            if (lover.isNotEmpty() && upper.isNotEmpty() && !lover.contains(",") && !lover.contains("=") && !upper.contains(
                    ","
                ) && !upper.contains("=")
            ) {

                field.set(font, lover)
                bigField.set(font, upper)

                LocalStorage(this).saveFont(font)
                setCaps(isCapes)
            } else {
                Toast.makeText(this, "Invalid key", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    private fun setCaps(caps: Boolean) {
        if (caps) {
            binding.btCaps.setImageResource(R.drawable.ic_caps2)
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
            binding.btCaps.setImageResource(R.drawable.ic_caps1)
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
}