package com.kan.dev.st051_stickermaker.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.kan.dev.st051_stickermaker.R


class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private val bitmaps = mutableListOf<Pair<Bitmap, PointF>>()
    private var mode = Mode.DRAW
    private var eraseRadius = 50f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (mode) {
                    Mode.DRAW -> addBitmapAtLocation(event.x, event.y)
                    Mode.ERASE -> eraseAtLocation(event.x, event.y)
                }
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                when (mode) {
                    Mode.DRAW -> addBitmapAtLocation(event.x, event.y)
                    Mode.ERASE -> eraseAtLocation(event.x, event.y)
                }
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }

    private fun addBitmapAtLocation(x: Float, y: Float) {
        val bitmap = drawableToBitmap(ContextCompat.getDrawable(context,R.drawable.effect)!!)
        bitmaps.add(Pair(bitmap, PointF(x - bitmap.width / 2, y - bitmap.height / 2)))
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            24,
            24,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun eraseAtLocation(x: Float, y: Float) {
        bitmaps.removeAll { (bitmap, point) ->
            val left = point.x
            val top = point.y
            val right = left + bitmap.width
            val bottom = top + bitmap.height
            left < x + eraseRadius && right > x - eraseRadius && top < y + eraseRadius && bottom > y - eraseRadius
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for ((bitmap, point) in bitmaps) {
            canvas.drawBitmap(bitmap, point.x, point.y, paint)
        }
    }

    fun toggleMode() {
        mode = when (mode) {
            Mode.DRAW -> Mode.ERASE
            Mode.ERASE -> Mode.DRAW
        }
    }

    private enum class Mode {
        DRAW, ERASE
    }
}