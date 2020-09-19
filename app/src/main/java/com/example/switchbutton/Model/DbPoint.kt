package com.example.switchbutton.Model

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

class DbPoint: LitePalSupport() {
    @Column(unique = true, defaultValue = "unknown")
    var pointId = 0
    var x = 0F
    var y = 0F

}