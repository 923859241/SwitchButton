package com.example.switchbutton

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.widget.FrameLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.switchbutton.button.SwitchButtonView2
import com.example.switchbutton.button.onSwitchListener
import com.example.switchbutton.buttonShow.ButtonUtil
import com.example.switchbutton.db.dbPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.LitePal
import org.litepal.extension.findAll


class MainActivity : AppCompatActivity() {
    //保存全部点的隐射
    var buttonMap = HashMap<Int,SwitchButtonView2>()

    private var isShowBubble = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LitePal.initialize(this@MainActivity)
        setDimension()//获取屏幕大小
        buttonAdd.setOnClickListener {
            addView()
            if(isShowBubble){
                switchBubble()
                isShowBubble = false
            }
        }
        if(isShowBubble){
            buttonBubble()//气泡弹窗
        }
    }

    override fun onDestroy() {
        //保存map
        buttonMap.forEach{
                (key, value) ->
            val mdbPoint = dbPoint()
            mdbPoint.mDbpoint = value.getPoint()
            mdbPoint.pointId = value.getID()
            mdbPoint.save()
        }
        //保存弹窗状态
        val sharedPreferences =
            getSharedPreferences("isShowBubble", Context.MODE_PRIVATE) //私有数据
        val editor = sharedPreferences.edit() //获取编辑器
        editor.putBoolean("isShowBubble", isShowBubble)
        editor.apply() //提交修改

        super.onDestroy()
    }

    /**
     * 描述:在指定位置增加View
     *
     * @param leftMargin
     * @param topMargin
     */
    private fun addView(leftMargin:Int = ButtonUtil.mWidth/3,
                        topMargin:Int = ButtonUtil.mHeight/3){
        var newButton = SwitchButtonView2(this)

        newButton.setOnSwitchListener(object: onSwitchListener {
            override fun onSwitchChanged(isCheck: Boolean) {
                if(!isCheck){
                    layoutContainer.removeView(newButton)
                    buttonMap.remove(newButton.getID(),newButton)
                    Toast.makeText(this@MainActivity, "当前按钮已成功移除", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "当前状态为$isCheck", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onRotateAndMove(rotateTime: Float, speedX: Float, speedY: Float) {
                ButtonUtil.RotateAndMove(newButton,rotateTime, speedX, speedY)
            }
        })
        //在界面中间增加控件
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        layoutParams.leftMargin = leftMargin
        layoutParams.topMargin = topMargin
        layoutContainer.addView(newButton,layoutParams)

        //map记录
        buttonMap[newButton.getID()] = newButton
    }
    /**
     * 描述:用于设置屏幕宽高
     *
     */
    private fun setDimension(){
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        ButtonUtil.mHeight = dm.heightPixels
        ButtonUtil.mWidth = dm.widthPixels
    }
    /**
     * 描述:设置按钮气泡弹窗
     *
     */
    private fun buttonBubble(){
        //获取高度与宽度
        val tmpWidth: Int =ButtonUtil.mWidth / 5 * 3
        val tmpHeight: Int = ButtonUtil.mHeight / 8

        val bubbleAlert = Dialog(this,R.style.bubble_dialog)
        val bubbleView = layoutInflater.inflate(R.layout.overlay_pop, null)
        val tvKnow = bubbleView.findViewById(R.id.tvKnow) as TextView
        tvKnow.text = Html.fromHtml("<u>" + "我知道了" + "</u>")
        val tvBubContent = bubbleView.findViewById(R.id.tvBubContent) as TextView
        tvBubContent.text = "请点击按钮，增加控件！"
        tvKnow.setOnClickListener{ bubbleAlert.cancel() }

        //设置TextView宽度
        tvKnow.minWidth = tmpWidth
        tvBubContent.maxWidth = tmpWidth

        val win = bubbleAlert.window //获取所在window
        val params = win.attributes //获取LayoutParams

        params.x = -ButtonUtil.mWidth/2//设置x坐标
        params.y = 350-ButtonUtil.mHeight/2//设置y坐标

        params.width = tmpWidth
        win.attributes = params //设置生效

        bubbleAlert.setCancelable(false)
        bubbleAlert.setContentView(bubbleView)
        bubbleAlert.show()
    }

    /**
     * 描述:设置按钮气泡弹窗
     *
     */
    private fun switchBubble(){
        //获取高度与宽度
        val tmpWidth: Int =ButtonUtil.mWidth / 5 * 3
        val tmpHeight: Int = ButtonUtil.mHeight / 8

        val bubbleAlert = Dialog(this,R.style.bubble_dialog)
        val bubbleView = layoutInflater.inflate(R.layout.overlay_pop, null)
        val tvKnow = bubbleView.findViewById(R.id.tvKnow) as TextView
        tvKnow.text = Html.fromHtml("<u>" + "我知道了" + "</u>")
        val tvBubContent = bubbleView.findViewById(R.id.tvBubContent) as TextView
        tvBubContent.text = "\t1、滑动按钮，按钮会开始运动！\n\t2、第二次点击按钮，按钮会销毁！"
        tvKnow.setOnClickListener{ bubbleAlert.cancel() }

        //设置TextView宽度
        tvKnow.minWidth = tmpWidth
        tvBubContent.maxWidth = tmpWidth

        val win = bubbleAlert.window //获取所在window
        val params = win.attributes //获取LayoutParams

        params.x = 100//设置x坐标
        params.y = 150//设置y坐标

        params.width = tmpWidth
        win.attributes = params //设置生效

        bubbleAlert.setCancelable(false)
        bubbleAlert.setContentView(bubbleView)
        bubbleAlert.show()
    }

    /**
     * 描述:从数据库更新数据
     *
     */
    fun initSwitch(){
        val allSwitchs = LitePal.findAll<dbPoint>()

    }
}
