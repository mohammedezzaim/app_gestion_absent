package com.mohammedezzaim.demo

import StudentAdapter
import StudentDatabaseHelper
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var dbHelper: StudentDatabaseHelper
    private lateinit var listStudent: ArrayList<Student>
    private lateinit var adaptorListStudent: StudentAdapter

    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Récupérer les éléments par ID
        val addEt = findViewById<EditText>(R.id.addEt)
        val idAbsent = findViewById<RadioButton>(R.id.idAbsent)
        val idPresent = findViewById<RadioButton>(R.id.idPresent)
        val btnAdd = findViewById<Button>(R.id.btn_add)
        val listViewStudent = findViewById<ListView>(R.id.idListEtudient)

        imageView = findViewById(R.id.imageView)
        val btnSelectImage: Button = findViewById(R.id.btn_select_image)

        dbHelper = StudentDatabaseHelper(this)
        listStudent = ArrayList(dbHelper.getAllStudents())



        adaptorListStudent = StudentAdapter(this, R.layout.item_etudiant,listStudent)

        listViewStudent.adapter = adaptorListStudent


        btnSelectImage.setOnClickListener {
            openGallery()
        }
// Fetch students from the API when activity is created
        fetchStudentsFromAPI()
        btnAdd.setOnClickListener({
            val studentName = addEt.text.toString()
            val imagePath = selectedImageUri?.let { saveImageToInternalStorage(it) } ?: ""

            if (studentName.isNotEmpty()) {

                if (idAbsent.isChecked) {
                    val status = "Absent"
                    listStudent.add(Student(studentName, status, imagePath))
                    dbHelper.insertStudent(studentName, status, imagePath)
                    var student = Student(studentName, status, imagePath)
                    addStudentToAPI(student)
                    adaptorListStudent.notifyDataSetChanged()
                } else if (idPresent.isChecked) {
                    val status = "Present"
                    listStudent.add(Student(studentName, status, imagePath))
                    dbHelper.insertStudent(studentName, status, imagePath)
                    var student = Student(studentName, status, imagePath)
                    addStudentToAPI(student)
                    adaptorListStudent.notifyDataSetChanged()
                } else {

                    Toast.makeText(
                        this,
                        "Please select a status (Absent/Present)",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                addEt.text.clear()
                idAbsent.isChecked = false
                idPresent.isChecked = true
                selectedImageUri = null
                imageView.setImageResource(R.drawable.image_profile)
            } else {

                Toast.makeText(this, "Please enter a student name", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun fetchStudentsFromAPI() {
        RetrofitClient.apiService.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    listStudent.clear()  // Clear existing data
                    response.body()?.let { listStudent.addAll(it) }  // Add new students
                    adaptorListStudent.notifyDataSetChanged()  // Update adapter
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to fetch students", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addStudentToAPI(student: Student) {
        RetrofitClient.apiService.addStudent(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@HomeActivity, "Student added successfully!", Toast.LENGTH_SHORT).show()
                    fetchStudentsFromAPI() // Refresh the list after adding a student
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to add student", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }



    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri): String {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        val appDirectory = File(filesDir, "MyAppImages")
        if (!appDirectory.exists()) {
            appDirectory.mkdirs()
        }
        val fileName = "student_${System.currentTimeMillis()}.png"
        val file = File(appDirectory, fileName)
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()
        return file.absolutePath
    }
}