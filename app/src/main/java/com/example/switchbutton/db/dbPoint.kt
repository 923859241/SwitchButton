package com.example.switchbutton.db

import com.example.switchbutton.buttonShow.Point
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

class dbPoint: LitePalSupport() {
    @Column(unique = true, defaultValue = "unknown")
    var pointId = 0
    var x = 0F
    var y = 0F

}