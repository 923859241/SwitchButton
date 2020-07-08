package com.example.switchbutton.buttonShow

import android.animation.TypeEvaluator
/**
 * 帧刷新延迟：您可以指定刷新动画帧的频率。
 * 默认设置为每10毫秒刷新一次，
 * 但是应用程序刷新帧的速度最终取决于系统整体的繁忙程度以及系统为基础计时器提供服务的速度。
*/
class PointEvaluator:TypeEvaluator<Point> {
    override fun evaluate( fraction: Float, startValue: Point?, endValue: Point): Point {
        //只需要对应的点就行
        return endValue
    }
}