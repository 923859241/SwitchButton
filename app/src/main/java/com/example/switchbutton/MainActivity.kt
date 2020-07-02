package com.example.switchbutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.switchbutton.button.onSwitchListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SwitchButton.setOnSwitchListener(object:
            onSwitchListener {
            override fun onSwitchChanged(isCheck: Boolean) {
                Toast.makeText(this@MainActivity,"isCheck is $isCheck",Toast.LENGTH_SHORT).show()
            }
        })
    }

}
