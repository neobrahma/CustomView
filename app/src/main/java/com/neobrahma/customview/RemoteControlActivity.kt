package com.neobrahma.customview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.remotecontrol.AbstractRemoteControlView
import com.neobrahma.customview.views.remotecontrol.RemoteControlItem
import com.neobrahma.customview.views.remotecontrol.RemoteControlRectView

class RemoteControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remotecontrol_rect)

        val items = listOf(
            RemoteControlItem(
                TypeResource.DRAWABLE,
                R.drawable.ic_arrow_up,
                R.string.joypad_action_up
            ),
            RemoteControlItem(
                TypeResource.STRING,
                R.string.state_switch_off,
                R.string.joypad_action_left
            ),
            RemoteControlItem(
                TypeResource.DRAWABLE,
                R.drawable.ic_arrow_down,
                R.string.joypad_action_down
            )
        )

        findViewById<RemoteControlRectView>(R.id.view).apply {
            this.items = items
            this.columns = 1
            this.listener = AbstractRemoteControlView.RemoteControlListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}