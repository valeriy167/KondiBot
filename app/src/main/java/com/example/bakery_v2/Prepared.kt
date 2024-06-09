package com.example.bakery_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Prepared : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepared)
    }
    fun onClickCherry(view: View){

    }
    fun onClickChocolate(view: View){

    }
    fun onClickChocoCherry(view: View){

    }
    fun onClickHome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}