package com.neobrahma.customview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.switch.AbstractSwitchView
import com.neobrahma.customview.views.switch.SwitchCircleView
import com.neobrahma.customview.views.switch.SwitchItem

class SwitchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = listOf(
            SwitchItem(
                TypeResource.STRING,
                R.string.state_switch_on,
                R.string.content_description_switch_on,
                R.string.switch_action_off

            ),
            SwitchItem(
                TypeResource.STRING,
                R.string.state_switch_off,
                R.string.content_description_switch_off,
                R.string.switch_action_on
            )
        )

        findViewById<SwitchCircleView>(R.id.view).apply {
            this.items = items
            listener = AbstractSwitchView.SwitchListener {
                Log.d("SwitchActivity", "onClickItem: $it")
            }
        }
    }
}