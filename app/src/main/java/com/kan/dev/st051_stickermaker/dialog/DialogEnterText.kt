package com.kan.dev.st051_stickermaker.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import com.kan.dev.st051_stickermaker.databinding.DialogEnterTextBinding
import com.kan.dev.st051_stickermaker.interfaces.IEditTextDialogListener

class DialogEnterText(private val context: Activity,private val listener: IEditTextDialogListener) : Dialog(context) {

    private val binding : DialogEnterTextBinding by lazy {
        DialogEnterTextBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCancelable(false)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        window?.attributes?.gravity = Gravity.CENTER
    }

    override fun show() {
        super.show()
        binding.apply {
            icClose.setOnClickListener {
                dismiss()
            }
            icDone.setOnClickListener {
                listener.IClickDoneListener(edtText.text.toString())
                dismiss()
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        context.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

}
