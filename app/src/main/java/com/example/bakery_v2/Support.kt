package com.example.bakery_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Support : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
    }
    fun onClickHome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}