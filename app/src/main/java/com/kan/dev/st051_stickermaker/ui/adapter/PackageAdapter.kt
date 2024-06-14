package com.kan.dev.st051_stickermaker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.StickerPackageModel
import com.kan.dev.st051_stickermaker.databinding.ItemPackageBinding

class PackageAdapter (private val context: Context,private val listSticker : List<StickerPackageModel>) : BaseAdapter<String,ItemPackageBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPackageBinding {
        return ItemPackageBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemPackageBinding, item: String, position: Int) {
        binding.apply {
            tvPackageName.text = item

        }
    }
}