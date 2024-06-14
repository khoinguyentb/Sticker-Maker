package com.kan.dev.st051_stickermaker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.kan.dev.st051_stickermaker.R
import kotlin.math.abs
import kotlin.math.roundToInt


class RulerView : View {

    private var minVelocity = 0
    private var scroller: Scroller? = null
    private var velocityTracker: VelocityTracker? = null
    private var widthDefault = 0
    private var heightDefault = 0

    private var isShowResult = false

    private var isShowUnit = false

    private var enableAlpha = false

    private var strokeCap = 0

    private val itemSpacingDefault = 6
    private val marginTopText = 8
    private val textScaleSize = 15
    private val textUnitSize = 15
    private val textResultSize = 17

    private var indicatorType = 0

    private var heightResult = 0

    private var value = 0f
    private var maxValue = 0f
    private var minValue = 0f

    private var itemSpacing = 0
    private var perSpanValue = 1f
    private var maxLineHeight = 0
    private var midLineHeight = 0
    private var minLineHeight = 0

    private var minLineWidth = 0
    private var maxLineWidth = 0
    private var midLineWidth = 0

    private var marginTextTop = 0
    private var scaleTextHeight = 0f
    private var resultTextHeight = 0f
    private var unitTextHeight = 0f

    private var colorIndicator = -0xaf4a7a
    private var widthIndicator = 0
    private var heightIndicator = 0
    private var textColorScale = -0x7fddddde
    private var textSizeScale = 0
    private var textColorResult = -0xaf4a7a
    private var textSizeResult = 0
    private var unitTextColor = -0x99999a
    private var unitTextSize = 0
    private var unit: String? = null

    private var lineColorMin = -0x7fddddde
    private var lineColorMax = -0x7fddddde
    private var lineColorMid = -0x7fddddde

    private var textPaintScale: Paint? = null

    private var linePaint: Paint? = null

    private var paintIndicator: Paint? = null

    private var textPaintResult: Paint? = null

    private var textPaintUnit: Paint? = null

    private var totalLine = 0
    private var maxOffset = 0
    private var offset = 0f

    private var lastX = 0
    private var move: Int = 0

    private var listener: OnValueListener? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

//    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
//        initView(context, attrs, defStyleAttr)
//    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            widthDefault = w
            heightDefault = h
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        heightResult = if (isShowResult) (if (resultTextHeight > unitTextHeight) resultTextHeight else unitTextHeight).toInt() + 20 else widthIndicator / 2
        drawScaleAndNum(canvas)
        drawIndicator(canvas)
    }

    @SuppressLint("ClickableViewAccessibility") override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val xPosition = event.x.toInt()
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                scroller!!.forceFinished(true)
                lastX = xPosition
                move = 0
            }
            MotionEvent.ACTION_MOVE -> {
                move = lastX - xPosition
                changeMoveAndValue()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                countMoveEnd()
                countVelocityTracker()
                return false
            }
            else -> {}
        }
        lastX = xPosition
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller!!.computeScrollOffset()) {
            if (scroller!!.currX == scroller!!.finalX) {
                countMoveEnd()
            } else {
                val xPosition = scroller!!.currX
                move = lastX - xPosition
                changeMoveAndValue()
                lastX = xPosition
            }
        }
    }

    fun setChooseValueChangeListener(listener: OnValueListener?) {
        this.listener = listener
    }

    private fun initView(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray: TypedArray = getContext().theme.obtainStyledAttributes(attrs, R.styleable.RulerView, defStyleAttr, 0)
        value = typedArray.getFloat(R.styleable.RulerView_rv_defaultValue, DEFAULT_VALUE)
        maxValue = typedArray.getFloat(R.styleable.RulerView_rv_maxValue, MAX_VALUE)
        minValue = typedArray.getFloat(R.styleable.RulerView_rv_minValue, MIN_VALUE)
        val precision = typedArray.getFloat(R.styleable.RulerView_rv_spanValue, SPAN_VALUE)
        perSpanValue = precision * 10
        itemSpacing = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_itemSpacing, dp2px(itemSpacingDefault.toFloat()))
        maxLineHeight = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_maxLineHeight, dp2px(ITEM_MAX_HEIGHT.toFloat()))
        minLineHeight = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_minLineHeight, dp2px(ITEM_MIN_HEIGHT.toFloat()))
        midLineHeight = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_middleLineHeight, dp2px(ITEM_MIDDLE_HEIGHT.toFloat()))
        minLineWidth = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_minLineWidth, dp2px(ITEM_MIN_WIDTH.toFloat()))
        midLineWidth = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_middleLineWidth, dp2px(ITEM_MIDDLE_WIDTH.toFloat()))
        maxLineWidth = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_maxLineWidth, dp2px(ITEM_MAX_WIDTH.toFloat()))
        lineColorMax = typedArray.getColor(R.styleable.RulerView_rv_maxLineColor, lineColorMax)
        lineColorMid = typedArray.getColor(R.styleable.RulerView_rv_middleLineColor, lineColorMid)
        lineColorMin = typedArray.getColor(R.styleable.RulerView_rv_minLineColor, lineColorMin)
        colorIndicator = typedArray.getColor(R.styleable.RulerView_rv_indcatorColor, colorIndicator)
        widthIndicator = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_indcatorWidth, dp2px(INDICATOR_WIDTH.toFloat()))
        heightIndicator = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_indcatorHeight, dp2px(INDICATOR_HEIGHT.toFloat()))
        indicatorType = typedArray.getInt(R.styleable.RulerView_rv_indcatorType, LINE)
        textColorScale = typedArray.getColor(R.styleable.RulerView_rv_scaleTextColor, textColorScale)
        textSizeScale = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_scaleTextSize, sp2px(textScaleSize.toFloat()))
        marginTextTop = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_textMarginTop, dp2px(marginTopText.toFloat()))
        textColorResult = typedArray.getColor(R.styleable.RulerView_rv_resultTextColor, textColorResult)
        textSizeResult = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_resultTextSize, sp2px(textResultSize.toFloat()))
        unitTextColor = typedArray.getColor(R.styleable.RulerView_rv_unitTextColor, unitTextColor)
        unitTextSize = typedArray.getDimensionPixelSize(R.styleable.RulerView_rv_unitTextSize, sp2px(textUnitSize.toFloat()))
        isShowUnit = typedArray.getBoolean(R.styleable.RulerView_rv_showUnit, true)
        unit = typedArray.getString(R.styleable.RulerView_rv_unit)
        isShowResult = typedArray.getBoolean(R.styleable.RulerView_rv_showResult, true)
        enableAlpha = typedArray.getBoolean(R.styleable.RulerView_rv_alphaEnable, true)
        strokeCap = typedArray.getInt(R.styleable.RulerView_rv_strokeCap, BUTT)
        init(context)
        typedArray.recycle()
    }

    private fun init(context: Context?) {
        scroller = Scroller(context)
        minVelocity = ViewConfiguration.get(getContext()).scaledMinimumFlingVelocity
        textPaintScale = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaintScale!!.textSize = textSizeScale.toFloat()
        textPaintScale!!.color = textColorScale
        textPaintScale!!.isAntiAlias = true
        scaleTextHeight = getFontHeight(textPaintScale!!) //here
        textPaintResult = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaintResult!!.textSize = textSizeResult.toFloat()
        textPaintResult!!.color = textColorResult
        textPaintResult!!.isAntiAlias = true
        textPaintResult!!.alpha = 255
        resultTextHeight = getFontHeight(textPaintResult!!)
        if (isShowUnit) {
            textPaintUnit = Paint(Paint.ANTI_ALIAS_FLAG)
            textPaintUnit!!.textSize = unitTextSize.toFloat()
            textPaintUnit!!.color = unitTextColor
            textPaintUnit!!.isAntiAlias = true
            textPaintUnit!!.alpha = 232
            unitTextHeight = getFontHeight(textPaintUnit!!)
        }
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        when (strokeCap) {
            ROUND -> {
                linePaint!!.strokeCap = Paint.Cap.ROUND
            }
            BUTT -> {
                linePaint!!.strokeCap = Paint.Cap.BUTT
            }
            SQUARE -> {
                linePaint!!.strokeCap = Paint.Cap.SQUARE
            }
        }
        linePaint!!.isAntiAlias = true
        paintIndicator = Paint(Paint.ANTI_ALIAS_FLAG)
        paintIndicator!!.strokeWidth = widthIndicator.toFloat()
        paintIndicator!!.isAntiAlias = true
        paintIndicator!!.color = colorIndicator
        when (strokeCap) {
            ROUND -> {
                paintIndicator!!.strokeCap = Paint.Cap.ROUND
            }
            BUTT -> {
                paintIndicator!!.strokeCap = Paint.Cap.BUTT
            }
            SQUARE -> {
                paintIndicator!!.strokeCap = Paint.Cap.SQUARE
            }
        }
        initViewParam(value, minValue, maxValue, perSpanValue)
    }

    private fun initViewParam(defaultValue: Float, minValue: Float, maxValue: Float, perSpanValue: Float) {
        value = defaultValue
        this.maxValue = maxValue
        this.minValue = minValue
        this.perSpanValue = (perSpanValue * 10.0f).toInt().toFloat()
        totalLine = ((this.maxValue * 10 - this.minValue * 10).toInt() / this.perSpanValue + 1).toInt()
        maxOffset = -(totalLine - 1) * itemSpacing
        offset = (this.minValue - value) / this.perSpanValue * itemSpacing * 10
        invalidate()
        visibility = VISIBLE
    }

    private fun getFontHeight(paint: Paint): Float {
        val fm = paint.fontMetrics
        return fm.descent - fm.ascent
    }

    private fun countVelocityTracker() {
        velocityTracker!!.computeCurrentVelocity(1000)
        val xVelocity = velocityTracker!!.xVelocity
        if (abs(xVelocity) > minVelocity) {
            scroller!!.fling(0, 0, xVelocity.toInt(), 0, Int.MIN_VALUE, Int.MAX_VALUE, 0, 0)
        }
    }

    private fun countMoveEnd() {
        offset -= move.toFloat()
        if (offset <= maxOffset) {
            offset = maxOffset.toFloat()
        } else if (offset >= 0) {
            offset = 0f
        }
        lastX = 0
        move = 0
        value = (minValue + (abs(offset) * 1.0f / itemSpacing).roundToInt() * perSpanValue / 10.0f)
        offset = ((minValue - value) * 10.0f / perSpanValue * itemSpacing) // 矫正位置,保证不会停留在两个相邻刻度之间
        notifyValueChange()
        postInvalidate()
    }

    private fun drawIndicator(canvas: Canvas) {
        val srcPointX = widthDefault / 2
        if (indicatorType == LINE) {
            canvas.drawLine(srcPointX.toFloat(), heightResult.toFloat() - 20, srcPointX.toFloat(), (heightIndicator + heightResult + 20).toFloat(), paintIndicator!!)
        }
    }

    private fun drawScaleAndNum(canvas: Canvas) {
        var left: Float
        var height: Float
        var alpha: Int
        var scale: Float
        val srcPointX = widthDefault / 2
        for (i in 0 until totalLine) {
            left = srcPointX + offset + i * itemSpacing
            if (left < 0 || left > widthDefault) {
                continue
            }
            if (i % 10 == 0) {
                height = minLineHeight.toFloat()
                linePaint!!.color = lineColorMax
                linePaint!!.strokeWidth = maxLineWidth.toFloat()
                if (enableAlpha) {
                    scale = 1.3f - abs(left - srcPointX) / srcPointX
                    alpha = (255 * scale).toInt()
                    linePaint!!.alpha = alpha
                }
                canvas.drawLine(left, heightResult.toFloat() - 20, left, height + heightResult + 20, linePaint!!)
            } else {
                height = minLineHeight.toFloat()
                linePaint!!.color = lineColorMin
                linePaint!!.strokeWidth = minLineWidth.toFloat()
                if (enableAlpha) {
                    scale = 1.3f - abs(left - srcPointX) / srcPointX
                    alpha = (255 * scale).toInt()
                    linePaint!!.alpha = alpha
                }
                canvas.drawLine(left, heightResult.toFloat(), left, height + heightResult, linePaint!!)
            }
        }
    }

    private fun changeMoveAndValue() {
        offset -= move.toFloat()
        if (offset <= maxOffset) {
            offset = maxOffset.toFloat()
            move = 0
            scroller!!.forceFinished(true)
        } else if (offset >= 0) {
            offset = 0f
            move = 0
            scroller!!.forceFinished(true)
        }
        value = (minValue + (abs(offset) * 1.0f / itemSpacing).roundToInt() * perSpanValue / 10.0f)
        notifyValueChange()
        postInvalidate()
    }

    private fun notifyValueChange() {
        if (null != listener) {
            listener!!.onValueListener(value)
        }
    }
}

interface OnValueListener {
    fun onValueListener(value: Float)
}