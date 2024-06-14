package com.kan.dev.st051_stickermaker.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Environment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.data.model.FolderModel
import com.kan.dev.st051_stickermaker.data.model.PhotoModel
import com.kan.dev.st051_stickermaker.databinding.ActivityPhotoBinding
import com.kan.dev.st051_stickermaker.dialog.DialogFilter
import com.kan.dev.st051_stickermaker.dialog.onDismissFilterListener
import com.kan.dev.st051_stickermaker.interfaces.IPhotoListener
import com.kan.dev.st051_stickermaker.ui.adapter.PhotoAdapter
import com.kan.dev.st051_stickermaker.utils.REQUEST_CODE_STORAGE
import com.kan.dev.st051_stickermaker.utils.checkPermissionStorage
import com.kan.dev.st051_stickermaker.utils.requestAppPermissionStorage
import com.kan.dev.st051_stickermaker.utils.showPermissionSettingsDialog
import java.io.File

class PhotoActivity : BaseActivity<ActivityPhotoBinding>(),IPhotoListener, onDismissFilterListener {
    override fun setViewBinding(): ActivityPhotoBinding {
        return ActivityPhotoBinding.inflate(layoutInflater)
    }
    private val folders = mutableListOf<FolderModel>()
    private val adapter : PhotoAdapter by lazy {
        PhotoAdapter(this,this)
    }
    private val layoutManager: RecyclerView.LayoutManager by lazy {
        GridLayoutManager(this,3)
    }
    private lateinit var dialogFilter: DialogFilter
    private lateinit var path: String
    private var isVideo = false
    override fun initData() {
        if (checkPermissionStorage()){
            getAllPictureFoldersAndImages()
        }
        if (intent.hasExtra("Video")){
            isVideo = intent.getBooleanExtra("Video",false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissionStorage()){
            getAllPictureFoldersAndImages()
        }else{
            requestAppPermissionStorage(REQUEST_CODE_STORAGE)
        }
        binding.apply {
            if (folders.size >= 1){
                tvTitle.text = folders[0].name
                adapter.setItems(folders[0].images)
                rcvPhoto.adapter = adapter
                rcvPhoto.layoutManager = layoutManager
            }
        }
    }

    override fun initView() {

    }

    override fun initListener() {
        binding.apply {
            Back.setOnClickListener {
                finish()
            }
            btnDone.setOnClickListener {
                if (isVideo){
                    intent = Intent(this@PhotoActivity,VideoCutterActivity::class.java)
                    intent.putExtra("PATH_VIDEO",path)
                }else{
                    intent = Intent(this@PhotoActivity,CutOutActivity::class.java)
                    intent.putExtra("PATH_IMAGE",path)
                }
                startActivity(intent)
                finish()
            }
            icDrop.setOnClickListener { setFilter() }
            tvTitle.setOnClickListener { setFilter() }
        }
    }

    private fun setFilter(){
        dialogFilter = DialogFilter(this,this,folders)
        dialogFilter.show()
    }
    override fun isPhoto(path: String) {
        this.path = path
    }

    private fun getAllPictureFoldersAndImages() {
        folders.clear()
        val pictureDirectories = listOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        )
        for (directory in pictureDirectories) {
            if (directory.exists() && directory.isDirectory) {
                val folderFiles = getFolders(directory)
                folders.addAll(folderFiles)
            }
        }
    }
    private fun getFolders(directory: File): List<FolderModel> {
        val folders = mutableListOf<FolderModel>()
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    val images = getMediaFiles(file)
                    if (images.isNotEmpty()) {
                        folders.add(FolderModel(file.name, images,false))
                    }
                }
            }
        }
        return folders
    }
    private fun getMediaFiles(directory: File): List<PhotoModel> {
        val images = mutableListOf<PhotoModel>()
        val files = directory.listFiles()

        if (files != null) {
            for (file in files) {
                if (isVideo){
                    if (file.isFile && isVideoFile(file)) {
                        val duration = getVideoDuration(file)
                        images.add(PhotoModel(file.absolutePath,false,duration))
                    }
                }else{
                    if (file.isFile && isImageFile(file)) {
                        images.add(PhotoModel(file.absolutePath,false))
                    }
                }
            }
        }
        return images
    }

    private fun getVideoDuration(file: File): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(file.absolutePath)
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationStr?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        } finally {
            retriever.release()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun isImageFile(file: File): Boolean {
        val fileName = file.name.toLowerCase()
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif") || fileName.endsWith(".bmp")
    }

    @SuppressLint("DefaultLocale")
    private fun isVideoFile(file: File): Boolean {
        val fileName = file.name.toLowerCase()
        return fileName.endsWith(".mp4") || fileName.endsWith(".avi") || fileName.endsWith(".mkv") || fileName.endsWith(".mov")
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_VIDEO)
                        && !shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)
                        && !shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                        showPermissionSettingsDialog()
                    }
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showPermissionSettingsDialog()
                    }
                }
            }
        }
    }

    override fun clickFilter(images: List<PhotoModel>, name: String) {
        adapter.setItems(images)
        binding.tvTitle.text = name
    }

}