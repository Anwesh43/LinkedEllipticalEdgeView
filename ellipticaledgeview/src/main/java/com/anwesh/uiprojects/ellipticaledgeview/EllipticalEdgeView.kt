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

val scGap : Float = 0.05f
val scDiv : Double = 0.51
val nodes : Int = 5
val lines : Int = 4
val strokeFactor : Int = 90
val sizeFactor : Float = 3f
val backColor : Int = Color.parseColor("#212121")
val foreColor : Int = Color.parseColor("#EF6C00")
val aFactor : Int = 1
val bFactor : Float = 3.5f
val rotation : Float = 90f
val delay : Long = 10

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
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * scGap

fun Paint.setStyle(w : Float, h : Float) {
    color = foreColor
    strokeWidth = Math.min(w, h) / strokeFactor
    strokeCap = Paint.Cap.ROUND
    style = Paint.Style.STROKE
}

fun Canvas.drawEllipticalPath(a : Float, b : Float, scale : Float, paint : Paint) {
    val path : Path = Path()
    val deg : Int = 360
    for (j in (0..deg)) {
        val x : Float = a * Math.cos(j * Math.PI / 180).toFloat()
        val y : Float = (b * scale) * Math.sin(j * Math.PI/180).toFloat()
        if (j == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    drawPath(path, paint)
}
fun Canvas.drawEENode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    paint.setStyle(w, h)
    save()
    translate(gap * (i + 1), h / 2)
    rotate(rotation * sc2)
    for (j in 0..(lines - 1)) {
        save()
        rotate(90f * j)
        translate(0f, -size)
        drawEllipticalPath(size / aFactor, size / bFactor, sc1.divideScale(j, lines), paint)
        restore()
    }
    restore()
}

class EllipticalEdgeView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateValue(dir, lines, 1)
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class EENode(var i : Int, val state : State = State()) {

        private var next : EENode? = null
        private var prev : EENode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = EENode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawEENode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : EENode {
            var curr : EENode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class EllipticalEdge(var i : Int) {

        private val root : EENode = EENode(0)
        private var curr : EENode = root
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            root.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scl ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(i, scl)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : EllipticalEdgeView) {

        private val animator : Animator = Animator(view)
        private val ee : EllipticalEdge = EllipticalEdge(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(backColor)
            ee.draw(canvas, paint)
            animator.animate {
                ee.update {i, scl ->
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            ee.startUpdating {
                animator.start()
            }
        }
    }

}