package com.kan.dev.st051_stickermaker.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.FontModel
import com.kan.dev.st051_stickermaker.databinding.ItemFontBinding

class FontAdapter(private val context: Context,private val listener : IFontChangeListener) : BaseAdapter<FontModel,ItemFontBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFontBinding {
        return ItemFontBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemFontBinding, item: FontModel, position: Int) {
        binding.apply {
            tvFont.text = item.text
            tvFont.typeface = ResourcesCompat.getFont(context, item.font)
            tvFont.setOnClickListener {
                setCheck(font = item.font)
                Log.d("Kan.exe",item.text)
                listener.fontChangeListener(item.font)
            }
            if (item.active){
                tvFont.setBackgroundResource(R.drawable.font_selector)
            }else{
                tvFont.setBackgroundResource(R.drawable.font_un)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setCheck(font : Int){
        for(item in listItem){
            item.active = font == item.font
        }
        notifyDataSetChanged()
    }
}

interface IFontChangeListener{
    fun fontChangeListener(font : Int)
}