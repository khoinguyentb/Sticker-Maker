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
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.databinding.DialogRateBinding
import com.kan.dev.st051_stickermaker.utils.SharePreferencesUtils

class DialogRate(private val context: Context, private val listener: OnDialogDismissListener) : Dialog(context) {
    var rating: Float = 0F
    private val bindingDialogRate by lazy {
        DialogRateBinding.inflate(layoutInflater)
    }
    private lateinit var sharePre : SharePreferencesUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingDialogRate.root)
        setCancelable(false)
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        window?.attributes?.gravity = Gravity.CENTER

        sharePre = SharePreferencesUtils(context)
        bindingDialogRate.tvTitle.isSelected = true
    }

    override fun show() {
        super.show()
        bindingDialogRate.ratingBar.rating = 0f
        bindingDialogRate.btnRate.setOnClickListener {
            if (rating > 3){
                sharePre.forceRated(context)
                rateApp(context)
            }else{
                dismiss()
            }

        }

        bindingDialogRate.tvExit.setOnClickListener {
            dismiss()
        }

        bindingDialogRate.ratingBar.setOnRatingChangeListener { ratingBar, rating, fromUser ->
            this.rating = rating
            when (rating) {
                0F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_0)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_0)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_0)
                }

                1F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_1)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_1)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_1)
                }

                2F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_2)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_2)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_2)
                }

                3F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_3)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_3)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_3)
                }

                4F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_4)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_4)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_4)
                }

                5F -> {
                    bindingDialogRate.img.setImageResource(R.drawable.rate_5)
                    bindingDialogRate.tvTitle.text = context.getString(R.string.title_rate_5)
                    bindingDialogRate.tvDes.text = context.getString(R.string.des_rate_5)
                }
            }
        }
    }


    fun rateApp(context: Context) {
        val manager = ReviewManagerFactory.create(context)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(context as Activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    dismiss()
                }
            } else {
                dismiss()
                @ReviewErrorCode val reviewErrorCode =
                    (task.exception as ReviewException).errorCode
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        listener.onDialogDismiss()
    }
}

interface OnDialogDismissListener {
    fun onDialogDismiss()
}
