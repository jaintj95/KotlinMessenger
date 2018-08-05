package com.tj.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        button_login.setOnClickListener {
            val email = editText_username_login.text.toString()
            val password = editText_password_login.text.toString()

            Log.d("Login","Attempt login with email/pw: $email/****")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        val intent = Intent(this,HomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener{
                        Toast.makeText(this,"                 Login failed.\nUsername or Password incorrect.",Toast.LENGTH_SHORT).show()
                    }
        }

        textView_backToRegister.setOnClickListener {
            finish()
        }
    }
}