package com.kan.dev.st051_stickermaker.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.databinding.ActivityCutOutBinding
import com.kan.dev.st051_stickermaker.utils.OnValueListener
import com.kan.dev.st051_stickermaker.utils.PUT_BITMAP
import com.kan.dev.st051_stickermaker.utils.dp2px
import com.kan.dev.st051_stickermaker.utils.dpToPx
import java.io.ByteArrayOutputStream
import java.io.File


class CutOutActivity : BaseActivity<ActivityCutOutBinding>(),OnValueListener {
    override fun setViewBinding(): ActivityCutOutBinding {
        return ActivityCutOutBinding.inflate(layoutInflater)
    }
    private lateinit var path :String
    private lateinit var croppedBitmap : Bitmap
    private lateinit var intentCrop : Intent
    private val stream = ByteArrayOutputStream()
    override fun initData() {
        if (intent.hasExtra("PATH_IMAGE")){
            path = intent.getStringExtra("PATH_IMAGE")!!
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        binding.apply {
            imgCrop.setImageUriAsync(getUriForImage(path))
            imgCrop.setAspectRatio(3,4)
            imgCrop.isAutoZoomEnabled = false
            imgCrop.setMaxCropResultSize(dpToPx(this@CutOutActivity,300f), dpToPx(this@CutOutActivity,300f))
        }
    }
    override fun initListener() {
        binding.apply {
            btnAround.setOnClickListener {
                imgCrop.rotateImage(-90)
            }
            btnFlip.setOnClickListener {
                imgCrop.flipImageHorizontally()
            }
            tvReset.setOnClickListener {
                imgCrop.resetCropRect()
            }
            icBack.setOnClickListener {
                finish()
            }
            rvRotation.setChooseValueChangeListener(this@CutOutActivity)
            btnNext.setOnClickListener {
                croppedBitmap = imgCrop.croppedImage!!

                croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                intentCrop = Intent(this@CutOutActivity,EditActivity::class.java)
                intentCrop.putExtra(PUT_BITMAP,byteArray)
                startActivity(intentCrop)
                finish()
            }
        }
    }

    fun getUriForImage(imagePath: String): Uri {
        val imageFile = File(imagePath)
        return Uri.fromFile(imageFile)
    }

    override fun onValueListener(value: Float) {
        binding.apply {
            imgCrop.rotatedDegrees = value.toInt()
        }
    }
}