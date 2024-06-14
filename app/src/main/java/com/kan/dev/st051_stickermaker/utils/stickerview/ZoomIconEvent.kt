package com.kan.dev.st051_stickermaker.utils.stickerview

import android.view.MotionEvent
import com.kan.dev.st051_stickermaker.interfaces.StickerIconEvent


class ZoomIconEvent : StickerIconEvent {
    override fun onActionDown(stickerView: CustomSticker?, event: MotionEvent?) {}
    override fun onActionMove(stickerView: CustomSticker?, event: MotionEvent?) {
        stickerView!!.zoomAndRotateCurrentSticker(event!!)
    }

    override fun onActionUp(stickerView: CustomSticker?, event: MotionEvent?) {
        if (stickerView!!.onStickerOperationListener != null) {
            stickerView.currentSticker?.let {
                stickerView.onStickerOperationListener
                    ?.onStickerZoomFinished(it)
            }
        }
    }
}