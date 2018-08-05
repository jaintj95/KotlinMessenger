package com.tj.kotlinmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_register.setOnClickListener {
            performRegister()
        }

        textView_account_already.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister(){
        val email = editText_email.text.toString()
        val password = editText_password.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Email/Password cannot be empty",Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is:" + email)
        Log.d("MainActivity","Password: $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main","Successfully created user with UID: ${it.result.user.uid}")
                }
                .addOnFailureListener{
                    Log.d("Main","Failed to create user: ${it.message}")
                    Toast.makeText(this,"Email format is incorrect",Toast.LENGTH_SHORT).show()
                }
    }
}
