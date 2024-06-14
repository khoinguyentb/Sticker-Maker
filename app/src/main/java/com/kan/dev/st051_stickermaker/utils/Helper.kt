package com.kan.dev.st051_stickermaker.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import com.kan.dev.st051_stickermaker.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


const val IS_LANGUAGE = "IS_LANGUAGE"
const val SELECT_RATE = "SELECT_RATE"
const val INTERACTION = "INTERACTION"
const val LOG_APP = "LOG_APP"
const val INDEX_LOG_APP = "INDEX_LOG_APP"
const val CHECK_PERMISSION = "CHECK_PERMISSION"

const val ROUND = 1
const val BUTT = 0
const val SQUARE = 2
const val DEFAULT_VALUE = 180f
const val MAX_VALUE = 360f
const val MIN_VALUE = 0f
const val SPAN_VALUE = 0.1f
const val ITEM_MAX_HEIGHT = 36
const val ITEM_MAX_WIDTH = 3
const val ITEM_MIN_HEIGHT = 20
const val ITEM_MIN_WIDTH = 2
const val ITEM_MIDDLE_HEIGHT = 28
const val ITEM_MIDDLE_WIDTH = 2
const val INDICATOR_WIDTH = 3
const val INDICATOR_HEIGHT = 38
const val LINE = 1
const val TRIANGLE = 2
const val PUT_BITMAP = "BITMAP"

var isClick = true
val handler : Handler = Handler(Looper.getMainLooper())
var alpha = 0
var red = 0
var green = 0
var blue = 0
fun adjustAlpha(color: Int, factor: Float): Int {
    alpha = (Color.alpha(color) * factor).toInt()
    red = Color.red(color)
    green = Color.green(color)
    blue = Color.blue(color)
    return Color.argb(alpha, red, green, blue)
}

fun openPolicy(context: Context, url: String) {
    try {
        val intent = Uri.parse(url).buildUpon().build()
        context.startActivity(Intent(Intent.ACTION_VIEW, intent))
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun getUriForImage(imagePath: String): Uri {
    val imageFile = File(imagePath)
    return Uri.fromFile(imageFile)
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

fun shareApp(context: Context) {
    val pInfo: PackageInfo =
        context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
    val appPackageName = pInfo.packageName

    val appName = context.getString(R.string.app_name)
    val shareBodyText = "https://play.google.com/store/apps/details?id=$appPackageName"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, appName)
        putExtra(Intent.EXTRA_TEXT, shareBodyText)
    }
    context.startActivity(Intent.createChooser(sendIntent, null))
}

fun dpToPx(context: Context, dp: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

internal fun dp2px(dpVal: Float): Int {
    val r = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, r.displayMetrics).toInt()
}

internal fun sp2px(spVal: Float): Int {
    val r = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, r.displayMetrics).toInt()
}

fun saveBitmapToStorage(bitmap: Bitmap): String {
    val path = Environment.getExternalStorageDirectory()
        .toString() + "/${Environment.DIRECTORY_PICTURES}/"
    val fOut: OutputStream?
    val file = File(path, "CropImage${System.currentTimeMillis()}.jpg")
    fOut = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
    fOut.flush()
    fOut.close()
    return file.absolutePath
}

fun convertBitmap(view : View): Bitmap {
    val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)
    val bgDrawable = view.background
    if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.TRANSPARENT)
    view.draw(canvas)
    return returnedBitmap
}