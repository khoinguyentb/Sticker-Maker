package com.kan.dev.st051_stickermaker.interfaces

import android.view.MotionEvent
import com.kan.dev.st051_stickermaker.utils.stickerview.CustomSticker

interface StickerIconEvent {
    fun onActionDown(stickerView: CustomSticker?, event: MotionEvent?)

    fun onActionMove(stickerView: CustomSticker?, event: MotionEvent?)

    fun onActionUp(stickerView: CustomSticker?, event: MotionEvent?)
}