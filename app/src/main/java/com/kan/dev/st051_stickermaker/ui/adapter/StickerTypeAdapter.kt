package com.kan.dev.st051_stickermaker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.StickerModel
import com.kan.dev.st051_stickermaker.data.model.TypeStickerModel
import com.kan.dev.st051_stickermaker.databinding.ItemStickerEditBinding

class StickerTypeAdapter(private val context: Context,private val listener: IStickerTypeListener) :BaseAdapter<TypeStickerModel,ItemStickerEditBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemStickerEditBinding {
        return ItemStickerEditBinding.inflate(inflater,parent,false)
    }
    private var itemSelector = 0
    override fun bind(binding: ItemStickerEditBinding, item: TypeStickerModel, position: Int) {
        binding.apply {
            if (itemSelector == position){
                root.setBackgroundResource(R.drawable.bg_selector_sticker)
            }else{
                root.setBackgroundColor(ContextCompat.getColor(context,R.color.transfers))
            }
            Glide.with(context).load("https://lvtglobal.site/" + item.listSticker[0].link).into(icSticker)
            root.setOnClickListener {
                itemSelector = position
                listener.clickSticker(item.listSticker)
                notifyDataSetChanged()
            }
        }
    }
}

interface IStickerTypeListener{
    fun clickSticker(listSicker : List<StickerModel>)
}