package com.kan.dev.st051_stickermaker.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kan.dev.st051_stickermaker.data.model.FolderModel
import com.kan.dev.st051_stickermaker.data.model.PhotoModel
import com.kan.dev.st051_stickermaker.databinding.DialogFolderBinding
import com.kan.dev.st051_stickermaker.interfaces.IFolderChangeListener
import com.kan.dev.st051_stickermaker.ui.adapter.FolderAdapter
import com.kan.dev.st051_stickermaker.utils.dpToPx

class DialogFilter (private val context: Context, private val listener : onDismissFilterListener,private val folders : List<FolderModel>) : Dialog(context),IFolderChangeListener {
    private val binding by lazy {
        DialogFolderBinding.inflate(layoutInflater)
    }

    private val adapter : FolderAdapter by lazy {
        FolderAdapter(context,this)
    }
    private val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    private lateinit var images : List<PhotoModel>
    private lateinit var name : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCancelable(false)
        window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        val layoutParams = window?.attributes
        layoutParams?.x = dpToPx(context,30f)
        layoutParams?.y = dpToPx(context,50f)
        window?.attributes?.gravity = Gravity.TOP or Gravity.START
        window?.attributes = layoutParams
    }

    override fun show() {
        super.show()
        binding.apply {
            adapter.setItems(folders)
            rcvFilter.adapter = adapter
            rcvFilter.layoutManager = layoutManager
        }
    }

    override fun dismiss() {
        super.dismiss()
        listener.clickFilter(images,name)
    }

    override fun iChangeFolderClick(images: List<PhotoModel>, name: String) {
       this.images = images
        this.name = name
        dismiss()
    }
}

interface onDismissFilterListener{
    fun clickFilter(images: List<PhotoModel>,name : String)
}