package com.kan.dev.st051_stickermaker.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.data.data.Companion.stickList
import com.kan.dev.st051_stickermaker.databinding.ActivitySelectorTextStickerBinding
import com.kan.dev.st051_stickermaker.ui.adapter.IStickClickListener
import com.kan.dev.st051_stickermaker.ui.adapter.StickAdapter
import com.kan.dev.st051_stickermaker.utils.handler
import com.kan.dev.st051_stickermaker.utils.isClick

class SelectorTextStickerActivity : BaseActivity<ActivitySelectorTextStickerBinding>() {
    override fun setViewBinding(): ActivitySelectorTextStickerBinding {
        return ActivitySelectorTextStickerBinding.inflate(layoutInflater)
    }

    private lateinit var adapter : StickAdapter
    private var sticker = 0
    override fun initData() {
        adapter = StickAdapter(this,object : IStickClickListener{
            override fun clickStick(position: Int, img: Int) {
                sticker = position
            }
        })

        adapter.setItems(stickList)
        adapter.setCheck(R.drawable.stick_0)

    }

    override fun initView() {
        binding.apply {
            rcvTextSticker.adapter = adapter
            rcvTextSticker.layoutManager = GridLayoutManager(this@SelectorTextStickerActivity,3)
        }
    }

    override fun initListener() {
        binding.apply {
            Back.setOnClickListener {
                finish()
            }
            btnDone.setOnClickListener {
                if (isClick){
                    isClick = false
                    intent = Intent(this@SelectorTextStickerActivity,TextStickerActivity::class.java)
                    intent.putExtra("Sticker",sticker)
                    startActivity(intent)
                    handler.postDelayed({ isClick = true},500)
                }
            }
        }
    }
}