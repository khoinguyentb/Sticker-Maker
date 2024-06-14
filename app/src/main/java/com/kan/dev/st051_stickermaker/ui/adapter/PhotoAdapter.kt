package com.kan.dev.st051_stickermaker.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseAdapter
import com.kan.dev.st051_stickermaker.data.model.PhotoModel
import com.kan.dev.st051_stickermaker.databinding.ItemPhotoBinding
import com.kan.dev.st051_stickermaker.interfaces.IPhotoListener
import java.util.concurrent.TimeUnit

class PhotoAdapter (private val context: Context,private val listener: IPhotoListener) : BaseAdapter<PhotoModel,ItemPhotoBinding>() {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemPhotoBinding {
        return ItemPhotoBinding.inflate(inflater,parent,false)
    }
    private var hours = 0L
    private var minutes = 0L
    private var seconds = 0L
    override fun bind(binding: ItemPhotoBinding, item: PhotoModel, position: Int) {
        binding.apply {
            Glide.with(context)
                .load(item.path)
                .into(imgPhoto)
            if (item.active){
                imgActive.setImageResource(R.drawable.selector)
            }else{
                imgActive.setImageResource(R.drawable.un_selector)
            }

            if (item.duration != 0L){
                tvDuration.text = formatDuration(item.duration)
            }else{
                tvDuration.visibility = View.GONE
            }

            root.setOnClickListener {
                setCheck(item.path)
                listener.isPhoto(item.path)
            }
        }
    }

    fun setCheck(path : String){
        for (item in listItem){
            item.active = item.path.equals(path)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("DefaultLocale")
    private fun formatDuration(durationInMillis: Long): String {
        hours = TimeUnit.MILLISECONDS.toHours(durationInMillis)
        minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60
        seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}