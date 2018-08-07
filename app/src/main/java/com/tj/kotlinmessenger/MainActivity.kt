package com.tj.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
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

        button_select_photo.setOnClickListener{
            Log.d("MainActivity","Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)

        }
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)
        {
            Log.d("MainActivity","Photo was selected")
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            selectphoto_imageview.setImageBitmap(bitmap)
            button_select_photo.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            button_select_photo.setBackgroundDrawable(bitmapDrawable)

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
                    uploadImageToFirebaseStorage()
                }
                .addOnFailureListener{
                    Log.d("Main","Failed to create user: ${it.message}")
                    Toast.makeText(this,"Email format is incorrect",Toast.LENGTH_SHORT).show()
                }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Register","Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity","File location: $it")

                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String)
    {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, editText_username.text.toString(), profileImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity","Finally we saved the user to Firebase Database.")
                }
    }
}

class User(val uid:String, val username: String, val profileImageUrl:String)
