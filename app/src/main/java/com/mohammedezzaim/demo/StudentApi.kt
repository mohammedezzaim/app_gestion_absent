package com.mohammedezzaim.demo

import retrofit2.Call
import retrofit2.http.*

interface StudentApi {

    @GET("/api/students/all")
    fun getStudents(): Call<List<Student>>

    @GET("/api/students/student/{name}")
    fun getStudentByName(@Query("name") name: String): Call<Student>

    @POST("/api/students/add")
    fun addStudent(@Body student: Student): Call<Student>
}