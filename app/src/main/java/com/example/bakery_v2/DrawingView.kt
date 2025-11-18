package com.example.bakery_v2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val path = Path()
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#2C3E50")  // тёмно-синий, приятный для глаз
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 6f
    }

    private var currentX = 0f
    private var currentY = 0f
    private var isDrawing = false

    // История для отмены (undo)
    private val pathHistory = mutableListOf<Path>()
    private val pointsHistory = mutableListOf<List<android.graphics.PointF>>()

    // Текущие точки
    private val points = mutableListOf<android.graphics.PointF>()

    fun clear() {
        path.reset()
        points.clear()
        pathHistory.clear()
        pointsHistory.clear()
        invalidate()
    }

    fun getPoints(): List<android.graphics.PointF> = points.toList()

    fun undo(): Boolean {
        if (pathHistory.isNotEmpty()) {
            path.set(pathHistory.removeAt(pathHistory.size - 1))
            points.clear()
            points.addAll(pointsHistory.removeAt(pointsHistory.size - 1))
            invalidate()
            return true
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Сохраняем состояние ДО начала рисования
                pathHistory.add(Path(path))
                pointsHistory.add(ArrayList(points))

                path.moveTo(x, y)
                currentX = x
                currentY = y
                points.add(android.graphics.PointF(x, y))
                isDrawing = true
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
                    // Фильтр дрожания руки: только при смещении >2px
                    if (abs(x - currentX) > 2 || abs(y - currentY) > 2) {
                        path.lineTo(x, y)
                        currentX = x
                        currentY = y
                        points.add(android.graphics.PointF(x, y))
                        invalidate()
                    }
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                isDrawing = false
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)  // чистый белый фон
        canvas.drawPath(path, paint)
    }
}