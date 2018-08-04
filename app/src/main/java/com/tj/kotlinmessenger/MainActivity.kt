package com.tj.kotlinmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_register.setOnClickListener {
            val email = editText_email.text.toString()
            val password = editText_password.text.toString()

            Log.d("MainActivity", "Email is: $email")
            Log.d("MainActivity","Password: $password")
        }

        textView_account_already.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
