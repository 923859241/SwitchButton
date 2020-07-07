package com.example.switchbutton.buttonShow

import android.content.Context
import android.text.Layout
import android.widget.Button
import java.lang.Exception
import java.util.concurrent.Executors


object ButtonUtil {
    /**
     * 描述:测试变量
     */
    val mWidth = 1000
    val mHeight = 2000

    /**
     * 描述:用于实际操作控件的移动
     *
     * @param targetButton 目标按键
     * @param targetPoint   目标按键抽象成的点
     */
    fun buttonMove(targetButton: Button,targetPoint: Point){
        Thread( Runnable {
            while(true){
                try {
                    targetButton.translationX = targetPoint.x + targetPoint.speedX
                    targetButton.translationY = targetPoint.y + targetPoint.speedY
                    //根据移动后的点 修改速度正负
                    if (targetButton.translationX < 0 || targetButton.translationX > mWidth){
                        targetPoint.speedX = -targetPoint.speedX
                    }
                    if (targetButton.translationY < 0 || targetButton.translationY > mHeight){
                        targetPoint.speedY = -targetPoint.speedY
                    }
                    //修改当前点的数据
                    targetPoint.x = targetButton.translationX
                    targetPoint.y = targetButton.translationY
                    Thread.sleep(1L)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }).start()
    }
}