package com.kan.dev.st051_stickermaker.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kan.dev.st051_stickermaker.R


const val REQUEST_CODE_STORAGE = 1
const val REQUEST_CODE_NOTIFICATION = 2
fun Activity.requestAppPermissionNotification(requestCode: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), requestCode)
    }
}

fun Activity.checkPerList(context: Context?, listPermission: Array<String?>): Boolean {
    for (per in listPermission) {
        val allow = ActivityCompat.checkSelfPermission(
            context!!,
            per!!
        ) == PackageManager.PERMISSION_GRANTED
        if (!allow) return false
    }
    return true
}

fun Activity.requestAppPermissionStorage(requestCode: Int) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissions13 = arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES
        )
        ActivityCompat.requestPermissions(this, permissions13, requestCode)
    } else {
        val permissions =
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }
}

fun Activity.checkPermissionStorage(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }else{
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}

fun Activity.checkPermissionNotification(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.POST_NOTIFICATIONS
        )
        if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    } else {
        return true
    }
}

fun Activity.showPermissionSettingsDialog() {
    val builder = AlertDialog.Builder(this, R.style.YourCustomAlertDialogTheme)
    builder.setTitle(R.string.title_dialog_permission)
        .setMessage(R.string.content_dialog_permission)
        .setPositiveButton(R.string.setting) { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        .setCancelable(false)
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
    builder.setOnDismissListener {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
    builder.show()
}
fun Activity.showPermissionMainDialog() {
    val builder = AlertDialog.Builder(this,R.style.YourCustomAlertDialogTheme)
    builder.setMessage(getString(R.string.content_dialog_permission))
        .setTitle(getString(R.string.title_dialog_permission))
        .setCancelable(false)
        .setPositiveButton(R.string.setting) { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    builder.show()
    builder.setOnDismissListener{
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions13 = arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_IMAGES
            )
            ActivityCompat.requestPermissions(this, permissions13, REQUEST_CODE_STORAGE)
        } else {
            val permissions =
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_STORAGE)
        }
    }
}
fun Activity.showPermissionAgreeDialog() {
    val builder = AlertDialog.Builder(this,R.style.YourCustomAlertDialogTheme)
    builder.setMessage(getString(R.string.content_dialog_permission))
        .setTitle(getString(R.string.title_dialog_permission))
        .setCancelable(false)
        .setPositiveButton(R.string.Agree) { p0, _ ->
            p0.dismiss()
        }

    builder.setOnDismissListener {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    builder.show()
}

