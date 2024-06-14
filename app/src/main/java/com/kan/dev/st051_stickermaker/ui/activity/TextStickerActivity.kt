package com.kan.dev.st051_stickermaker.ui.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.databinding.ActivityTextStickerBinding
import com.kan.dev.st051_stickermaker.dialog.DialogEnterText
import com.kan.dev.st051_stickermaker.interfaces.IEditTextDialogListener
import com.kan.dev.st051_stickermaker.utils.convertBitmap
import com.kan.dev.st051_stickermaker.utils.stickerview.DrawableSticker
import com.kan.dev.st051_stickermaker.utils.stickerview.Sticker

class TextStickerActivity : BaseActivity<ActivityTextStickerBinding>(), IEditTextDialogListener {
    override fun setViewBinding(): ActivityTextStickerBinding {
        return ActivityTextStickerBinding.inflate(layoutInflater)
    }
    private val dialogSticker: DialogEnterText by lazy {
        DialogEnterText(this, this)
    }
    private lateinit var bitmap: Bitmap
    private lateinit var drawable: Drawable
    private lateinit var textSticker: DrawableSticker
    private var checkEdit = false
    private var id = 0
    override fun initData() {
        id = intent.getIntExtra("Sticker",0)
    }

    override fun initView() {
        dialogSticker.show()
    }

    override fun initListener() {
        binding.apply {
            Text.setOnClickListener {
                dialogSticker.show()
            }
            Sticker.setOnClickListener {
                layoutSticker.root.visibility = View.VISIBLE
                layoutText.visibility = View.INVISIBLE
            }
            Effect.setOnClickListener {

            }
        }
    }

    override fun IClickDoneListener(str: String) {
        binding.apply {
            layoutTextSticker.apply {
                val textStickerViews = listOf(
                    textSticker1, textSticker2, textSticker3,
                    textSticker4, textSticker5, textSticker6,
                    textSticker7, textSticker8, textSticker9,
                    textSticker11, textSticker12,
                    textSticker13, textSticker14, textSticker15,
                    textSticker16, textSticker17, textSticker18,
                    textSticker19, textSticker20, textSticker21,
                    textSticker22, textSticker23, textSticker24
                )

                val tvStickerViews = listOf(
                    tvSticker1, tvSticker2, tvSticker3,
                    tvSticker4, tvSticker5, tvSticker6,
                    tvSticker7, tvSticker8, tvSticker9,
                    tvSticker11, tvSticker12,
                    tvSticker13, tvSticker14, tvSticker15,
                    tvSticker16, tvSticker17, tvSticker18,
                    tvSticker19, tvSticker20, tvSticker21,
                    tvSticker22, tvSticker23, tvSticker24
                )
                when(id){
                    in textStickerViews.indices -> {
                        hideAllChildViews(root)
                        textStickerViews[id].visibility = View.VISIBLE
                        tvStickerViews[id].text = str
                        setTextSticker(textStickerViews[id], tvStickerViews[id])
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun hideAllChildViews(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val childView = view.getChildAt(i)
                childView.visibility = View.GONE
            }
        }
    }
    private fun setTextSticker(view: View,textView: TextView) {
        binding.apply {
            textView.post {
                bitmap = convertBitmap(view)
                drawable = BitmapDrawable(resources, bitmap)
                textSticker = DrawableSticker(drawable)
                stickerView.addSticker(textSticker)
            }
        }
    }

}