package com.example.switchbutton.db

import com.example.switchbutton.buttonShow.Point
import org.litepal.crud.LitePalSupport

class dbPoint: LitePalSupport() {
    var mDbpoint = Point()

    fun getmDbpoint():Point{
        return mDbpoint
    }
    fun setmDbpoint(mPoint:Point){
        this.mDbpoint = mPoint
    }
}