package com.neobrahma.customview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.joypad.AbstractJoypadView
import com.neobrahma.customview.views.joypad.JoypadCircleView
import com.neobrahma.customview.views.joypad.JoypadItem
import com.neobrahma.customview.views.switch.SwitchItem

class JoypadCircleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joypad)

        val items = listOf(
            JoypadItem(
                TypeResource.STRING,
                R.string.state_switch_on,
                R.string.joypad_action_up
            ),
            JoypadItem(
                TypeResource.DRAWABLE,
                R.drawable.ic_arrow_left,
                R.string.joypad_action_left
            ),
            JoypadItem(
                TypeResource.DRAWABLE,
                R.drawable.ic_arrow_down,
                R.string.joypad_action_down
            ),
            JoypadItem(
                TypeResource.DRAWABLE,
                R.drawable.ic_arrow_right,
                R.string.joypad_action_right
            )
        )

        findViewById<JoypadCircleView>(R.id.view).apply {
            this.items = items
            listener = AbstractJoypadView.JoypadListener {
                Log.e("JoypadCircleActivity", "on selected position : $it")
            }
        }
    }
}