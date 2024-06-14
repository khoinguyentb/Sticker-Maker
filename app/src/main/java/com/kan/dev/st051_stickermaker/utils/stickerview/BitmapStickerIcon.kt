package com.kan.dev.st051_stickermaker.utils.stickerview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import androidx.annotation.IntDef
import com.kan.dev.st051_stickermaker.interfaces.StickerIconEvent


class BitmapStickerIcon(drawable: Drawable?, @Gravity gravity: Int) : DrawableSticker(drawable),
    StickerIconEvent {
    @IntDef(*[LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTOM])
    @Retention(AnnotationRetention.SOURCE)
    annotation class Gravity

    var iconRadius = DEFAULT_ICON_RADIUS
    var iconExtraRadius = DEFAULT_ICON_EXTRA_RADIUS
    var x = 0f
    var y = 0f

    @get:Gravity
    @Gravity
    var position = LEFT_TOP
    private var iconEvent: StickerIconEvent? = null

    init {
        position = gravity
    }

    fun draw(canvas: Canvas, paint: Paint?) {
        canvas.drawCircle(x, y, iconRadius, paint!!)
        super.draw(canvas)
    }

    override fun onActionDown(stickerView: CustomSticker?, event: MotionEvent?) {
        if (iconEvent != null) {
            iconEvent!!.onActionDown(stickerView, event)
        }
    }

    override fun onActionMove(stickerView: CustomSticker?, event: MotionEvent?) {
        if (iconEvent != null) {
            iconEvent!!.onActionMove(stickerView, event)
        }
    }

    override fun onActionUp(stickerView: CustomSticker?, event: MotionEvent?) {
        if (iconEvent != null) {
            iconEvent!!.onActionUp(stickerView, event)
        }
    }

    fun getIconEvent(): StickerIconEvent? {
        return iconEvent
    }

    fun setIconEvent(iconEvent: StickerIconEvent?) {
        this.iconEvent = iconEvent
    }

    companion object {
        const val DEFAULT_ICON_RADIUS = 30f
        const val DEFAULT_ICON_EXTRA_RADIUS = 10f
        const val LEFT_TOP = 0
        const val RIGHT_TOP = 1
        const val LEFT_BOTTOM = 2
        const val RIGHT_BOTOM = 3
    }
}