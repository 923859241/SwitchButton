package com.example.switchbutton

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.switchbutton.button.onSwitchListener
import kotlinx.android.synthetic.main.activity_main.*
import android.animation.ObjectAnimator.ofFloat
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.switchbutton.button.SwitchButtonView2
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


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
                Toast.makeText(this@MainActivity,"isCheck is $isCheck",Toast.LENGTH_SHORT).show()
             }

            override fun onRotate(rotateTime: Float) {
/*                newButton.scrollBy(0,30)
                newButton.postInvalidate()*/
                var animator: ObjectAnimator = if(rotateTime>0){
                    ofFloat(newButton, "rotation",0F,360F)
                }else{
                    ofFloat(newButton, "rotation",0F,-360F)
                }
                animator.duration = abs(rotateTime).toLong()
                animator.repeatCount = ValueAnimator.INFINITE
                //插值器为线性
                animator.interpolator = LinearInterpolator()
                animator.start()

            }
            })
        layoutContainer.addView(newButton)
    }
/*    *//**
     * 按钮二的点击事件
     *//*
    private fun addviewTwo() {
        val textView = TextView(this)
        //获取当前时间并格式化
        textView.text = "测试二..."
        textView.textSize = 20f
        layoutContainer.addView(textView)
    }*/




}
