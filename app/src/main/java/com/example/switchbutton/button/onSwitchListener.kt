package com.example.switchbutton.button

interface onSwitchListener {
    /**
     * 按键状态切换
     *
     * @param isCheck
     */
    fun onSwitchChanged(isCheck: Boolean)

    /**
     * 控制按键旋转方向与速度，默认参数顺时针为正
     * 控制按键x与y轴移动的速度，使用系统默认的xy正方向
     *
     * @param rotateTime 转动一圈用的时间
     * @param speedX x轴移动速度
     * @param speedY y轴移动速度
     */
    fun onRotateAndMove(rotateTime:Float,speedX:Float,speedY:Float)


}