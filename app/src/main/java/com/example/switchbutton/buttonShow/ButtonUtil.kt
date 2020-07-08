package com.example.switchbutton.buttonShow

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.text.Layout
import android.view.animation.LinearInterpolator
import android.widget.Button
import com.example.switchbutton.button.SwitchButtonView2
import java.lang.Exception
import java.util.concurrent.Executors
import kotlin.math.abs


object ButtonUtil {
    /**
     * 屏幕高度与宽度
     */
    var mWidth = 0
    var mHeight = 0

    /**
     * 旋转与移动的倍速
     */
    val rotateTimes = 1
    val moveTimes = 10

    /**
     * 描述:用于实际操作控件的移动与旋转
     *
     * @param newButton 目标按键
     * @param rotateTime 旋转一圈所需时间
     * @param speedX x轴速度
     * @param speedY y轴速度
     */
    fun RotateAndMove(newButton: SwitchButtonView2,rotateTime: Float, speedX: Float, speedY: Float) {
        //旋转动画
        val animatorRotate: ObjectAnimator = if (rotateTime > 0) {
            ObjectAnimator.ofFloat(newButton, "rotation", 0F, 360F)
        } else {
            ObjectAnimator.ofFloat(newButton, "rotation", 0F, -360F)
        }
        animatorRotate.duration = abs(rotateTime).toLong()/rotateTimes
        animatorRotate.repeatCount = ValueAnimator.INFINITE
        //插值器为线性
        animatorRotate.interpolator = LinearInterpolator()

        //计算当前的点
        var newPoint = newButton.getPoint()
        newPoint.addPoint(Point(newButton.x, newButton.y, speedX, speedY))
        newButton.setPoint(newPoint)
        //移动动画
        val myProperty= MyProperty("move")
        val evaluator = PointEvaluator()

        val animatorMove = ObjectAnimator.ofObject(newButton, myProperty,evaluator,newButton.getPoint())
        animatorMove.duration = abs(rotateTime).toLong()
        animatorMove.repeatCount = ValueAnimator.INFINITE
        //插值器为线性
        animatorMove.interpolator = LinearInterpolator()

        val animator = AnimatorSet()
        animator.play(animatorRotate).with(animatorMove)
        //animator.play(animatorMove)
        animator.start()
    }
}