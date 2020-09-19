package com.example.switchbutton.Presenter

import android.content.Context
import android.preference.PreferenceManager
import com.example.switchbutton.Model.DbPoint
import com.example.switchbutton.View.SwitchButtonView2
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.findAll

class SwitchButtonPresenter() {
    fun pRotateAndMove(newButton: SwitchButtonView2, rotateTime: Float, speedX: Float, speedY: Float){
        ButtonUtil.rotateAndMove(newButton,rotateTime, speedX, speedY)
    }

    /**
     * 描述:把数据保存
     *
     */
    fun saveData(buttonMap:HashMap<Int,SwitchButtonView2>,isShowBubble:Boolean,context: Context){
        //保存map
        buttonMap.forEach{
                (key, value) ->
            val mdbPoint = DbPoint()
            mdbPoint.pointId = value.getID()
            mdbPoint.x = value.x
            mdbPoint.y = value.y
            mdbPoint.save()
        }
        //保存弹窗状态
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit() //获取编辑器
        editor.putBoolean("isShowBubble", isShowBubble)
        editor.apply() //提交修改
    }

    /**
     * 描述:读取数据
     *
     */
    fun loadData():List<DbPoint>{
        val db = LitePal.getDatabase()
        val allSwitchs = LitePal.findAll<DbPoint>()
        //清除数据库状态
        LitePal.deleteAll<DbPoint>()
        return allSwitchs
    }
    fun loadIsShowBubble(context: Context):Boolean{
        return PreferenceManager.getDefaultSharedPreferences(context).
            getBoolean("isShowBubble",true)
        }
}