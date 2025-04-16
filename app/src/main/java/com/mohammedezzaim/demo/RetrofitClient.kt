package com.mohammedezzaim.demo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    private const val BASE_URL = "http://10.0.2.2:8080" // Emulator's localhost

//    private const val BASE_URL = "http://192.168.1.12:8080" // Emulator's localhost
    val instance: StudentApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(StudentApi::class.java)
    }
}