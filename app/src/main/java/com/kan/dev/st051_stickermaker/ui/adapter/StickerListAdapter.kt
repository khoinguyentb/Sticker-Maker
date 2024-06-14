package com.kan.dev.st051_stickermaker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.StickerModel
import com.kan.dev.st051_stickermaker.databinding.ItemImageStickerBinding

class StickerListAdapter(private val context: Context,private val listener: IStickerListListener) :BaseAdapter<StickerModel,ItemImageStickerBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemImageStickerBinding {
        return ItemImageStickerBinding.inflate(inflater,parent,false)
    }
    override fun bind(binding: ItemImageStickerBinding, item: StickerModel, position: Int) {
        binding.apply {
            Glide.with(context).load("https://lvtglobal.site/" + item.link).into(imgSticker)

            root.setOnClickListener {
                listener.clickItemSticker(item)
            }
        }
    }
}

interface IStickerListListener{
    fun clickItemSticker(item: StickerModel)
}