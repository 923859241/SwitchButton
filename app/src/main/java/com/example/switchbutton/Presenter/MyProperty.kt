package com.example.switchbutton.Presenter

import android.util.Property
import android.widget.Button


class MyProperty(name:String):Property<Button,Point>(Point::class.java,name) {
    override fun get(p0: Button): Point {
        val point = Point()
        point.x = p0.translationX
        point.y = p0.translationY
        return point
    }

    override fun set(targetButton: Button, value: Point) {
        targetButton.translationX = targetButton.translationX+ButtonUtil.moveTimes*value.speedX
        targetButton.translationY = targetButton.translationY+ButtonUtil.moveTimes*value.speedY
        //对应点也改变
        //根据移动后的点 修改速度正负
        if (targetButton.x< 0 || targetButton.x > (ButtonUtil.mWidth-targetButton.width) ){
            value.speedX = -value.speedX
        }
        if (targetButton.y < 0 || targetButton.y > (ButtonUtil.mHeight-targetButton.width) ){
            value.speedY = -value.speedY
        }
        //修改当前点的数据
        value.x = targetButton.translationX
        value.y = targetButton.translationY
    }

}