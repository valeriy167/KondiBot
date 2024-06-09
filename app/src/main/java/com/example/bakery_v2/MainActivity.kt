package com.example.bakery_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClickCreate(view: View){
        val intent = Intent(this,Build::class.java)
        startActivity(intent)
    }
    fun onClickSupport(view: View){
        val intent = Intent(this,Support::class.java)
        startActivity(intent)
    }
    fun onClickInformation(view: View){
        val intent = Intent(this,Information::class.java)
        startActivity(intent)
    }
    fun onClickReady(view: View){
        val intent = Intent(this,Prepared::class.java)
        startActivity(intent)
    }
    fun onClickSettingsCheck(view: View){
        val intent = Intent(this,CheckPass::class.java)
        startActivity(intent)
    }
}