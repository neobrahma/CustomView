package com.neobrahma.customview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.switch.AbstractSwitchView
import com.neobrahma.customview.views.switch.SwitchDoubleCanalView
import com.neobrahma.customview.views.switch.SwitchItem

class DoubleSwitchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doubleswitch)

        val items1 = listOf(
            SwitchItem(
                TypeResource.STRING,
                R.string.state_switch_on,
                R.string.content_description_canal_1_switch_on,
                R.string.switch_action_off
            ),
            SwitchItem(
                TypeResource.STRING,
                R.string.state_switch_off,
                R.string.content_description_canal_1_switch_off,
                R.string.switch_action_on
            )
        )

        val items2 = listOf(
            SwitchItem(
                TypeResource.STRING,
                R.string.state_switch_on,
                R.string.content_description_canal_2_switch_on,
                R.string.switch_action_off
            ),
            SwitchItem(
                TypeResource.STRING,
                R.string.state_switch_off,
                R.string.content_description_canal_2_switch_off,
                R.string.switch_action_on
            )
        )

        findViewById<SwitchDoubleCanalView>(R.id.switch1).apply {
            items = items1
            isChecked = true
            direction = SwitchDoubleCanalView.Position.LEFT
            listener = AbstractSwitchView.SwitchListener {
                Log.d("SwitchActivity", "onClickItem: switch1 $it")
            }
        }

        findViewById<SwitchDoubleCanalView>(R.id.switch2).apply {
            items = items2
            isChecked = false
            direction = SwitchDoubleCanalView.Position.RIGHT
            listener = AbstractSwitchView.SwitchListener {
                Log.d("SwitchActivity", "onClickItem: switch2 $it")
            }
        }
    }
}