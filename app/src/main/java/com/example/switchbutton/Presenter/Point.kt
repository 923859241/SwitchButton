package com.example.switchbutton.Presenter

data class Point @JvmOverloads constructor(var x:Float = 0F,
                                           var y:Float = 0F,
                                           var speedX:Float = 0F,
                                           var speedY:Float = 0F){
    fun addPoint(otherPoint: Point){
        speedX += otherPoint.speedX
        speedY += otherPoint.speedY
        x = otherPoint.x
        y = otherPoint.y
    }
}