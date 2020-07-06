package com.example.switchbutton.button

interface onSwitchListener {
    /**
     * 按键状态切换
     *
     * @param isCheck
     */
    fun onSwitchChanged(isCheck: Boolean)

    /**
     * 控制按键选择方向与速度
     *
     *
     * @param rotateTime
     * 转动一圈用的时间
     * 默认参数顺时针为正
     */
    fun onRotate(rotateTime:Float)


}