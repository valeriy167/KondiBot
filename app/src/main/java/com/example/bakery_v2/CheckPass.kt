package com.example.bakery_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class CheckPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_pass)
        val inputPassword: EditText = findViewById(R.id.password_input)
        val buttonConfirm: Button = findViewById(R.id.confirm_button)

        buttonConfirm.setOnClickListener {
            var password = inputPassword.text.toString()
            when(password){
                "asd123asd" -> startActivity(Intent(this,SettingsNavigate::class.java))
                else -> startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }
    fun onClickHome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}