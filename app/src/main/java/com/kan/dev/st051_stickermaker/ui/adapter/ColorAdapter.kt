package com.kan.dev.st051_stickermaker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.ColorModel
import com.kan.dev.st051_stickermaker.databinding.ItemPaintBinding

class ColorAdapter(private val context: Context,private val listener: IColorClickListener):BaseAdapter<ColorModel,ItemPaintBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPaintBinding {
        return ItemPaintBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemPaintBinding, item: ColorModel, position: Int) {
        binding.apply {
            if (item.active){
                root.setBackgroundResource(R.drawable.font_selector)
            } else{
                if (position > 1){
                    root.setBackgroundColor(ContextCompat.getColor(context,R.color.transfers))
                }else{
                    root.setBackgroundResource(R.drawable.bg_color_1)
                }
            }
            if (position == 0){
                icPaint.setImageResource(R.drawable.ic_paint)
            } else{
                icPaint.setColorFilter(ContextCompat.getColor(context,R.color.transfers))
                cardPainColor.setCardBackgroundColor(ContextCompat.getColor(context,item.color))
            }
            root.setOnClickListener {
                if (!item.active){
                    setCheck(item.color)
                    listener.onColorClicked(position,item)
                }
            }
        }
    }
    fun setCheck(color : Int){
        listItem.forEach { colorModel ->
            colorModel.active = colorModel.color == color
        }
        notifyDataSetChanged()
    }
}

interface IColorClickListener{
    fun onColorClicked(position: Int,item: ColorModel)
}