package com.example.bakery_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Build : AppCompatActivity() {

    private var layers = 2
    private var choco = 1
    private var cherry = 0
    private val uiHandler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_build)

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
        val drawingView = findViewById<CircleDrawingView>(R.id.drawingView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        fun updateUI() {
            txtLayers.text = layers.toString()
            txtChoco.text = choco.toString()
            txtCherry.text = cherry.toString()
        }
        updateUI()

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
        }

        btnUndo.setOnClickListener {
            drawingView.undo()
        }

        btnCreate.setOnClickListener {
            val points = drawingView.getPoints()
            if (points.size < 3) {
                showToast("Narisiite uzor (minimum 3 tochki)!")
                return@setOnClickListener
            }

            val epsilon = if (points.size > 150) 4.0f else 3.0f
            val simplified = simplifyPath(points, epsilon)
            if (simplified.size < 2) {
                showToast("Uzor slishkom prostoy")
                return@setOnClickListener
            }

            // Format: layers|choco|cherry|x1,y1;x2,y2;...
            // Coordinates as integers (pixels)
            val pointsStr = simplified.joinToString(";") {
                "${it.x.toInt()},${it.y.toInt()}"
            }
            val command = "$layers|$choco|$cherry|$pointsStr"

            progressBar.visibility = View.VISIBLE
            btnCreate.isEnabled = false

            Thread {
                try {
                    Socket("192.168.4.1", 80).use { socket ->
                        socket.soTimeout = 10000
                        val output = PrintWriter(socket.getOutputStream(), true)
                        val input = BufferedReader(InputStreamReader(socket.getInputStream()))

                        output.println(command)
                        val response = input.readLine() ?: "Bez otveta"

                        uiHandler.post {
                            progressBar.visibility = View.GONE
                            btnCreate.isEnabled = true
                            if (response == "RECEIVED") {
                                showToast("Accepted! Drawing cake...", Toast.LENGTH_LONG)
                            } else if (response.startsWith("OK")) {
                                showToast("Done!", Toast.LENGTH_LONG)
                            } else {
                                showToast("Error: $response", Toast.LENGTH_LONG)
                            }
                        }
                    }
                } catch (e: SocketTimeoutException) {
                    postError("Vremennoy limit: stanok ne otvetil")
                } catch (e: Exception) {
                    postError("Set: ${e.message ?: "neizvestnaya oshibka"}")
                }
            }.start()
        }
    }

    private fun simplifyPath(points: List<PointF>, epsilon: Float): List<PointF> {
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

    private fun perpendicularDistance(p: PointF, start: PointF, end: PointF): Float {
        val dx = end.x - start.x
        val dy = end.y - start.y
        val lenSq = dx * dx + dy * dy
        if (lenSq == 0f) return distance(p, start)
        val t = ((p.x - start.x) * dx + (p.y - start.y) * dy) / lenSq
        val projX = start.x + t * dx
        val projY = start.y + t * dy
        return distance(p, PointF(projX, projY))
    }

    private fun distance(p1: PointF, p2: PointF): Float {
        return sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))
    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        uiHandler.post { Toast.makeText(this, message, duration).show() }
    }

    private fun postError(message: String) {
        uiHandler.post {
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            findViewById<Button>(R.id.createButton).isEnabled = true
            showToast("Oshibka: $message", Toast.LENGTH_LONG)
        }
    }

    fun onClickHome(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}