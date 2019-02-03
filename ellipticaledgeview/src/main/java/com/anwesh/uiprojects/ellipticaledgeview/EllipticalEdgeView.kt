package com.anwesh.uiprojects.ellipticaledgeview

/**
 * Created by anweshmishra on 03/02/19.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.Path
import android.app.Activity
import android.content.Context

class EllipticalEdgeView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    companion object {

        fun create(activity : Activity) : EllipticalEdgeView {
            val view : EllipticalEdgeView = EllipticalEdgeView(activity)
            activity.setContentView(view)
            return view 
        }
    }
}