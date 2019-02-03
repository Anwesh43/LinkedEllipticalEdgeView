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
    val textSizeFactor : Int = 8
    val welcomeText : String = "W.E.L.C.O.M.E"
    drawColor(Color.CYAN)
    paint.color = Color.WHITE
    paint.textSize = Math.min(width, height).toFloat() / textSizeFactor
    val tw : Float = paint.measureText(welcomeText)
    drawText(welcomeText,  width / 2 - tw / 2, height / 2 - paint.textSize / 4, paint)
}

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.scaleFactor() : Float = Math.floor(this / 0.51).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * 0.05f

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