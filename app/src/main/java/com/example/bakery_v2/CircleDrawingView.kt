package com.example.bakery_v2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.math.abs
import kotlin.math.pow
import android.graphics.PointF
import kotlin.math.sqrt

class CircleDrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val path = Path()
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#2C3E50")
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 6f
    }

    private var currentX = 0f
    private var currentY = 0f
    private var isDrawing = false
    private val points = mutableListOf<PointF>()

    private val pathHistory = mutableListOf<Path>()
    private val pointsHistory = mutableListOf<List<PointF>>()

    init {
        // Делаем View круглым
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val radius = minOf(width, height) / 2f
                outline.setOval(0, 0, width, height)
            }
        }
        clipToOutline = true
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    fun clear() {
        pathHistory.add(Path(path))          // сохраняем текущее состояние
        pointsHistory.add(points.toList())
        path.reset()
        points.clear()
        invalidate()
    }

    fun getPoints(): List<PointF> = points.toList()

    fun undo(): Boolean {
        if (pathHistory.isNotEmpty()) {
            path.set(pathHistory.removeLast())
            points.clear()
            points.addAll(pointsHistory.removeLast())
            invalidate()
            return true
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        // Ограничение по кругу
        val cx = width / 2f
        val cy = height / 2f
        val r = minOf(width, height) / 2f * 0.92f  // 92% — отступ от края

        if ((x - cx).pow(2) + (y - cy).pow(2) > r.pow(2)) {
            return true  // outside circle — consume but don't draw
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                currentX = x
                currentY = y
                points.add(PointF(x, y))
                isDrawing = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
                    path.lineTo(x, y)
                    currentX = x
                    currentY = y
                    points.add(PointF(x, y))
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                isDrawing = false
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawPath(path, paint)
    }
}