package com.mohammedezzaim.demo

import java.io.Serializable

class Student(
    var id:Long,
    var nom: String,
    var status: String,
    val imagePath: String ) : Serializable {

}