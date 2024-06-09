package com.example.bakery_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SettingsNavigate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_navigate)
    }
    fun onClickTO(view: View){
        val intent = Intent(this,Settings::class.java)
        startActivity(intent)
    }
    fun onClickFood(view: View){
        val intent = Intent(this,Products::class.java)
        startActivity(intent)
    }
    fun onClickCommands(view: View){
        val intent = Intent(this,Commands::class.java)
        startActivity(intent)
    }
    fun onClickHome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}