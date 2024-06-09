package com.example.bakery_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Commands : AppCompatActivity() {
    private val serverIp = "192.168.4.1"
    private val udpPort = 80
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commands)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val socket = DatagramSocket()

        val inputCommand: EditText = findViewById(R.id.commands_input)
        val buttonConfirm: Button = findViewById(R.id.commands_send)

        buttonConfirm.setOnClickListener {
            var comm = inputCommand.text.toString()
            val sendData = (comm.toString()).toByteArray()
            val sendPacket = DatagramPacket(sendData,sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.4.1"), 80)
            socket.send(sendPacket)
        }
    }
    fun onClickHome(view: View){
        val intent = Intent(this,SettingsNavigate::class.java)
        startActivity(intent)}
}