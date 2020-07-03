package com.example.switchbutton.buttonShow

import android.content.Context
import com.example.switchbutton.button.SwitchButtonView2

/*
* 新建的带有球性质的button
*
 */
class ButtonBall@JvmOverloads constructor(context: Context):
    SwitchButtonView2(context) {
    init{
        //角速度，顺时针为正
        var mRotationalSpeed = 0F
        //圆心横坐标
        var mX = 0
        //圆心纵坐标
        var mY = 0
        //速度
        var mSpeedX = 0
        var mSpeedY = 0

        var radius = 0
        //透明度
        var alpha = 0
    }



}