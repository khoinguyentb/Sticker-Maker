package com.kan.dev.st051_stickermaker.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.StickModel
import com.kan.dev.st051_stickermaker.databinding.ItemStickBinding

class StickAdapter(private val context: Context,private val listener: IStickClickListener) : BaseAdapter<StickModel,ItemStickBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemStickBinding {
        return ItemStickBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemStickBinding, item: StickModel, position: Int) {
        binding.apply {
            if (item.active){
//                root.setBackgroundResource(R.drawable.font_selector)
                icSelector.setImageResource(R.drawable.selector)
            }else{
//                root.setBackgroundColor(ContextCompat.getColor(context,R.color.transfers))
                icSelector.setImageResource(R.drawable.un_selector)
            }
            imgStick.setImageResource(item.stick)
            root.setOnClickListener{
                setCheck(item.stick)
                listener.clickStick(position,item.stick)
            }

        }
    }

    fun setCheck(stick :Int){
        listItem.forEach { stickModel ->
            stickModel.active = stickModel.stick == stick
        }
        notifyDataSetChanged()
    }
}
interface IStickClickListener{
    fun clickStick(position: Int,img : Int)
}