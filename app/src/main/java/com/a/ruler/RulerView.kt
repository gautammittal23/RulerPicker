package com.a.ruler

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Nullable

class RulerView : View {
    /**
     * Height of the view. This view height is measured in [.onMeasure].
     *
     * @see .onMeasure
     */
    private var mViewHeight = 0
    /**
     * [Paint] for the line in the ruler view.
     *
     * @see .refreshPaint
     */
    private var mIndicatorPaint: Paint? = null
    /**
     * [Paint] to display the text on the ruler view.
     *
     * @see .refreshPaint
     */
    private var mTextPaint: Paint? = null
    /**
     * @return Get distance between two indicator in pixels.
     * @see .setIndicatorIntervalDistance
     */
    /**
     * Distance interval between two subsequent indicators on the ruler.
     *
     * @see .setIndicatorIntervalDistance
     * @see .getIndicatorIntervalWidth
     */
    @get:CheckResult
    var indicatorIntervalWidth = 14 /* Default value */
        private set
    /**
     * @return Get the minimum value displayed on the ruler.
     * @see .setValueRange
     */
    /**
     * Minimum value. This value will be displayed at the left-most end of the ruler. This value
     * must be less than [.mMaxValue].
     *
     * @see .setValueRange
     * @see .getMinValue
     */
    @get:CheckResult
    var minValue = 0 /* Default value */
        private set
    /**
     * @return Get the maximum value displayed on the ruler.
     * @see .setValueRange
     */
    /**
     * Maximum value. This value will be displayed at the right-most end of the ruler. This value
     * must be greater than [.mMinValue].
     *
     * @see .setValueRange
     * @see .getMaxValue
     */
    @get:CheckResult
    var maxValue = 100 /* Default maximum value */
        private set
    /**
     * @return Ratio of long indicator height to the ruler height.
     * @see .setIndicatorHeight
     */
    /**
     * Ratio of long indicator height to the ruler height. This value must be between 0 to 1. The
     * value should greater than [.mShortIndicatorHeight]. Default value is 0.6 (i.e. 60%).
     * If the value is 0, indicator won't be displayed. If the value is 1, indicator height will be
     * same as the ruler height.
     *
     * @see .setIndicatorHeight
     * @see .getLongIndicatorHeightRatio
     */
    @get:CheckResult
    var longIndicatorHeightRatio = 0.6f /* Default value */
        private set
    /**
     * @return Ratio of short indicator height to the ruler height.
     * @see .setIndicatorHeight
     */
    /**
     * Ratio of short indicator height to the ruler height. This value must be between 0 to 1. The
     * value should less than [.mLongIndicatorHeight]. Default value is 0.4 (i.e. 40%).
     * If the value is 0, indicator won't be displayed. If the value is 1, indicator height will be
     * same as the ruler height.
     *
     * @see .setIndicatorHeight
     * @see .getShortIndicatorHeightRatio
     */
    @get:CheckResult
    var shortIndicatorHeightRatio = 0.4f /* Default value */
        private set
    /**
     * Actual height of the long indicator in pixels. This height is derived from
     * [.mLongIndicatorHeightRatio].
     *
     * @see .updateIndicatorHeight
     */
    private var mLongIndicatorHeight = 0
    /**
     * Actual height of the short indicator in pixels. This height is derived from
     * [.mShortIndicatorHeightRatio].
     *
     * @see .updateIndicatorHeight
     */
    private var mShortIndicatorHeight = 0
    /**
     * Integer color of the text, that is displayed on the ruler.
     *
     * @see .setTextColor
     * @see .getTextColor
     */
    @ColorInt
    private var mTextColor = Color.WHITE
    /**
     * Integer color of the indicators.
     *
     * @see .setIndicatorColor
     * @see .getIndicatorColor
     */
    @ColorInt
    private var mIndicatorColor = Color.WHITE
    /**
     * Height of the text, that is displayed on ruler in pixels.
     *
     * @see .setTextSize
     * @see .getTextSize
     */
    @Dimension
    private var mTextSize = 36
    /**
     * @return Width of the indicator in pixels.
     * @see .setIndicatorWidth
     */
    /**
     * Width of the indicator in pixels.
     *
     * @see .setIndicatorWidth
     * @see .getIndicatorWidth
     */
    @get:CheckResult
    @Dimension
    var indicatorWidth = 4f
        private set

    constructor(context: Context) : super(context) {
        parseAttr(null)
    }

    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?
    ) : super(context, attrs) {
        parseAttr(attrs)
    }

    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        parseAttr(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        parseAttr(attrs)
    }

    private fun parseAttr(@Nullable attributeSet: AttributeSet?) {
        if (attributeSet != null) {
            val a = context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.RulerView,
                0,
                0
            )
            try { //Parse params
                if (a.hasValue(R.styleable.RulerView_ruler_text_color)) {
                    mTextColor = a.getColor(
                        R.styleable.RulerView_ruler_text_color,
                        Color.WHITE
                    )
                }
                if (a.hasValue(R.styleable.RulerView_ruler_text_size)) {
                    mTextSize =
                        a.getDimensionPixelSize(R.styleable.RulerView_ruler_text_size, 14)
                }
                if (a.hasValue(R.styleable.RulerView_indicator_color)) {
                    mIndicatorColor = a.getColor(
                        R.styleable.RulerView_indicator_color,
                        Color.WHITE
                    )
                }
                if (a.hasValue(R.styleable.RulerView_indicator_width)) {
                    indicatorWidth = a.getDimensionPixelSize(
                        R.styleable.RulerView_indicator_width,
                        4
                    ).toFloat()
                }
                if (a.hasValue(R.styleable.RulerView_indicator_interval)) {
                    indicatorIntervalWidth = a.getDimensionPixelSize(
                        R.styleable.RulerView_indicator_interval,
                        4
                    )
                }
                if (a.hasValue(R.styleable.RulerView_long_height_height_ratio)) {
                    longIndicatorHeightRatio = a.getFraction(
                        R.styleable.RulerView_long_height_height_ratio,
                        1, 1, 0.6f
                    )
                }
                if (a.hasValue(R.styleable.RulerView_short_height_height_ratio)) {
                    shortIndicatorHeightRatio = a.getFraction(
                        R.styleable.RulerView_short_height_height_ratio,
                        1, 1, 0.4f
                    )
                }
                setIndicatorHeight(longIndicatorHeightRatio, shortIndicatorHeightRatio)
                if (a.hasValue(R.styleable.RulerView_min_value)) {
                    minValue = a.getInteger(R.styleable.RulerView_min_value, 0)
                }
                if (a.hasValue(R.styleable.RulerView_max_value)) {
                    maxValue = a.getInteger(R.styleable.RulerView_max_value, 100)
                }
                setValueRange(minValue, maxValue)
            } finally {
                a.recycle()
            }
        }
        refreshPaint()
    }

    /**
     * Create the indicator paint and value text color.
     */
    private fun refreshPaint() {
        mIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mIndicatorPaint!!.color = mIndicatorColor
        mIndicatorPaint!!.strokeWidth = indicatorWidth
        mIndicatorPaint!!.style = Paint.Style.STROKE
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.color = mTextColor
        mTextPaint!!.textSize = mTextSize.toFloat()
        mTextPaint!!.textAlign = Paint.Align.CENTER
        invalidate()
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) { //Iterate through all value
        for (value in 1 until maxValue - minValue) {
            if (value % 5 == 0) {
                drawLongIndicator(canvas, value)
                drawValueText(canvas, value)
            } else {
                drawSmallIndicator(canvas, value)
            }
        }
        //Draw the first indicator.
        drawSmallIndicator(canvas, 0)
        //Draw the last indicator.
        drawSmallIndicator(canvas, width)
        super.onDraw(canvas)
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) { //Measure dimensions
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec)
        val viewWidth = (maxValue - minValue - 1) * indicatorIntervalWidth
        updateIndicatorHeight(longIndicatorHeightRatio, shortIndicatorHeightRatio)
        setMeasuredDimension(viewWidth, mViewHeight)
    }

    /**
     * Calculate and update the height of the long and the short indicators based on new ratios.
     *
     * @param longIndicatorHeightRatio  Ratio of long indicator height to the ruler height.
     * @param shortIndicatorHeightRatio Ratio of short indicator height to the ruler height.
     */
    private fun updateIndicatorHeight(
        longIndicatorHeightRatio: Float,
        shortIndicatorHeightRatio: Float
    ) {
        mLongIndicatorHeight = (mViewHeight * longIndicatorHeightRatio).toInt()
        mShortIndicatorHeight = (mViewHeight * shortIndicatorHeightRatio).toInt()
    }

    /**
     * Draw the vertical short line at every value.
     *
     * @param canvas [Canvas] on which the line will be drawn.
     * @param value  Value to calculate the position of the indicator.
     */
    private fun drawSmallIndicator(
        canvas: Canvas,
        value: Int
    ) {
        canvas.drawLine(
            indicatorIntervalWidth * value.toFloat(), 0f,
            indicatorIntervalWidth * value.toFloat(),
            mShortIndicatorHeight.toFloat(),
            mIndicatorPaint!!
        )
    }

    /**
     * Draw the vertical long line.
     *
     * @param canvas [Canvas] on which the line will be drawn.
     * @param value  Value to calculate the position of the indicator.
     */
    private fun drawLongIndicator(
        canvas: Canvas,
        value: Int
    ) {
        canvas.drawLine(
            indicatorIntervalWidth * value.toFloat(), 0f,
            indicatorIntervalWidth * value.toFloat(),
            mLongIndicatorHeight.toFloat(),
            mIndicatorPaint!!
        )
    }

    /**
     * Draw the value number below the longer indicator. This will use [.mTextPaint] to draw
     * the text.
     *
     * @param canvas [Canvas] on which the text will be drawn.
     * @param value  Value to draw.
     */
    private fun drawValueText(
        canvas: Canvas,
        value: Int
    ) {
        canvas.drawText(
            (value + minValue).toString(),
            indicatorIntervalWidth * value.toFloat(),
            mLongIndicatorHeight + mTextPaint!!.textSize,
            mTextPaint!!
        )
    }
    /////////////////////// Properties getter/setter ///////////////////////
    /**
     * @return Color integer value of the ruler text color.
     * @see .setTextColor
     */
    /**
     * Set the color of the text to display on the ruler.
     *
     * @param color Color integer value.
     */
    @get:ColorInt
    @get:CheckResult
    var textColor: Int
        get() = mIndicatorColor
        set(color) {
            mTextColor = color
            refreshPaint()
        }

    /**
     * @return Size of the text of ruler in pixels.
     * @see .setTextSize
     */
    @get:CheckResult
    val textSize: Float
        get() = mTextSize.toFloat()

    /**
     * Set the size of the text to display on the ruler.
     *
     * @param textSizeSp Text size dimension in dp.
     */
    fun setTextSize(textSizeSp: Float) {
        mTextSize = RulerViewUtils.sp2px(context, textSizeSp)
        refreshPaint()
    }

    /**
     * @return Color integer value of the indicator color.
     * @see .setIndicatorColor
     */
    /**
     * Set the indicator color.
     *
     * @param color Color integer value.
     */
    @get:ColorInt
    @get:CheckResult
    var indicatorColor: Int
        get() = mIndicatorColor
        set(color) {
            mIndicatorColor = color
            refreshPaint()
        }

    /**
     * Set the width of the indicator line in the ruler.
     *
     * @param widthPx Width in pixels.
     */
    fun setIndicatorWidth(widthPx: Int) {
        indicatorWidth = widthPx.toFloat()
        refreshPaint()
    }

    /**
     * Set the maximum value to display on the ruler. This will decide the range of values and number
     * of indicators that ruler will draw.
     *
     * @param minValue Value to display at the left end of the ruler. This can be positive, negative
     * or zero. Default minimum value is 0.
     * @param maxValue Value to display at the right end of the ruler. This can be positive, negative
     * or zero.This value must be greater than min value. Default minimum value is 100.
     */
    fun setValueRange(minValue: Int, maxValue: Int) {
        this.minValue = minValue
        this.maxValue = maxValue
        invalidate()
    }

    /**
     * Set the spacing between two vertical lines/indicators. Default value is 14 pixels.
     *
     * @param indicatorIntervalPx Distance in pixels. This cannot be negative number or zero.
     * @throws IllegalArgumentException if interval is negative or zero.
     */
    fun setIndicatorIntervalDistance(indicatorIntervalPx: Int) {
        require(indicatorIntervalPx > 0) { "Interval cannot be negative or zero." }
        indicatorIntervalWidth = indicatorIntervalPx
        invalidate()
    }

    /**
     * Set the height of the long and short indicators.
     *
     * @param longHeightRatio  Ratio of long indicator height to the ruler height. This value must
     * be between 0 to 1. The value should greater than [.mShortIndicatorHeight].
     * Default value is 0.6 (i.e. 60%). If the value is 0, indicator won't
     * be displayed. If the value is 1, indicator height will be same as the
     * ruler height.
     * @param shortHeightRatio Ratio of short indicator height to the ruler height. This value must
     * be between 0 to 1. The value should less than [.mLongIndicatorHeight].
     * Default value is 0.4 (i.e. 40%). If the value is 0, indicator won't
     * be displayed. If the value is 1, indicator height will be same as
     * the ruler height.
     * @throws IllegalArgumentException if any of the parameter is invalid.
     */
    fun setIndicatorHeight(
        longHeightRatio: Float,
        shortHeightRatio: Float
    ) {
        require(!(shortHeightRatio < 0 || shortHeightRatio > 1)) { "Sort indicator height must be between 0 to 1." }
        require(!(longHeightRatio < 0 || longHeightRatio > 1)) { "Long indicator height must be between 0 to 1." }
        require(shortHeightRatio <= longHeightRatio) { "Long indicator height cannot be less than sort indicator height." }
        longIndicatorHeightRatio = longHeightRatio
        shortIndicatorHeightRatio = shortHeightRatio
        updateIndicatorHeight(longIndicatorHeightRatio, shortIndicatorHeightRatio)
        invalidate()
    }
}