package com.kan.dev.st051_stickermaker.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.SettingModel
import com.kan.dev.st051_stickermaker.databinding.ItemSettingBinding
import com.kan.dev.st051_stickermaker.interfaces.ISettingClickListener

class SettingAdapter(private val listener: ISettingClickListener) : BaseAdapter<SettingModel, ItemSettingBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemSettingBinding {
        return ItemSettingBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemSettingBinding, item: SettingModel, position: Int) {
        binding.apply {
            icSetting.setImageResource(item.image)
            tvItemSetting.setText(item.text)
            if (position == 0){
                imgNext.visibility = View.VISIBLE
            }else{
                imgNext.visibility = View.INVISIBLE
            }
            root.setOnClickListener {
                listener.iSettingClick(item.image)
            }
        }
    }
}