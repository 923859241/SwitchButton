package com.example.switchbutton

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.switchbutton.button.onSwitchListener
import kotlinx.android.synthetic.main.activity_main.*
import android.animation.ObjectAnimator.ofFloat
import android.animation.ValueAnimator
import android.view.Gravity

import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import com.example.switchbutton.button.SwitchButtonView2
import com.example.switchbutton.buttonShow.ButtonUtil
import com.example.switchbutton.buttonShow.MyProperty
import kotlin.math.abs
import com.example.switchbutton.buttonShow.Point
import com.example.switchbutton.buttonShow.PointEvaluator


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonAdd.setOnClickListener { addView() }
    }

    private fun addView(){
        var newButton = SwitchButtonView2(this)

        newButton.setOnSwitchListener(object: onSwitchListener {
            override fun onSwitchChanged(isCheck: Boolean) {
                Toast.makeText(this@MainActivity, "isCheck is $isCheck", Toast.LENGTH_SHORT).show()
            }

            override fun onRotateAndMove(rotateTime: Float, speedX: Float, speedY: Float) {

                //旋转动画
                val animatorRotate: ObjectAnimator = if (rotateTime > 0) {
                    ofFloat(newButton, "rotation", 0F, 360F)
                } else {
                    ofFloat(newButton, "rotation", 0F, -360F)
                }
                animatorRotate.duration = abs(rotateTime).toLong()
                animatorRotate.repeatCount = ValueAnimator.INFINITE
                //插值器为线性
                animatorRotate.interpolator = LinearInterpolator()

                //移动动画
                val myProperty= MyProperty("move")
                val evaluator = PointEvaluator()
                var switchPoint = Point(newButton.x, newButton.y, speedX, speedY)
                val animatorMove = ObjectAnimator.ofObject(newButton, myProperty,evaluator,switchPoint)
                animatorMove.duration = abs(rotateTime).toLong()
                animatorMove.repeatCount = ValueAnimator.INFINITE
                //插值器为线性
                animatorMove.interpolator = LinearInterpolator()

                val animator = AnimatorSet()
                animator.play(animatorRotate).with(animatorMove)
                animator.start()
/*                var switchPoint = Point(newButton.x, newButton.y, speedX, speedY)
                ButtonUtil.buttonMove(newButton, switchPoint)*/
            }
        })
        //在中间增加控件
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        layoutParams.gravity = Gravity.CENTER
        layoutContainer.addView(newButton,layoutParams)

    }





}
