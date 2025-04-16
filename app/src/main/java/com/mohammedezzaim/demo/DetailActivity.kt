package com.mohammedezzaim.demo

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        // Initialisation des vues
        val imageProfile: ImageView = findViewById(R.id.detail_image_profile)
        val studentName: TextView = findViewById(R.id.detail_name)
        val studentStatus: TextView = findViewById(R.id.detail_status)

        val btnMarkPresent: Button = findViewById(R.id.btn_mark_present)
        val btnMarkAbsent: Button = findViewById(R.id.btn_mark_absent)
        val btnShowStudentList: Button = findViewById(R.id.btn_show_student_list)

        // Récupération des données de l'intent
        val name = intent.getStringExtra("nom") ?: "Inconnu"
        val status = intent.getStringExtra("status") ?: "Non défini"
        val imagePath = intent.getStringExtra("imagePath")

        studentName.text = name
        studentStatus.text = status

        if (status=="Present"){
            studentStatus.setTextColor(ContextCompat.getColor(this, R.color.green_present))
        }
        else {
            studentStatus.setTextColor(ContextCompat.getColor(this, R.color.red_absent))
        }

        // Charger l'image de profil
        if (!imagePath.isNullOrEmpty()) {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                imageProfile.setImageURI(Uri.fromFile(imageFile))
            } else {
                imageProfile.setImageResource(R.drawable.image_profile) // Image par défaut
            }
        } else {
            imageProfile.setImageResource(R.drawable.image_profile)
        }

        fun showConfirmationDialog(newStatus: String) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmation")
            builder.setMessage("Voulez-vous vraiment marquer \"$name\" comme \"$newStatus\" ?")
            builder.setPositiveButton("Oui") { _, _ ->
                studentStatus.text = newStatus
                if(newStatus == "Présent") {
                    studentStatus.setTextColor(ContextCompat.getColor(this, R.color.green_present))
                }
                else {
                    studentStatus.setTextColor(ContextCompat.getColor(this, R.color.red_absent))
                }
                Toast.makeText(this, "$name est maintenant marqué comme $newStatus", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Non") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }

        btnMarkPresent.setOnClickListener {
            showConfirmationDialog("Présent")
        }

        btnMarkAbsent.setOnClickListener {
            showConfirmationDialog("Absent")
        }


        btnShowStudentList.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
