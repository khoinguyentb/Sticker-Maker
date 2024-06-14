package com.kan.dev.st051_stickermaker.ui.activity

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.databinding.ActivityVideoCutterBinding
import com.kan.dev.st051_stickermaker.utils.checkPermissionStorage
import com.kan.dev.st051_stickermaker.utils.getUriForImage
import com.video.trimmer.interfaces.OnVideoEditedListener
import com.video.trimmer.interfaces.OnVideoListener
import com.video.trimmer.utils.fileFromContentUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class VideoCutterActivity : BaseActivity<ActivityVideoCutterBinding>(), OnVideoEditedListener,
    OnVideoListener {
    override fun setViewBinding(): ActivityVideoCutterBinding {
        return ActivityVideoCutterBinding.inflate(layoutInflater)
    }
    private lateinit var path :String
    private var tempFile: File? = null
    override fun initData() {
        if (intent.hasExtra("PATH_VIDEO")){
            path = intent.getStringExtra("PATH_VIDEO")!!
            if (checkPermissionStorage()){
                tempFile = getUriForImage(path).fileFromContentUri(this)
                binding.apply {
                    videoTrimmer.setTextTimeSelectionTypeface(ResourcesCompat.getFont(this@VideoCutterActivity, R.font.roboto))
                        .setOnTrimVideoListener(this@VideoCutterActivity)
                        .setOnVideoListener(this@VideoCutterActivity)
                        .setVideoURI(Uri.parse(tempFile?.path))
                        .setBitrate(2)
                        .setVideoInformationVisibility(true)
                        .setMinDuration(2)
                        .setDestinationPath(Environment.getExternalStorageDirectory().path + File.separator + Environment.DIRECTORY_MOVIES)
                }
            }
        }
    }

    override fun initView() {
        binding.apply {
            videoTrimmer.rootView
        }
    }

    override fun initListener() {
        binding.apply {
            icBack.setOnClickListener {
                videoTrimmer.onCancelClicked()
                finish()
            }
            btnNext.setOnClickListener {
                videoTrimmer.onSaveClicked()
            }
        }
    }
    override fun onPause() {
        super.onPause()
        binding.videoTrimmer.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        tempFile?.delete()
    }
    override fun cancelAction() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.videoTrimmer.destroy()
            tempFile?.delete()
            finish()
        }
    }

    override fun getResult(uri: Uri) {
        lifecycleScope.launch(Dispatchers.Main) {
            startActivity(Intent(this@VideoCutterActivity,VideoCutterActivity::class.java).putExtra("PATH_VIDEO",uri.path))
        }
    }

    override fun onError(message: String) {
        val context = applicationContext
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
        Log.e("ERROR", message)
        tempFile?.delete()
    }

    override fun onProgress(percentage: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
//            progressDialog.updateProgress("Cropping video. Please wait..  " +
//                    "$percentage %")
        }
    }

    override fun onTrimStarted() {
        val context = applicationContext
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, "Started Trimming", Toast.LENGTH_SHORT).show()
//            progressDialog.show()
        }
    }

    override fun onVideoPrepared() {

    }

}