package com.example.bakery_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Build : AppCompatActivity() {

    private var layers = 2
    private var choco = 1
    private var cherry = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_build)

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build()
        )

        // UI элементы
        val btnPlusLayers = findViewById<Button>(R.id.plusLayers)
        val btnMinusLayers = findViewById<Button>(R.id.minusLayers)
        val btnPlusChoco = findViewById<Button>(R.id.plusChoco)
        val btnMinusChoco = findViewById<Button>(R.id.minusChoco)
        val btnPlusCherry = findViewById<Button>(R.id.plusCherry)
        val btnMinusCherry = findViewById<Button>(R.id.minusCherry)
        val btnCreate = findViewById<Button>(R.id.createButton)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnUndo = findViewById<Button>(R.id.btnUndo)
        val txtLayers = findViewById<TextView>(R.id.layersNum)
        val txtChoco = findViewById<TextView>(R.id.chocoNum)
        val txtCherry = findViewById<TextView>(R.id.cherryNum)
        val drawingView = findViewById<DrawingView>(R.id.drawingView)

        fun updateUI() {
            txtLayers.text = layers.toString()
            txtChoco.text = choco.toString()
            txtCherry.text = cherry.toString()
        }

        updateUI()

        // Слушатели
        btnPlusLayers.setOnClickListener {
            if (layers < 6) {
                layers++
                choco = layers - 1
                cherry = 0
                updateUI()
            }
        }
        btnMinusLayers.setOnClickListener {
            if (layers > 2) {
                layers--
                choco = layers - 1
                cherry = 0
                updateUI()
            }
        }
        btnPlusChoco.setOnClickListener {
            if (choco < layers - 1) {
                choco++
                cherry = layers - choco - 1
                updateUI()
            }
        }
        btnMinusChoco.setOnClickListener {
            if (choco > 0) {
                choco--
                cherry = layers - choco - 1
                updateUI()
            }
        }
        btnPlusCherry.setOnClickListener {
            if (cherry < layers - 1) {
                cherry++
                choco = layers - cherry - 1
                updateUI()
            }
        }
        btnMinusCherry.setOnClickListener {
            if (cherry > 0) {
                cherry--
                choco = layers - cherry - 1
                updateUI()
            }
        }

        btnClear.setOnClickListener {
            drawingView.clear()
            Toast.makeText(this, "Очищено", Toast.LENGTH_SHORT).show()
        }

        btnUndo.setOnClickListener {
            if (drawingView.undo()) {
                Toast.makeText(this, "Отменено", Toast.LENGTH_SHORT).show()
            }
        }

        btnCreate.setOnClickListener {
            val points = drawingView.getPoints()
            if (points.size < 3) {
                Toast.makeText(this, "Нарисуйте узор (минимум 3 точки)!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Упрощение + ОКРУГЛЕНИЕ до целых!
            val simplified = simplifyPath(points, epsilon = 3.0f)
            if (simplified.size < 2) {
                Toast.makeText(this, "Узор слишком простой", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ОКРУГЛЕНИЕ: x и y → Int
            val roundedPoints = simplified.map {
                "${it.x.toInt()},${it.y.toInt()}"
            }

            // Формат: layers|choco|cherry|x1,y1;x2,y2;...
            val pointsStr = roundedPoints.joinToString(";")
            val command = "$layers|$choco|$cherry|$pointsStr"

            // Отладка: вывод в лог
            println("Отправляю: $command")

            Thread {
                try {
                    val socket = DatagramSocket()
                    val sendData = command.toByteArray(Charsets.UTF_8)
                    val packet = DatagramPacket(
                        sendData,
                        sendData.size,
                        InetAddress.getByName("192.168.4.1"),
                        80
                    )
                    socket.send(packet)
                    socket.close()

                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Заказ отправлен!\nТочек: ${roundedPoints.size}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Ошибка:\n${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        }
    }

    // Алгоритм Дугласа-Пекера (без изменений)
    private fun simplifyPath(points: List<android.graphics.PointF>, epsilon: Float): List<android.graphics.PointF> {
        if (points.size <= 2) return points

        val first = points.first()
        val last = points.last()
        var farthestIndex = -1
        var maxDist = 0f

        for (i in 1 until points.lastIndex) {
            val dist = perpendicularDistance(points[i], first, last)
            if (dist > maxDist) {
                maxDist = dist
                farthestIndex = i
            }
        }

        return if (maxDist > epsilon) {
            val rec1 = simplifyPath(points.subList(0, farthestIndex + 1), epsilon)
            val rec2 = simplifyPath(points.subList(farthestIndex, points.size), epsilon)
            rec1.dropLast(1) + rec2
        } else {
            listOf(first, last)
        }
    }

    private fun perpendicularDistance(p: android.graphics.PointF, start: android.graphics.PointF, end: android.graphics.PointF): Float {
        val dx = end.x - start.x
        val dy = end.y - start.y
        val lenSq = dx * dx + dy * dy
        if (lenSq == 0f) return distance(p, start)

        val t = ((p.x - start.x) * dx + (p.y - start.y) * dy) / lenSq
        val projX = start.x + t * dx
        val projY = start.y + t * dy
        return distance(android.graphics.PointF(projX, projY), p)
    }

    private fun distance(p1: android.graphics.PointF, p2: android.graphics.PointF): Float {
        return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
    }

    fun onClickHome(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}