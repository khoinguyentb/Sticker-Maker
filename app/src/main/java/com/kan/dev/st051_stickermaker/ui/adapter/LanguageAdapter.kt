package com.kan.dev.st051_stickermaker.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.LanguageModel
import com.kan.dev.st051_stickermaker.databinding.ItemLanguageBinding
import com.kan.dev.st051_stickermaker.interfaces.ILanguageClick

class LanguageAdapter(private val listener: ILanguageClick) : BaseAdapter<LanguageModel, ItemLanguageBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemLanguageBinding {
        return ItemLanguageBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemLanguageBinding, item: LanguageModel, position: Int) {
        binding.apply {
            imgflag.setImageResource(item.flag)
            tvLanguage.text = item.language
            if (item.active){
                root.setBackgroundResource(R.drawable.language_selector)
            }else{
                root.setBackgroundResource(R.drawable.language_un_selector)
            }
            root.setOnClickListener{
                setCheck(item.codeLang)
                listener.onClickItem(item.codeLang)
            }
        }
    }

    fun setCheck(code: String?) {
        for (item in listItem) {
            item.active = item.codeLang.equals(code)
        }
        notifyDataSetChanged()
    }

}