package com.kan.dev.st051_stickermaker.utils.stickerview

import android.view.MotionEvent
import com.kan.dev.st051_stickermaker.interfaces.IEditStickerView
import com.kan.dev.st051_stickermaker.interfaces.StickerIconEvent

class EditIconEvent : StickerIconEvent {
    override fun onActionDown(stickerView: CustomSticker?, event: MotionEvent?) {

    }

    override fun onActionMove(stickerView: CustomSticker?, event: MotionEvent?) {

    }

    override fun onActionUp(stickerView: CustomSticker?, event: MotionEvent?) {
        stickerView!!.editCurrentSticker()
    }
}