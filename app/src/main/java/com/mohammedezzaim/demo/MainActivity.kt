package com.mohammedezzaim.demo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var login = findViewById<EditText>(R.id.login)
        var password = findViewById<EditText>(R.id.password)
        var btn = findViewById<Button>(R.id.btn_auth);

        val messageError = "your login is not be Empty"
        val messageSuccess = "successful!"
        val messageField = "Your password or login is not valid"

        btn.setOnClickListener({
            if (login == null || password == null) {
                Toast.makeText(this, messageError, Toast.LENGTH_LONG).show()
            }else{
                if (login.text.toString() == "admin" && password.text.toString() == "admin"){
                    Toast.makeText(this, messageSuccess, Toast.LENGTH_LONG).show()

                    val intent = Intent(this, HomeActivity::class.java)

                    intent.putExtra("login", login.text.toString())

                    startActivity(intent)

                    finish()

                }else{
                    Toast.makeText(this, messageField, Toast.LENGTH_LONG).show()            }
            }
        })
    }
}