package com.example.bakery_v2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Information : AppCompatActivity() {
    var pref : SharedPreferences? = null
    var chocolate = ""
    var cherry = ""
    var bread = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val chocoDate: TextView = findViewById(R.id.choco_d)
        val cherryDate: TextView = findViewById(R.id.cherry_d)
        val breadDate: TextView = findViewById(R.id.bread_d)
        pref = getSharedPreferences("TABLE_PRODUCTS", Context.MODE_PRIVATE)
        chocolate = pref?.getString("choco_date", "0")!!
        cherry = pref?.getString("cherry_date", "0")!!
        bread = pref?.getString("bread_date", "0")!!
        chocoDate.text = chocolate
        cherryDate.text = cherry
        breadDate.text = bread
    }
//    fun onClickRenew(view: View){
//        val chocolate = intent.getStringExtra("CHOCOLATE")
//        val cherry= intent.getStringExtra("CHERRY")
//        val bread = intent.getStringExtra("BREAD")
//        prefChocolate = getSharedPreferences("TABLE_PRODUCTS", Context.MODE_PRIVATE)
//
//        val printChoco = findViewById<TextView>(R.id.choco_d).apply{
//            text = chocolate
//        }
//        val printCherry = findViewById<TextView>(R.id.cherry_d).apply{
//            text = cherry
//        }
//        val printBread = findViewById<TextView>(R.id.bread_d).apply{
//            text = bread
//        }
//    }

    fun onClickHome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}