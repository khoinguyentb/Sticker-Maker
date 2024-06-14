package com.kan.dev.st051_stickermaker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.IntroModel
import com.kan.dev.st051_stickermaker.databinding.ItemIntroBinding

class IntroAdapter() : BaseAdapter<IntroModel,ItemIntroBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemIntroBinding {
        return ItemIntroBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemIntroBinding, item: IntroModel, position: Int) {
        binding.apply {
            imgIntro.setImageResource(item.image)
        }
    }
}