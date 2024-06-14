package com.kan.dev.st051_stickermaker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.FolderModel
import com.kan.dev.st051_stickermaker.databinding.ItemFilterBinding
import com.kan.dev.st051_stickermaker.interfaces.IFolderChangeListener

class FolderAdapter(private val context : Context,private val listener : IFolderChangeListener) : BaseAdapter<FolderModel,ItemFilterBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFilterBinding {
        return ItemFilterBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemFilterBinding, item: FolderModel, position: Int){
        binding.apply {
            tvDescription.text = item.name
            if (item.active){
                tvDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.selectorFolder))
            }else{
                tvDescription.setBackgroundColor(ContextCompat.getColor(context, R.color.transfers))
            }
            root.setOnClickListener {
                setCheck(item.name)
                listener.iChangeFolderClick(item.images,item.name)
            }
        }
    }
    fun setCheck(name : String){
        for (item in listItem){
            item.active = item.name.equals(name)
        }
        notifyDataSetChanged()
    }

}