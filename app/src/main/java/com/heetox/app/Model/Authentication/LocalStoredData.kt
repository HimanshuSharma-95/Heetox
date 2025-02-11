package com.heetox.app.Model.Authentication

data class LocalStoredData (
    val Status : Boolean = false ,
    val Token : String? = null ,
    val Name : String = "" ,
    val Email : String = "" ,
    val EmailStatus : Boolean = false,
    val Age : String,
    val Phone : String,
    val Dob : String,
    val avatar : String,
    val gender : String
)