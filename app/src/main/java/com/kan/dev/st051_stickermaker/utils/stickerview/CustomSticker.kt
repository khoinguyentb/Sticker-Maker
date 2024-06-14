package com.kan.dev.st051_stickermaker.utils.stickerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat
import com.kan.dev.st051_stickermaker.R
import java.io.File
import java.util.Arrays
import java.util.Collections



@SuppressLint("CustomViewStyleable")
open class CustomSticker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    private var showIcons = true
    private var showBorder = true
    private var bringToFrontCurrentSticker = false
    private var mHandlingSticker: Sticker? = null

    @IntDef(*[ActionMode.NONE, ActionMode.DRAG, ActionMode.ZOOM_WITH_TWO_FINGER, ActionMode.ICON, ActionMode.CLICK])
    @Retention(AnnotationRetention.SOURCE)
    protected annotation class ActionMode {
        companion object {
            const val NONE = 0
            const val DRAG = 1
            const val ZOOM_WITH_TWO_FINGER = 2
            const val ICON = 3
            const val CLICK = 4
        }
    }

    @IntDef(flag = true, value = [FLIP_HORIZONTALLY, FLIP_VERTICALLY])
    @Retention(AnnotationRetention.SOURCE)
    annotation class Flip

    fun setControlItemsHidden() {
        mHandlingSticker = null
    }

    private val stickers: MutableList<Sticker?> = ArrayList<Sticker?>()
    private val icons: MutableList<BitmapStickerIcon> = ArrayList<BitmapStickerIcon>(4)
    private val borderPaint = Paint()
    private val stickerRect = RectF()
    private val sizeMatrix = Matrix()
    private val downMatrix = Matrix()
    private val moveMatrix = Matrix()

    // region storing variables
    private val bitmapPoints = FloatArray(8)
    private val bounds = FloatArray(8)
    private val point = FloatArray(2)
    private val currentCenterPoint = PointF()
    private val tmp = FloatArray(2)
    private var midPoint = PointF()

    // endregion
    private val touchSlop: Int
    private var currentIcon: BitmapStickerIcon? = null

    //the first point down position
    private var downX = 0f
    private var downY = 0f
    private var oldDistance = 0f
    private var oldRotation = 0f

    @ActionMode
    private var currentMode = ActionMode.NONE
    private var handlingSticker: Sticker? = null
    var isLocked = false
        private set
    var isConstrained = false
        private set
    var onStickerOperationListener: OnStickerOperationListener? = null
        private set
    private var lastClickTime: Long = 0
    var minClickDelayTime = DEFAULT_MIN_CLICK_DELAY_TIME
        private set

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        var a: TypedArray? = null
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.StickerView)
            showIcons = a.getBoolean(R.styleable.StickerView_showIcons, false)
            showBorder = a.getBoolean(R.styleable.StickerView_showBorder, false)
            bringToFrontCurrentSticker =
                a.getBoolean(R.styleable.StickerView_bringToFrontCurrentSticker, false)
            borderPaint.isAntiAlias = true
            borderPaint.style = Paint.Style.STROKE
            borderPaint.setPathEffect(DashPathEffect(floatArrayOf(50f, 20f), 0f))
            borderPaint.strokeWidth = 5f
            borderPaint.color = a.getColor(R.styleable.StickerView_borderColor, Color.WHITE)
            //borderPaint.setAlpha(a.getInteger(R.styleable.StickerView_borderAlpha, 128));
            configDefaultIcons()
        } finally {
            a?.recycle()
        }
    }

    fun configDefaultIcons() {
        val deleteIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_delete_sticker_view),
            BitmapStickerIcon.LEFT_TOP
        )

        val editIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_edit_text_stickerview),
            BitmapStickerIcon.RIGHT_TOP
        )
        editIcon.setIconEvent(EditIconEvent())

        deleteIcon.setIconEvent(DeleteIconEvent())
        val zoomIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_scale_sticker_view),
            BitmapStickerIcon.RIGHT_BOTOM
        )
        zoomIcon.setIconEvent(ZoomIconEvent())
        val flipIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_flip_sticker_view),
            BitmapStickerIcon.LEFT_BOTTOM
        )
        flipIcon.setIconEvent(FlipHorizontallyEvent())
        icons.clear()
        icons.add(editIcon)
        icons.add(deleteIcon)
        icons.add(zoomIcon)
        icons.add(flipIcon)
    }
    fun swapLayers(oldPos: Int, newPos: Int) {
        if (stickers.size >= oldPos && stickers.size >= newPos) {
            Collections.swap(stickers, oldPos, newPos)
            invalidate()
        }
    }
    fun sendToLayer(oldPos: Int, newPos: Int) {
        if (stickers.size >= oldPos && stickers.size >= newPos) {
            val s: Sticker? = stickers[oldPos]
            stickers.removeAt(oldPos)
            stickers.add(newPos, s)
            invalidate()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            stickerRect.left = left.toFloat()
            stickerRect.top = top.toFloat()
            stickerRect.right = right.toFloat()
            stickerRect.bottom = bottom.toFloat()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawStickers(canvas)
    }

    protected fun drawStickers(canvas: Canvas) {
        for (i in stickers.indices) {
            stickers[i]?.draw(canvas)
        }
        if (handlingSticker != null && !isLocked && (showBorder || showIcons)) {
            getStickerPoints(handlingSticker, bitmapPoints)
            val x1 = bitmapPoints[0]
            val y1 = bitmapPoints[1]
            val x2 = bitmapPoints[2]
            val y2 = bitmapPoints[3]
            val x3 = bitmapPoints[4]
            val y3 = bitmapPoints[5]
            val x4 = bitmapPoints[6]
            val y4 = bitmapPoints[7]
            if (showBorder) {
                canvas.drawLine(x1, y1, x2, y2, borderPaint)
                canvas.drawLine(x1, y1, x3, y3, borderPaint)
                canvas.drawLine(x2, y2, x4, y4, borderPaint)
                canvas.drawLine(x4, y4, x3, y3, borderPaint)
            }

            //draw icons
            if (showIcons) {
                val rotation = calculateRotation(x4, y4, x3, y3)
                for (i in icons.indices) {
                    val icon: BitmapStickerIcon = icons[i]
                    when (icon.position) {
                        BitmapStickerIcon.LEFT_TOP -> configIconMatrix(icon, x1, y1, rotation)
                        BitmapStickerIcon.RIGHT_TOP -> configIconMatrix(icon, x2, y2, rotation)
                        BitmapStickerIcon.LEFT_BOTTOM -> configIconMatrix(icon, x3, y3, rotation)
                        BitmapStickerIcon.RIGHT_BOTOM -> configIconMatrix(icon, x4, y4, rotation)
                    }
                    icon.draw(canvas, borderPaint)
                }
            }
        }
    }

    protected fun configIconMatrix(
        icon: BitmapStickerIcon, x: Float, y: Float,
        rotation: Float
    ) {
        icon.x = x
        icon.y = y
        icon.matrix.reset()
        icon.matrix.postRotate(rotation, (icon.width / 2).toFloat(), (icon.height / 2).toFloat())
        icon.matrix.postTranslate(x - icon.width / 2, y - icon.height / 2)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isLocked) return super.onInterceptTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                return findCurrentIconTouched() != null || findHandlingSticker() != null
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isLocked) {
            return super.onTouchEvent(event)
        }
        val action = MotionEventCompat.getActionMasked(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> if (!onTouchDown(event)) {
                return false
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = calculateDistance(event)
                oldRotation = calculateRotation(event)
                midPoint = calculateMidPoint(event)
                if (handlingSticker != null && isInStickerArea(
                        handlingSticker!!, event.getX(1),
                        event.getY(1)
                    ) && findCurrentIconTouched() == null
                ) {
                    currentMode = ActionMode.ZOOM_WITH_TWO_FINGER
                }
            }

            MotionEvent.ACTION_MOVE -> {
                handleCurrentMode(event)
                invalidate()
            }

            MotionEvent.ACTION_UP -> onTouchUp(event)
            MotionEvent.ACTION_POINTER_UP -> {
                if (currentMode == ActionMode.ZOOM_WITH_TWO_FINGER && handlingSticker != null) {
                    if (onStickerOperationListener != null) {
                        onStickerOperationListener!!.onStickerZoomFinished(handlingSticker!!)
                    }
                }
                currentMode = ActionMode.NONE
            }
        }
        return true
    }

    /**
     * @param event MotionEvent received from [)][.onTouchEvent]
     */
    protected fun onTouchDown(event: MotionEvent): Boolean {
        currentMode = ActionMode.DRAG
        downX = event.x
        downY = event.y
        midPoint = calculateMidPoint()
        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY)
        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY)
        currentIcon = findCurrentIconTouched()
        if (currentIcon != null) {
            currentMode = ActionMode.ICON
            currentIcon!!.onActionDown(this, event)
        } else {
            handlingSticker = findHandlingSticker()
        }
        if (handlingSticker != null) {
            downMatrix.set(handlingSticker!!.matrix)
            if (bringToFrontCurrentSticker) {
                stickers.remove(handlingSticker)
                stickers.add(handlingSticker)
            }
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerTouchedDown(handlingSticker!!)
            }
        }
        if (currentIcon == null && handlingSticker == null) {
            return false
        }
        invalidate()
        return true
    }

    protected fun onTouchUp(event: MotionEvent) {
        val currentTime = SystemClock.uptimeMillis()
        if (currentMode == ActionMode.ICON && currentIcon != null && handlingSticker != null) {
            currentIcon!!.onActionUp(this, event)
        }
        if (currentMode == ActionMode.DRAG && Math.abs(event.x - downX) < touchSlop && Math.abs(
                event.y - downY
            ) < touchSlop && handlingSticker != null
        ) {
            currentMode = ActionMode.CLICK
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerClicked(handlingSticker!!)
            }
            if (currentTime - lastClickTime < minClickDelayTime) {
                if (onStickerOperationListener != null) {
                    onStickerOperationListener!!.onStickerDoubleTapped(handlingSticker!!)
                }
            }
        }
        if (currentMode == ActionMode.DRAG && handlingSticker != null) {
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerDragFinished(handlingSticker!!)
            }
        }
        currentMode = ActionMode.NONE
        lastClickTime = currentTime
    }

    protected fun handleCurrentMode(event: MotionEvent) {
        when (currentMode) {
            ActionMode.NONE, ActionMode.CLICK -> {}
            ActionMode.DRAG -> if (handlingSticker != null) {
                moveMatrix.set(downMatrix)
                moveMatrix.postTranslate(event.x - downX, event.y - downY)
                handlingSticker!!.setMatrix(moveMatrix)
                if (isConstrained) {
                    constrainSticker(handlingSticker!!)
                }
            }

            ActionMode.ZOOM_WITH_TWO_FINGER -> if (handlingSticker != null) {
                val newDistance = calculateDistance(event)
                val newRotation = calculateRotation(event)
                moveMatrix.set(downMatrix)
                moveMatrix.postScale(
                    newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                    midPoint.y
                )
                moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
                handlingSticker!!.setMatrix(moveMatrix)
            }

            ActionMode.ICON -> if (handlingSticker != null && currentIcon != null) {
                currentIcon!!.onActionMove(this, event)
            }
        }
    }

    fun zoomAndRotateCurrentSticker(event: MotionEvent) {
        zoomAndRotateSticker(handlingSticker, event)
    }

    fun zoomAndRotateSticker(sticker: Sticker?, event: MotionEvent) {
        if (sticker != null) {
            val newDistance = calculateDistance(midPoint.x, midPoint.y, event.x, event.y)
            val newRotation = calculateRotation(midPoint.x, midPoint.y, event.x, event.y)
            moveMatrix.set(downMatrix)
            moveMatrix.postScale(
                newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                midPoint.y
            )
            moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
            handlingSticker!!.setMatrix(moveMatrix)
        }
    }

    protected fun constrainSticker(sticker: Sticker) {
        var moveX = 0f
        var moveY = 0f
        val width = width
        val height = height
        sticker.getMappedCenterPoint(currentCenterPoint, point, tmp)
        if (currentCenterPoint.x < 0) {
            moveX = -currentCenterPoint.x
        }
        if (currentCenterPoint.x > width) {
            moveX = width - currentCenterPoint.x
        }
        if (currentCenterPoint.y < 0) {
            moveY = -currentCenterPoint.y
        }
        if (currentCenterPoint.y > height) {
            moveY = height - currentCenterPoint.y
        }
        sticker.matrix.postTranslate(moveX, moveY)
    }

    protected fun findCurrentIconTouched(): BitmapStickerIcon? {
        for (icon in icons) {
            val x: Float = icon.x - downX
            val y: Float = icon.y - downY
            val distance_pow_2 = x * x + y * y
            if (distance_pow_2 <= Math.pow((icon.iconRadius + icon.iconRadius).toDouble(), 2.0)) {
                return icon
            }
        }
        return null
    }

    /**
     * find the touched Sticker
     */
    protected fun findHandlingSticker(): Sticker? {
        for (i in stickers.indices.reversed()) {
            if (isInStickerArea(stickers[i]!!, downX, downY)) {
                return stickers[i]
            }
        }
        return null
    }

    protected fun isInStickerArea(sticker: Sticker, downX: Float, downY: Float): Boolean {
        tmp[0] = downX
        tmp[1] = downY
        return sticker.contains(tmp)
    }

    protected fun calculateMidPoint(event: MotionEvent?): PointF {
        if (event == null || event.pointerCount < 2) {
            midPoint[0f] = 0f
            return midPoint
        }
        val x = (event.getX(0) + event.getX(1)) / 2
        val y = (event.getY(0) + event.getY(1)) / 2
        midPoint[x] = y
        return midPoint
    }

    protected fun calculateMidPoint(): PointF {
        if (handlingSticker == null) {
            midPoint[0f] = 0f
            return midPoint
        }
        handlingSticker!!.getMappedCenterPoint(midPoint, point, tmp)
        return midPoint
    }

    /**
     * calculate rotation in line with two fingers and x-axis
     */
    protected fun calculateRotation(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        val radians = Math.atan2(y, x)
        return Math.toDegrees(radians).toFloat()
    }

    protected fun calculateDistance(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    protected fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        return Math.sqrt(x * x + y * y).toFloat()
    }
    protected fun transformSticker(sticker: Sticker?) {
        if (sticker == null) {
            Log.e(
                TAG,
                "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null"
            )
            return
        }
        sizeMatrix.reset()
        val width = width.toFloat()
        val height = height.toFloat()
        val stickerWidth: Float = sticker.width.toFloat()
        val stickerHeight: Float = sticker.height.toFloat()
        //step 1
        val offsetX = (width - stickerWidth) / 2
        val offsetY = (height - stickerHeight) / 2
        sizeMatrix.postTranslate(offsetX, offsetY)

        //step 2
        val scaleFactor: Float
        scaleFactor = if (width < height) {
            width / stickerWidth
        } else {
            height / stickerHeight
        }
        sizeMatrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f)
        sticker.matrix.reset()
        sticker.setMatrix(sizeMatrix)
        invalidate()
    }

    fun flipCurrentSticker(direction: Int) {
        flip(handlingSticker, direction)
    }

    fun flip(sticker: Sticker?, @Flip direction: Int) {
        if (sticker != null) {
            sticker.getCenterPoint(midPoint)
            if (direction and FLIP_HORIZONTALLY > 0) {
                sticker.matrix.preScale(-1F, 1F, midPoint.x, midPoint.y)
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally)
            }
            if (direction and FLIP_VERTICALLY > 0) {
                sticker.matrix.preScale(1F, -1F, midPoint.x, midPoint.y)
                sticker.setFlippedVertically(!sticker.isFlippedVertically)
            }
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerFlipped(sticker)
            }
            invalidate()
        }
    }

    fun replace(sticker: Sticker?): Boolean {
        return replace(sticker, true)
    }

    fun replace(sticker: Sticker?, needStayState: Boolean): Boolean {
        return if (handlingSticker != null && sticker != null) {
            val width = width.toFloat()
            val height = height.toFloat()
            if (needStayState) {
                sticker.setMatrix(handlingSticker!!.matrix)
                sticker.setFlippedVertically(handlingSticker!!.isFlippedVertically)
                sticker.setFlippedHorizontally(handlingSticker!!.isFlippedHorizontally)
            } else {
                handlingSticker!!.matrix.reset()
                // reset scale, angle, and put it in center
                val offsetX: Float = (width - handlingSticker!!.width) / 2f
                val offsetY: Float = (height - handlingSticker!!.height) / 2f
                sticker.matrix.postTranslate(offsetX, offsetY)
                val scaleFactor: Float
                if (width < height) {
                    scaleFactor = width / handlingSticker!!.drawable!!.intrinsicWidth
                } else {
                    scaleFactor = height / handlingSticker!!.drawable!!.intrinsicHeight
                }
                sticker.matrix
                    .postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f)
            }
            val index = stickers.indexOf(handlingSticker)
            stickers[index] = sticker
            handlingSticker = sticker
            invalidate()
            true
        } else {
            false
        }
    }

    fun remove(sticker: Sticker?): Boolean {
        return if (stickers.contains(sticker)) {
            stickers.remove(sticker)
            if (onStickerOperationListener != null) {
                onStickerOperationListener!!.onStickerDeleted(sticker!!)
            }
            if (handlingSticker === sticker) {
                handlingSticker = null
            }
            invalidate()
            true
        } else {
            Log.d(TAG, "remove: the sticker is not in this StickerView")
            false
        }
    }

    fun editCurrentSticker(){
        if (onStickerOperationListener != null) {
            onStickerOperationListener!!.onStickerEdit()
        }
    }

    fun duplicateCurrentSticker() {
        val drawableSticker: DrawableSticker? = handlingSticker as DrawableSticker?
        val drawable: Drawable =
            drawableSticker!!.drawable?.constantState?.newDrawable()!!.mutate()
        val drawableSticker1 = DrawableSticker(drawable)
        val matrix: Matrix = drawableSticker.matrix
        drawableSticker1.setMatrix(matrix)
        drawableSticker1.matrix.postTranslate(30f, 30f)
        stickers.add(drawableSticker1)
        handlingSticker = drawableSticker1
        invalidate()
    }
    fun setSticker(drawableStk: DrawableSticker) {
        val drawableSticker: DrawableSticker? = handlingSticker as DrawableSticker?
        val drawable: Drawable = drawableStk.drawable?.constantState?.newDrawable()!!.mutate()
        val drawableSticker1 = DrawableSticker(drawable)
        val matrix: Matrix = drawableSticker!!.matrix
        drawableSticker1.setMatrix(matrix)
        stickers.add(drawableSticker1)
        stickers.remove(drawableSticker)
        handlingSticker = drawableSticker1
        invalidate()
    }

    fun removeCurrentSticker(): Boolean {
        return remove(handlingSticker)
    }

    fun removeAllStickers() {
        stickers.clear()
        if (handlingSticker != null) {
            handlingSticker!!.release()
            handlingSticker = null
        }
        invalidate()
    }

    fun hideAllButton() {
        if (handlingSticker != null) {
            //handlingSticker.release();
            handlingSticker = null
        }
        invalidate()
    }

    fun addSticker(sticker: Sticker): CustomSticker {
        return addSticker(sticker, Sticker.Position.TOP)
    }

    fun addSticker(
        sticker: Sticker,
        @Sticker.Position position: Int
    ): CustomSticker {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, position)
        } else {
            post { addStickerImmediately(sticker, position) }
        }
        return this
    }

    protected fun addStickerImmediately(sticker: Sticker, @Sticker.Position position: Int) {
        setStickerPosition(sticker, position)
        val scaleFactor: Float
        val widthScaleFactor: Float = width.toFloat() / sticker.drawable!!.intrinsicWidth
        val heightScaleFactor: Float = height.toFloat() / sticker.drawable!!.intrinsicHeight
        scaleFactor =
            if (widthScaleFactor > heightScaleFactor) heightScaleFactor else widthScaleFactor
        sticker.matrix
            .postScale(scaleFactor / 2, scaleFactor / 2, (width / 2).toFloat(),
                (height / 2).toFloat()
            )
        handlingSticker = sticker
        stickers.add(sticker)
        if (onStickerOperationListener != null) {
            onStickerOperationListener!!.onStickerAdded(sticker)
        }
        invalidate()
    }

    protected fun setStickerPosition(sticker: Sticker, @Sticker.Position position: Int) {
        val width = width.toFloat()
        val height = height.toFloat()
        var offsetX: Float = width - sticker.width
        var offsetY: Float = height - sticker.height
        if (position and Sticker.Position.TOP > 0) {
            offsetY /= 4f
        } else if (position and Sticker.Position.BOTTOM > 0) {
            offsetY *= 3f / 4f
        } else {
            offsetY /= 2f
        }
        if (position and Sticker.Position.LEFT > 0) {
            offsetX /= 4f
        } else if (position and Sticker.Position.RIGHT > 0) {
            offsetX *= 3f / 4f
        } else {
            offsetX /= 2f
        }
        sticker.matrix.postTranslate(offsetX, offsetY)
    }

    fun getStickerPoints(sticker: Sticker?): FloatArray {
        val points = FloatArray(8)
        getStickerPoints(sticker, points)
        return points
    }

    fun getStickerPoints(sticker: Sticker?, dst: FloatArray) {
        if (sticker == null) {
            Arrays.fill(dst, 0f)
            return
        }
        sticker.getBoundPoints(bounds)
        sticker.getMappedPoints(dst, bounds)
    }

    fun save(file: File) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap())
            StickerUtils.notifySystemGallery(context, file)
        } catch (ignored: IllegalArgumentException) {
            //
        } catch (ignored: IllegalStateException) {
        }
    }

    @Throws(OutOfMemoryError::class)
    fun createBitmap(): Bitmap {
        handlingSticker = null
        val bitmap = Bitmap.createBitmap(
            width,
            height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    val stickerCount: Int
        get() = stickers.size
    val isNoneSticker: Boolean
        get() = stickerCount == 0

    fun setLocked(locked: Boolean): CustomSticker {
        isLocked = locked
        invalidate()
        return this
    }

    fun setMinClickDelayTime(minClickDelayTime: Int): CustomSticker {
        this.minClickDelayTime = minClickDelayTime
        return this
    }

    fun setConstrained(constrained: Boolean): CustomSticker {
        isConstrained = constrained
        postInvalidate()
        return this
    }

    fun setOnStickerOperationListener(
        onStickerOperationListener: OnStickerOperationListener?
    ): CustomSticker {
        this.onStickerOperationListener = onStickerOperationListener
        return this
    }

    val currentSticker: Sticker?
        get() = handlingSticker

    fun getIcons(): List<BitmapStickerIcon> {
        return icons
    }

    fun setIcons(icons: List<BitmapStickerIcon>) {
        this.icons.clear()
        this.icons.addAll(icons)
        invalidate()
    }

    interface OnStickerOperationListener {
        fun onStickerAdded(sticker: Sticker)
        fun onStickerClicked(sticker: Sticker)
        fun onStickerDeleted(sticker: Sticker)
        fun onStickerDragFinished(sticker: Sticker)
        fun onStickerTouchedDown(sticker: Sticker)
        fun onStickerZoomFinished(sticker: Sticker)
        fun onStickerFlipped(sticker: Sticker)
        fun onStickerDoubleTapped(sticker: Sticker)

        fun onStickerEdit()
    }

    fun hideIcons(isHidden: Boolean) {
        if (isHidden) {
            showBorder = false
            showIcons = false
        } else {
            showBorder = true
            showIcons = true
        }
    }

    companion object {
        private const val TAG = "StickerView"
        private const val DEFAULT_MIN_CLICK_DELAY_TIME = 200
        const val FLIP_HORIZONTALLY = 1
        const val FLIP_VERTICALLY = 1 shl 1
    }
}