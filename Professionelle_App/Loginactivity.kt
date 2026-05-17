package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        loginBtn.setOnClickListener {
            auth.signInWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            ).addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        registerBtn.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            ).addOnSuccessListener {
                Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}