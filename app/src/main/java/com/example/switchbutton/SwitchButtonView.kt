package com.example.switchbutton

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Switch


/**
 * Created by chenjunyu
 */
class SwitchButtonView@JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0) : View(context,attrs,defStyleAttr) {
    private lateinit var mPaint : Paint
    //背景
    private lateinit var bitmapBackGround: Bitmap
    //小球
    private lateinit var bitmapBall: Bitmap
    //底部
    private lateinit var bitmapBottom: Bitmap
    //黑色
    private lateinit var bitmapBlack: Bitmap
    //取重叠部分
    private lateinit var pdf : PorterDuffXfermode
    //开关状态
    private var isChecked : Boolean = false
    //触摸X坐标
    private var touchX:Int = 0
    //小球X坐标
    private var ballX:Int = 0

    //小球的两个状态
    private val LEFT_MOST:Int = 0
    private val RIGHT_MOST:Int = 1
    //小球运动状态
    private var ballMoveState = LEFT_MOST

    //图层标识
    private var saveFlags = 0
    //switch的宽度
    private var switchWidth = 0
    //switch的高度
    private var switchHeight = 0

    //手指按下
    private val TOUCH_STATE_DOWN = 2
    //手指移动
    private val TOUCH_STATE_MOVE = 3
    //手指抬起
    private val TOUCH_STATE_UP = 4

    private lateinit var mListener: onSwitchListener

    //初始化
    init{
        val ta:TypedArray  = context.obtainStyledAttributes(attrs, R.styleable.SwitchButtonView);
        isChecked = ta.getBoolean(R.styleable.SwitchButtonView_checked, false);
        ta.recycle()
        viewInit(context)
    }


    /**
     * 视图、数据初始化
     *
     * @param context
     */
    private fun viewInit(context:Context)
    {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        pdf = PorterDuffXfermode (PorterDuff.Mode.SRC_IN); //2张重叠 取上面一张重叠部分

        saveFlags = Canvas.ALL_SAVE_FLAG

        bitmapBackGround = BitmapFactory.decodeResource(resources, R.mipmap.switch_bg)
        bitmapBall = BitmapFactory.decodeResource(resources, R.mipmap.switch_ball)
        bitmapBottom = BitmapFactory.decodeResource(resources, R.mipmap.switch_bottom)
        bitmapBlack = BitmapFactory.decodeResource(resources, R.mipmap.switch_black)
        //获取图片大小
        switchWidth = bitmapBackGround.width
        switchHeight = bitmapBackGround.height
        //是否为开
        if (isChecked) {
            ballMoveState = RIGHT_MOST
            ballX = bitmapBackGround.width - bitmapBall.width
        }
    }

    //度量框的大小
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(switchWidth, switchHeight)
    }

    //在touch事件中不断更新onDrow中改变的部分
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //增加图层
        canvas?.saveLayer(0F, 0F, switchWidth.toFloat(), switchHeight.toFloat(), null, saveFlags)
        //底部是黑色图层
        canvas?.drawBitmap(bitmapBlack, 0F, 0F, mPaint)
        mPaint.xfermode = pdf;
        if (isChecked) {
            canvas?.drawBitmap(
                bitmapBottom,
                0F - (switchWidth - bitmapBall.width - ballX),
                0F,
                mPaint
            )
        } else {
            canvas?.drawBitmap(
                bitmapBottom,
                -(bitmapBottom.width / 2 - bitmapBall.width / 2).toFloat() + ballX,
                0F,
                mPaint
            )
        }
        mPaint.xfermode = null
        canvas?.restore()
        //判断小球状态与决定其在画板上的位置
        ballMoveState(canvas)
    }


    /**
     * 滑动状态绘制
     *
     * @param canvas
     */
    private fun ballMoveState(canvas:Canvas?)
    {
        //判断当前小球的状态，从而在画板上决定小球的显示
        when(ballMoveState) {
            TOUCH_STATE_DOWN -> null
            TOUCH_STATE_MOVE -> {
                if (touchX > 0 && touchX < switchWidth - bitmapBall.width) {
                    canvas?.drawBitmap(bitmapBall, touchX.toFloat(), 0F, mPaint)
                } else if (touchX <= 0) {
                    canvas?.drawBitmap(bitmapBall, 0F, 0F, mPaint);
                } else if (touchX >= switchWidth - bitmapBall.width) {
                    canvas?.drawBitmap(bitmapBall, switchWidth - bitmapBall.width.toFloat(), 0F, mPaint);
                }
            }

            TOUCH_STATE_UP -> null
            //最左边 True
            LEFT_MOST -> {
                if (touchX > 0 && touchX < switchWidth / 2) {
                    canvas?.drawBitmap(bitmapBall, 0F, 0F, mPaint);
                } else if (touchX >= switchWidth / 2 && touchX <= switchWidth) {
                    canvas?.drawBitmap(bitmapBall, switchWidth - bitmapBall.width.toFloat(), 0F, mPaint)
                } else if (touchX <= 0) {
                    canvas?.drawBitmap(bitmapBall, 0F, 0F, mPaint)
                } else if (touchX >= switchWidth - bitmapBall.width) {
                    canvas?.drawBitmap(bitmapBall, switchWidth - bitmapBall.width.toFloat(), 0F, mPaint)
                }
            }
            //最右边 False
            RIGHT_MOST -> canvas?.drawBitmap(bitmapBall, switchWidth - bitmapBall.width.toFloat(), 0F, mPaint)

            else -> null
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //监控触摸事件
        when(event?.action){
            MotionEvent.ACTION_DOWN -> touchStateChange(event.x.toInt(), TOUCH_STATE_DOWN)
            MotionEvent.ACTION_MOVE -> touchStateChange(event.x.toInt(), TOUCH_STATE_MOVE)
            MotionEvent.ACTION_UP -> touchStateChange(event.x.toInt(), TOUCH_STATE_UP)
            else -> null
        }
        return true
    }

    /**
     * 触摸状态改变
     *
     * @param mTouchX
     * @param touchState
     */
    private fun touchStateChange(mTouchX: Int, touchState:Int)
    {
        //手指的位置就是小球的位置
        touchX = mTouchX
        ballX = touchX
        if (touchX <= 0) {
            ballX = 0;
        }
        if (touchX >= switchWidth - bitmapBall.width) {
            ballX = switchWidth - bitmapBall.width
        }
        ballMoveState = touchState;
        if (ballMoveState == TOUCH_STATE_UP) { //手指抬起
            ballX = 0;
            if (touchX >= switchWidth / 2f) {//最右
                isChecked = false
                ballX = switchWidth - bitmapBall.width
                ballMoveState = RIGHT_MOST
            } else {//最左
                isChecked = true
                ballMoveState = LEFT_MOST
            }
            //监听函数返回
            mListener?.onSwitchChanged(isChecked)
        }
        /*
        请求重绘View树.即draw()过程.假如视图发生大小没有变化就不会调用layout()过程，并且只绘制那些“需要重绘的”
         视图，即谁(View的话，只绘制该View.ViewGroup，则绘制整个ViewGroup)请求invalidate()方法，就绘制该视图。
        */
        invalidate()
    }

    fun setOnSwitchListener(listener: onSwitchListener) {
        mListener = listener
    }
}