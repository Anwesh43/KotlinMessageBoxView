package com.example.messageboxview

/**
 * Created by anweshmishra on 09/05/18.
 */

import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.*

class MessageBoxView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action == MotionEvent.ACTION_DOWN) {

        }
        return true
    }

    data class State(var j : Int = 0, var dir : Float = 0f, var prevScale : Float = 0f) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * this.dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

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

    data class MessageBox (var i : Int, val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val r : Float = Math.min(w, h)/10
            val size : Float = Math.min(w, h)/3
            canvas.save()
            canvas.translate(w/2, h/2)
            paint.color = Color.WHITE
            canvas.drawTail(-r, r, state.scales[0], paint)
            canvas.drawBox(-r, size, state.scales[1], paint)
            paint.color = Color.BLACK
            canvas.drawMultipleLines(-r - size, 0.8f * size, size, 3, state.scales[2], paint)
            paint.color = Color.WHITE
            canvas.drawCircle(0f, 0f, r, paint)
            paint.color = Color.BLACK
            canvas.drawLine(0f, -r/2 * state.scales[2], 0f, r/2 * state.scales[2], paint)
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
}

fun Canvas.drawTail(y:Float, size : Float,  scale : Float, paint : Paint) {
    save()
    translate(0f, -y * scale)
    val path : Path = Path()
    path.moveTo(0f, 0f)
    path.lineTo(-size/2, -size)
    path.lineTo(size/2, -size)
    drawPath(path, paint)
    restore()
}

fun Canvas.drawBox(y : Float, size : Float, scale : Float, paint : Paint) {
    drawRect(RectF(-size/2, y - size * scale, size/2, y), paint)
}

fun Canvas.drawMultipleLines(yStart : Float, size : Float, h : Float,  n : Int, scale : Float paint : Paint) {
    paint.color = Color.BLACK
    paint.strokeWidth = size/20
    paint.strokeCap = Paint.Cap.ROUND
    val gap : Float = h / (2 * n + 1)
    val w : Float = (size / 2 ) * scale
    save()
    translate(yStart, 0f)
    var y : Float = gap
    for (i in 1..n) {
        drawLine(-w, 0f, w, 0f, paint)
        y += gap
    }
    restore()
}