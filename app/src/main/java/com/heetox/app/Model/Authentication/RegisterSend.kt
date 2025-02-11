package com.heetox.app.Model.Authentication

data class RegisterSend(
    val DOB: String,
    val email: String,
    val fullname: String,
    val password: String,
    val phone_number: String,
    val gender : String
)