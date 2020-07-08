package com.example.switchbutton.button

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import com.example.switchbutton.buttonShow.Point
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatCheckBox
import kotlin.math.abs

open class SwitchButtonView2@JvmOverloads constructor(context: Context,
                                                      attrs: AttributeSet? = null,
                                                      defStyleAttr: Int = 0) : AppCompatCheckBox(context,attrs,defStyleAttr) {

    private val TAG = "SwitchButton"

    private val BUTTONID = viewCount
    /**
     * 控件默认宽度
     */
    private val DEFAULT_WIDTH = 200
    /**
     * 控件默认高度
     */
    private val DEFAULT_HEIGHT = DEFAULT_WIDTH / 8 * 5;
    /**
     * 画笔
     */
    private var mPaint = Paint()
    /**
     * 控件背景的矩形范围
     */
    private var mRectF = RectF()
    /**
     * 开关指示器按钮圆心 X 坐标的偏移量
     */
    private var mButtonCenterXOffset = 0F
    /**
     * 颜色渐变系数
     */
    private var mColorGradientFactor = 1F
    /**
     * 状态切换时的动画时长
     */
    private var mAnimateDuration = 300L
    /**
     * 开关未选中状态,即关闭状态时的背景颜色
     */
    private var mBackgroundColorUnchecked = Color.GRAY
    /**
     * 开关选中状态,即打开状态时的背景颜色
     */
    private var mBackgroundColorChecked = Color.BLUE
    /**
     * 开关指示器按钮的颜色
     */
    private var mButtonColor = 0xFFFFFFFF
    /**
     * 认为是触发移动的距离
     */
    private val MOVE_DISTANCE = 100
    /**
     * 记录开始点击的位置与时间
     */
    private var starPointX = 0F
    private var starPointY = 0F
    private var starTime = 0L
    /**
     * 记录是否获得初始速度与旋转速度
     */
    private var isRotate = false
    private var isMove = false
    /**
     * 抽象一个内置点 以供修改
     */
    private var absPoint = Point()

    private lateinit var mListener: onSwitchListener

    init {
        viewCount++
        // 不显示 CheckBox 默认的 Button
        buttonDrawable = null
        // 不显示 CheckBox 默认的背景
        setBackgroundResource(0)
        // 默认 CheckBox 为关闭状态
        mPaint.isAntiAlias = true
        //在控件被点击时重新绘制 UI
        setOnClickListener {
            startAnimate()
        }
    }
    companion object {
        @JvmStatic
        private var viewCount = 0
    }

    override fun onMeasure(widthMeasureSpec:Int,heightMeasureSpec:Int) {
/*      MeasureSpec.EXACTLY - 视图应该是这么多像素，无论它实际上有多大。
        MeasureSpec.AT_MOST - 如果视图的尺寸较小，则视图可以是此尺寸或更小。
        MeasureSpec.UNSPECIFIED - 视图可以是它需要的任何大小，以显示它需要显示的内容*/
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width = (paddingLeft + DEFAULT_WIDTH + paddingRight)
/*                if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            (paddingLeft + DEFAULT_WIDTH + paddingRight)
        }*/

        val height = (paddingTop + DEFAULT_HEIGHT + paddingBottom)
/*            if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            (paddingTop + DEFAULT_HEIGHT + paddingBottom)
        }*/
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 设置画笔宽度为控件宽度的 1/40,准备绘制控件背景
        mPaint.strokeWidth = (measuredWidth / 40).toFloat()
        // 根据是否选中的状态设置画笔颜色
        mPaint.color = if (isChecked) {
            // 选中状态时,背景颜色由未选中状态的背景颜色逐渐过渡到选中状态的背景颜色
            getCurrentColor(mColorGradientFactor, mBackgroundColorUnchecked, mBackgroundColorChecked)
            //0xFF6495ED.toInt()
        } else {
            // 未选中状态时,背景颜色由选中状态的背景颜色逐渐过渡到未选中状态的背景颜色
            //0xFFCCCCCC.toInt()
            getCurrentColor(mColorGradientFactor, mBackgroundColorChecked, mBackgroundColorUnchecked)
        }
        // 设置背景的矩形范围
        mRectF.set(mPaint.strokeWidth
            ,  mPaint.strokeWidth
            , measuredWidth - mPaint.strokeWidth
            , measuredHeight - mPaint.strokeWidth
        )
        // 绘制圆角矩形作为背景
        //绘画逻辑 根据mRectF生成的矩形，
        canvas?.drawRoundRect(mRectF, measuredHeight.toFloat(),
            measuredHeight.toFloat(), mPaint)

        // 设置画笔颜色,准备绘制开关按钮指示器
        mPaint.color = mButtonColor.toInt()
        /*
         * 获取开关按钮指示器的半径
         * 为了好看一点,开关按钮指示器在背景矩形中显示一点内边距,所以多减去两个画笔宽度
         */
        val radius = (measuredHeight - mPaint.strokeWidth * 4) / 2
        var x = 0F
        var y = 0F
        // 根据是否选中的状态来决定开关按钮指示器圆心的 X 坐标
        x = if (isChecked) {
            // 选中状态时开关按钮指示器圆心的 X 坐标从左边逐渐移到右边
            measuredWidth - radius - mPaint.strokeWidth - mPaint.strokeWidth - mButtonCenterXOffset
        } else {
            // 未选中状态时开关按钮指示器圆心的 X 坐标从右边逐渐移到左边
            radius + mPaint.strokeWidth + mPaint.strokeWidth + mButtonCenterXOffset
        }
        // Y 坐标就是控件高度的一半不变
        y = (measuredHeight / 2).toFloat()
        canvas?.drawCircle(x, y, radius, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                starPointX = event.x
                starPointY = event.y
                starTime = event.downTime
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                val delXY = abs(starPointY-event.y)+abs(starPointX-event.x)
                //转动与移动
                if(delXY>MOVE_DISTANCE && !isRotate && !isMove){
                    clcRotateAndMove(starPointX,starPointY,event.x,event.y,event.eventTime-starTime)
                    isRotate = true
                    isMove = true
                }else{
                    super.onTouchEvent(event)
                }
            }
            else -> super.onTouchEvent(event)
        }
        return true
    }

    /**
     * 描述:获取一个过渡期中当前颜色,fraction 为过渡系数,取值范围 0f-1f,值越接近 1,颜色就越接近 endColor
     *
     * @param fraction   当前渐变系数
     * @param startColor 过渡开始颜色
     * @param endColor   过渡结束颜色
     * @return 当前颜色
     */
    private fun getCurrentColor(
        fraction:Float,
        startColor: Int,
        endColor: Int
    ):Int {
        val redStart = Color.red(startColor)
        val blueStart = Color.blue(startColor)
        val greenStart = Color.green(startColor)
        val alphaStart = Color.alpha(startColor)

        val redEnd = Color.red(endColor)
        val blueEnd = Color.blue(endColor)
        val greenEnd = Color.green(endColor)
        val alphaEnd = Color.alpha(endColor)

        val redDifference = redEnd - redStart
        val blueDifference = blueEnd - blueStart
        val greenDifference = greenEnd - greenStart
        val alphaDifference = alphaEnd - alphaStart

        val redCurrent = (redStart + fraction * redDifference).toInt()
        val blueCurrent = (blueStart + fraction * blueDifference).toInt()
        val greenCurrent = (greenStart + fraction * greenDifference).toInt()
        val alphaCurrent = (alphaStart + fraction * alphaDifference).toInt()

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent)
    }
    /**

     * 描述:开始开关按钮切换状态和背景颜色过渡的动画
     */
    private fun startAnimate() {
        // 计算开关指示器的半径
        val radius = (measuredHeight - mPaint.strokeWidth * 4) / 2
        // 计算开关指示器的 X 坐标的总偏移量
        val centerXOffset = measuredWidth - 2*(mPaint.strokeWidth + mPaint.strokeWidth + radius)

        val animatorSet = AnimatorSet()
        // 偏移量逐渐变化到 0 ObjectAnimator动画实现
        val objectAnimator = ObjectAnimator.ofFloat(this, "buttonCenterXOffset", centerXOffset, 0F)
        objectAnimator.duration = mAnimateDuration
        objectAnimator.addUpdateListener { invalidate() }

        // 背景颜色过渡系数逐渐变化到 1
        val objectAnimator2 = ObjectAnimator.ofFloat(this, "colorGradientFactor", 0F, 1F)
        objectAnimator2.duration = mAnimateDuration

        // 同时开始修改开关指示器 X 坐标偏移量的动画和修改背景颜色过渡系数的动画
        animatorSet.play(objectAnimator).with(objectAnimator2)
        animatorSet.start()
        mListener.onSwitchChanged(isChecked)
    }

    fun setButtonCenterXOffset(buttonCenterXOffset:Float) {
        mButtonCenterXOffset = buttonCenterXOffset
    }

    fun setColorGradientFactor(colorGradientFactor:Float) {
        mColorGradientFactor = colorGradientFactor
    }
    fun setOnSwitchListener(listener: onSwitchListener) {
        mListener = listener
    }

    /**
     * 描述:计算旋转方向与速度
     *
     * @param startX  初始点x值
     * @param startY  初始点y值
     * @param endX
     * @param endY
     * @param touchTime 触摸时间
     */
    fun clcRotateAndMove(startX:Float,startY:Float,endX:Float,endY:Float,touchTime:Long){
        //x轴决定转动方向 x轴与y轴共同决定转动速度
        //防止为0
        val rotateV = (startX-DEFAULT_WIDTH/2) * (endY-startY)/(touchTime+1e-5F)
        val rotateTime = ( Math.PI*(DEFAULT_WIDTH/2)*(DEFAULT_WIDTH/2) ).toFloat() / rotateV
        val moveXV = (endX-startX)/touchTime
        val moveYV = (endY-startY)/touchTime
        mListener.onRotateAndMove(rotateTime,moveXV,moveYV)
    }

    fun setPoint(point:Point){
        absPoint = point
    }
    fun getPoint():Point{
        return absPoint
    }
    fun getID():Int{
        return BUTTONID
    }



}
