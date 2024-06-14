package com.kan.dev.st051_stickermaker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.HomeModel
import com.kan.dev.st051_stickermaker.databinding.ItemHomeBinding
import com.kan.dev.st051_stickermaker.interfaces.IHomeClickListener

class HomeAdapter(private val context: Context,private  val listener : IHomeClickListener):BaseAdapter<HomeModel,ItemHomeBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemHomeBinding {
        return ItemHomeBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemHomeBinding, item: HomeModel, position: Int) {
        binding.apply {
            tvTitle.setText(item.title)
            tvDes.setText(item.des)
            cardBtn.setCardBackgroundColor(ContextCompat.getColor(context,item.bgColor))
            btnDIY.setTextColor(ContextCompat.getColor(context,item.texColor))
//            if (position == 3){
//                root.setPadding()
//            }
            root.setBackgroundResource(item.bg)
            root.setOnClickListener {
                listener.isClickListener(position)
            }
        }
    }
}