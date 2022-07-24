package com.neobrahma.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.remotecontrol.AbstractRemoteControlView
import com.neobrahma.customview.views.remotecontrol.SwitchStatelessView
import com.neobrahma.customview.views.remotecontrol.RemoteControlItem

class RemoteControlCircleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remotecontrol_circle)

        val items = listOf(
            RemoteControlItem(
                TypeResource.STRING,
                R.string.state_switch_on,
                R.string.joypad_action_up
            ),
            RemoteControlItem(
                TypeResource.STRING,
                R.string.state_switch_off,
                R.string.joypad_action_left
            )
        )

        findViewById<SwitchStatelessView>(R.id.view).apply {
            this.items = items
            this.listener = AbstractRemoteControlView.RemoteControlListener {
//                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}