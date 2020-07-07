package com.example.switchbutton.buttonShow

import android.animation.TypeEvaluator

class PointEvaluator:TypeEvaluator<Point> {
    override fun evaluate( fraction: Float, startValue: Point?, endValue: Point): Point {
        //只需要对应的点就行
        return endValue
    }
}