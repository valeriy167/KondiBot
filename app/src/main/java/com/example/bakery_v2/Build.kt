package com.example.bakery_v2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Build : AppCompatActivity() {
    var layers = 1
    var choco = 0
    var cherry = 0
    var drawing = "Smile"
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_build)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val socket = DatagramSocket()

        val buttonPlusLayers: Button = findViewById(R.id.plusLayers)
        val buttonMinusLayers: Button = findViewById(R.id.minusLayers)
        val buttonPlusChoco: Button = findViewById(R.id.plusChoco)
        val buttonMinusChoco: Button = findViewById(R.id.minusChoco)
        val buttonPlusCherry: Button = findViewById(R.id.plusCherry)
        val buttonMinusCherry: Button = findViewById(R.id.minusCherry)
        val buttonDR: Button = findViewById(R.id.DR)
        val buttonLab: Button = findViewById(R.id.lab)
        val buttonSquare: Button = findViewById(R.id.square)
        val buttonSmile: Button = findViewById(R.id.smile)
        val buttonCreate: Button = findViewById(R.id.createButton)
        val numLayers: TextView = findViewById(R.id.layersNum)
        val numChoco: TextView = findViewById(R.id.chocoNum)
        val numCherry: TextView = findViewById(R.id.cherryNum)
        val drawingText: TextView = findViewById(R.id.drawing)
        numLayers.text = layers.toString()
        numChoco.text = choco.toString()
        numCherry.text = cherry.toString()
        drawingText.text = drawing

        buttonPlusLayers.setOnClickListener {
            if (layers < 6){
                layers++
                choco = layers-1
                cherry = 0
                numLayers.text = layers.toString()
                numChoco.text = choco.toString()
                numCherry.text = cherry.toString()
            }
        }
        buttonMinusLayers.setOnClickListener {
            if (layers > 2){
                layers--
                choco = layers-1
                cherry = 0
                numLayers.text = layers.toString()
                numChoco.text = choco.toString()
                numCherry.text = cherry.toString()
            }
        }
        buttonPlusChoco.setOnClickListener {
            if (choco < layers-1){
                choco++
                cherry = layers - choco - 1
                numChoco.text = choco.toString()
                numCherry.text = cherry.toString()
            }
        }
        buttonMinusChoco.setOnClickListener {
            if (choco > 0){
                choco--
                cherry = layers - choco - 1
                numChoco.text = choco.toString()
                numCherry.text = cherry.toString()
            }
        }
        buttonPlusCherry.setOnClickListener {
            if (cherry < layers-1){
                cherry++
                choco = layers - cherry - 1
                numChoco.text = choco.toString()
                numCherry.text = cherry.toString()
            }
        }
        buttonMinusCherry.setOnClickListener {
            if (cherry > 0){
                cherry--
                choco = layers - cherry - 1
                numChoco.text = choco.toString()
                numCherry.text = cherry.toString()
            }
        }
        buttonDR.setOnClickListener {
            drawing = "DR"
            drawingText.text = "С ДР!"
        }
        buttonSmile.setOnClickListener {
            drawing = "Smile"
            drawingText.text = "Смайл"
        }
        buttonLab.setOnClickListener {
            drawing = "Lab"
            drawingText.text = "Лабиринт"
        }
        buttonSquare.setOnClickListener {
            drawing = "Square"
            drawingText.text = "Квадрат"
        }
        buttonCreate.setOnClickListener {
            var comm = layers.toString() + "|" + choco.toString() + "|" + cherry.toString() + "|" + drawing
            val sendData = comm.toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
    }
    fun onClickHome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}