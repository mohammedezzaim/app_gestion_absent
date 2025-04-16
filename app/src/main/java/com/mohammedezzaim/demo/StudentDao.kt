package com.mohammedezzaim.demo


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.StudentDbHelper

class StudentDao(context: Context) {

    private val dbHelper = StudentDbHelper(context)

    fun addStudent(student: Student) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", student.nom)
            put("status", student.status)
            put("imagePath", student.imagePath)
        }
        db.insert("students", null, values)
        db.close()
    }

    fun getAllStudents(): ArrayList<Student> {
        val list = ArrayList<Student>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM students", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
                val imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"))
                list.add(Student(name, status, imagePath))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateStudentStatus(name: String, newStatus: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("status", newStatus)
        }
        db.update("students", values, "name=?", arrayOf(name))
        db.close()
    }
}