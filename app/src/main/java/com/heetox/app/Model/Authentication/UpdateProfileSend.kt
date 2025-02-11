package com.heetox.app.Model.Authentication

data class UpdateProfileSend(
    val email : String,
    val fullname : String,
    val phone_number : String,
    val DOB : String,
    val gender : String
)
