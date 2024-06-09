package com.example.bakery_v2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.net.DatagramPacket
import java.net.InetAddress

class Products : AppCompatActivity() {
    var pref : SharedPreferences? = null
    var chocolate = ""
    var cherry = ""
    var bread = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        val pr_choco_date: TextView = findViewById(R.id.pr_date_choco)
        val pr_cherry_date: TextView = findViewById(R.id.pr_date_cherry)
        val pr_bread_date: TextView = findViewById(R.id.pr_date_bread)
        pref = getSharedPreferences("TABLE_PRODUCTS", Context.MODE_PRIVATE)
        chocolate = pref?.getString("choco_date", "0")!!
        cherry = pref?.getString("cherry_date", "0")!!
        bread = pref?.getString("bread_date", "0")!!
        pr_choco_date.text = chocolate
        pr_cherry_date.text = cherry
        pr_bread_date.text = bread
    }
    fun onClickSave(view: View){
        val inputDateChocolate: EditText = findViewById(R.id.choco_date)
        val inputDateCherry: EditText = findViewById(R.id.cherry_date)
        val inputDateBread: EditText = findViewById(R.id.bread_date)
        val pr_choco_date: TextView = findViewById(R.id.pr_date_choco)
        val pr_cherry_date: TextView = findViewById(R.id.pr_date_cherry)
        val pr_bread_date: TextView = findViewById(R.id.pr_date_bread)
        var newChocolate = inputDateChocolate.text.toString()
        var newCherry = inputDateCherry.text.toString()
        var newBread = inputDateBread.text.toString()
        if (newChocolate != chocolate && newChocolate != null && newChocolate != "0"){
            saveChoco(newChocolate)
        }
        if (newCherry != cherry && newCherry != null && newChocolate != "0"){
            saveCherry(newCherry)
        }
        if (newBread != bread && newBread != null && newChocolate != "0"){
            saveBread(newBread)
        }
        pr_choco_date.text = pref?.getString("choco_date", "0")!!
        pr_cherry_date.text = pref?.getString("cherry_date", "0")!!
        pr_bread_date.text = pref?.getString("bread_date", "0")!!
    }
    fun saveChoco(res: String){
        val editor = pref?.edit()
        editor?.putString("choco_date", res)
        editor?.apply()
    }
    fun saveCherry(res: String){
        val editor = pref?.edit()
        editor?.putString("cherry_date", res)
        editor?.apply()
    }
    fun saveBread(res: String){
        val editor = pref?.edit()
        editor?.putString("bread_date", res)
        editor?.apply()
    }
    fun onClickHome(view: View){
        val intent = Intent(this,SettingsNavigate::class.java)
        startActivity(intent)
    }

//    private fun callActivity() {
//
//        val inputDateCherry: EditText = findViewById(R.id.cherry_date)
//        val inputDateBread: EditText = findViewById(R.id.bread_date)
//        val cherry = inputDateCherry.text.toString()
//        val bread = inputDateBread.text.toString()
////        val intent = Intent(this,Information::class.java).also{
////            it.putExtra("CHOCOLATE",chocolate)
////            it.putExtra("CHERRY", cherry)
////            it.putExtra("BREAD", bread)
////
////            startActivity(it)
////        }
//    }

}