package com.example.bakery_v2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.min

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
        // 💡 Исправление 1: безопасный outlineProvider (без minOf!)
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                // Защита от width=0
                if (view.width <= 0 || view.height <= 0) {
                    outline.setRect(0, 0, 0, 0)
                    return
                }
                // Используем min() вместо minOf()
                val size = min(view.width, view.height).toFloat()
                outline.setOval(0f, 0f, size, size)
            }
        }
        clipToOutline = true
        // 💡 Исправление 2: убрать setLayerType — он может ломать touch на некоторых устройствах
        // setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    fun clear() {
        pathHistory.add(Path(path))
        pointsHistory.add(points.toList())
        path.reset()
        points.clear()
        invalidate()
    }

    fun getPoints(): List<PointF> = points.toList()

    fun undo(): Boolean {
        return if (pathHistory.isNotEmpty()) {
            path.set(pathHistory.removeLast())
            points.clear()
            points.addAll(pointsHistory.removeLast())
            invalidate()
            true
        } else {
            false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 💡 Исправление 3: используем getPointerCoords() для надёжности
        val x = event.x
        val y = event.y

        // Защита от инициализации
        if (width == 0 || height == 0) return true

        // Центр и радиус с отступом
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(width, height) / 2f * 0.92f

        // 💡 Исправление 4: НЕ отбрасываем события за кругом!
        // Вместо этого — проецируем точку НА окружность
        val dx = x - cx
        val dy = y - cy
        val distance = sqrt(dx * dx + dy * dy)

        val targetX: Float
        val targetY: Float

        if (distance > radius && distance > 0) {
            // Точка за кругом → проецируем на границу
            val ratio = radius / distance
            targetX = cx + dx * ratio
            targetY = cy + dy * ratio
        } else {
            // Внутри круга — оставляем как есть
            targetX = x
            targetY = y
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(targetX, targetY)
                currentX = targetX
                currentY = targetY
                points.add(PointF(targetX, targetY))
                isDrawing = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
                    // 💡 Исправление 5: убрать фильтр — рисуем ВСЕГДА при движении
                    path.lineTo(targetX, targetY)
                    currentX = targetX
                    currentY = targetY
                    points.add(PointF(targetX, targetY))
                    invalidate()
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
        canvas.drawColor(Color.WHITE)
        canvas.drawPath(path, paint)
    }
}