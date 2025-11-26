package com.example.bakery_v2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class CircleDrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val path = Path()
    private val points = mutableListOf<PointF>()
    private val pathHistory = mutableListOf<Path>()
    private val pointsHistory = mutableListOf<List<PointF>>()
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

    private val history = mutableListOf<PathSnapshot>()

    data class PathSnapshot(
        val path: Path,
        val points: List<PointF>
    )

    // 👇 ИСПРАВЛЕНИЕ: GestureDetector для надёжного рисования
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            handleTouchEvent(e.x, e.y, MotionEvent.ACTION_DOWN)
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            handleTouchEvent(e2.x, e2.y, MotionEvent.ACTION_MOVE)
            return true
        }

        fun onUp(e: MotionEvent): Boolean {
            handleTouchEvent(e.x, e.y, MotionEvent.ACTION_UP)
            return true
        }
    })

    init {
        // Круглая форма
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                if (view.width <= 0 || view.height <= 0) {
                    outline.setRect(0, 0, 0, 0)
                    return
                }
                val size = min(view.width, view.height).toFloat()
                outline.setOval(0, 0, size.toInt(), size.toInt())
            }
        }
        clipToOutline = true

        // 👇 ИСПРАВЛЕНИЕ: отключаем long-press и захватываем жест
        isLongClickable = false
        setOnLongClickListener { true } // перехватываем и гасим
    }

    fun saveState() {
        if (history.size > 20) history.removeAt(0) // лимит истории
        history.add(PathSnapshot(Path(path), points.toList()))
    }

    fun clear() {
        saveState() // сохраняем перед очисткой
        path.reset()
        points.clear()
        invalidate()
    }

    fun getPoints(): List<PointF> = points.toList()

    fun undo(): Boolean {
        if (history.isNotEmpty()) {
            val snapshot = history.removeLast()
            path.set(snapshot.path)
            points.clear()
            points.addAll(snapshot.points)
            invalidate()
            return true
        }
        return false
    }

    private fun handleTouchEvent(x: Float, y: Float, action: Int) {
        if (width == 0 || height == 0) return

        // Проекция на круг (плавное прилипание к краю)
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(width, height) / 2f * 0.92f

        val dx = x - cx
        val dy = y - cy
        val distance = sqrt(dx * dx + dy * dy)

        val targetX = if (distance > radius && distance > 0) {
            cx + dx * (radius / distance)
        } else {
            x
        }

        val targetY = if (distance > radius && distance > 0) {
            cy + dy * (radius / distance)
        } else {
            y
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(targetX, targetY)
                currentX = targetX
                currentY = targetY
                points.add(PointF(targetX, targetY))
                isDrawing = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
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
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 👇 Говорим родителю НЕ перехватывать жест
        if (event.action == MotionEvent.ACTION_DOWN) {
            parent?.requestDisallowInterceptTouchEvent(true)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                saveState() // ← сохраняем перед новым штрихом
                // ... остальное
            }
            MotionEvent.ACTION_UP -> {
                // не обязательно, но можно для промежуточных состояний
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawPath(path, paint)
    }
}