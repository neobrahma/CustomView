package com.neobrahma.customview.views.picker

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.neobrahma.customview.R

class PickerView : ConstraintLayout {

    private val pickerColorView: PickerColorView
    private val poiView: POIView

    private val listBulbItem = mutableListOf<BulbItem>()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        pickerColorView = PickerColorView(context)
        poiView = POIView(context)

        addCustomView(poiView, R.id.poiView)
        addCustomView(pickerColorView, R.id.temperaturePickerView)

        poiView.listener = object : POIView.POIListener {
            override fun updateColor(position: Int) {
                val color = pickerColorView.getSelectedColor(position)
                poiView.updateColor(color)
            }

            override fun selectedColor(id : Int, position : Int) {
                //todo listener compose/customview
            }
        }

        pickerColorView.listener = PickerColorView.PickerColorListener {
            val POIs: MutableList<POIBulb> = mutableListOf()
            listBulbItem.forEachIndexed { index, bulbItem ->
                val color = pickerColorView.getColor(bulbItem.hue)
                val angle = pickerColorView.getAngle(bulbItem.hue)
                POIs.add(POIBulb(index, angle, color))
            }
            poiView.updatePOIBulb(POIs)
        }
    }

    private fun addCustomView(view: View, id: Int) {
        val set = ConstraintSet()
        view.apply {
            this.id = id

            addView(this, 0)
            set.clone(this@PickerView)
            set.connect(this.id, ConstraintSet.TOP, this@PickerView.id, ConstraintSet.TOP)
            set.connect(this.id, ConstraintSet.START, this@PickerView.id, ConstraintSet.START)
            set.connect(this.id, ConstraintSet.END, this@PickerView.id, ConstraintSet.END)
            set.connect(this.id, ConstraintSet.BOTTOM, this@PickerView.id, ConstraintSet.BOTTOM)
            set.applyTo(this@PickerView)
        }
    }

    fun setListBulbItem(bulbs: List<BulbItem>) {
        listBulbItem.clear()
        listBulbItem.addAll(bulbs)
    }

    data class POIBulb(
        val id: Int,
        var angle: Float,
        var color: Int,
        val centerPoint: PointF = PointF()
    )
}