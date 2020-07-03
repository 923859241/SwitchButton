package com.example.switchbutton

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.switchbutton.button.onSwitchListener
import kotlinx.android.synthetic.main.activity_main.*
import android.animation.ObjectAnimator.ofFloat
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.switchbutton.button.SwitchButtonView2



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonAdd.setOnClickListener { addView() }
    }

    private fun addView(){
        var newButton = SwitchButtonView2(this)
        newButton.setOnSwitchListener(object:
            onSwitchListener {
            override fun onSwitchChanged(isCheck: Boolean) {
                Toast.makeText(this@MainActivity,"isCheck is $isCheck",Toast.LENGTH_SHORT).show()
            }
        })
        val animator: ObjectAnimator = ofFloat(newButton, "rotation",0F,360F)
        animator.repeatCount = ValueAnimator.INFINITE
        //animator.repeatMode = ValueAnimator.INFINITE
        animator.start()
        layoutContainer.addView(newButton)
        var textView = TextView(this)
        textView.textSize = 20F
        textView.text = "this is test"
        layoutContainer.addView( textView )

    }

}
