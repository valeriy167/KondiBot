package com.example.bakery_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Settings : AppCompatActivity() {
    private val serverIp = "0.0.0.0"
    private val udpPort = 80
    lateinit var bar : SeekBar
    lateinit var rate : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Allow network operations on the main thread (Note: This is not recommended in production code)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val socket = DatagramSocket()

        val left_up: ImageButton = findViewById(R.id.lUp)
        val left_down: ImageButton = findViewById(R.id.lDown)
        val right_up: ImageButton = findViewById(R.id.rUp)
        val right_down: ImageButton = findViewById(R.id.rDown)
        val table_up: ImageButton = findViewById(R.id.tUp)
        val table_down: ImageButton = findViewById(R.id.tDown)
        val table_forward: ImageButton = findViewById(R.id.tFwd)
        val table_backward: ImageButton = findViewById(R.id.tBwd)
        val painter_forward: ImageButton = findViewById(R.id.pFwd)
        val painter_backward: ImageButton = findViewById(R.id.pBwd)
        val reset_pos: Button = findViewById(R.id.reset)
        val left_throw: Button = findViewById(R.id.thrL)
        val right_throw: Button = findViewById(R.id.thrR)
        val left_fill: Button = findViewById(R.id.fillL)
        val right_fill: Button = findViewById(R.id.fillR)
        val bar: SeekBar = findViewById(R.id.seekBar)
        val rate: TextView = findViewById(R.id.moveRate)

        var coef = 1.0

        bar.max = 50

        bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                coef = (progress / 10.0)
                rate.text = coef.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        left_throw.setOnClickListener {
            val sendData = ("TL").toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        right_throw.setOnClickListener {
            val sendData = ("TR").toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        left_fill.setOnClickListener {
            val sendData = ("FL").toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        right_fill.setOnClickListener {
            val sendData = ("FR").toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        painter_forward.setOnClickListener {
            val sendData = ("My-"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        painter_backward.setOnClickListener {
            val sendData = ("My+"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        left_up.setOnClickListener {
            val sendData = ("Kz+"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        left_down.setOnClickListener {
            val sendData = ("Kz-"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        right_down.setOnClickListener {
            val sendData = ("Ky-"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        right_up.setOnClickListener {
            val sendData = ("Ky+"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        table_down.setOnClickListener {
            val sendData = ("Mz-"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        table_up.setOnClickListener {
            val sendData = ("Mz+"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        table_forward.setOnClickListener {
            val sendData = ("Mx+"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        table_backward.setOnClickListener {
            val sendData = ("Mx-"+coef.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
        reset_pos.setOnClickListener {
            val sendData = ("H").toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
    }
    fun onClickHome(view: View){
        val intent = Intent(this,SettingsNavigate::class.java)
        startActivity(intent)
    }
}


