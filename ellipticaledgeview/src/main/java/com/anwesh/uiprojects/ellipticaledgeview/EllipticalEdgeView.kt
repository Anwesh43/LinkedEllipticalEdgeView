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

fun Canvas.drawWelcomeText(paint : Paint) {
    val textSizeFactor : Int = 15
    val welcomeText : String = "W.E.L.C,O.M.E"
    drawColor(Color.CYAN)
    paint.color = Color.WHITE
    paint.textSize = Math.min(width, height).toFloat() / textSizeFactor
    val tw : Float = paint.measureText(welcomeText)
    drawText(welcomeText,  width / 2 - tw / 2, height / 2 - paint.textSize / 4, paint)
}
class EllipticalEdgeView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {
        canvas.drawWelcomeText(paint)
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