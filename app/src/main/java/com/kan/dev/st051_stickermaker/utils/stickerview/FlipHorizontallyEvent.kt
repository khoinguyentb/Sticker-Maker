package com.kan.dev.st051_stickermaker.utils.stickerview


open class FlipHorizontallyEvent : AbstractFlipEvent() {
    @get:CustomSticker.Flip
    override val flipDirection: Int
        get() = CustomSticker.FLIP_HORIZONTALLY
}