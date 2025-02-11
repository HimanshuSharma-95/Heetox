package com.heetox.app.Model.Authentication

data class UpdateProfleResponse(
    val DOB: String,
    val __v: Int,
    val _id: String,
    val age: String,
    val createdAt: String,
    val email: String,
    val fullname: String,
    val is_email_verified: Boolean,
    val phone_number: String,
    val refreshToken: String,
    val token: String,
    val updatedAt: String,
    val avatar : String,
    val gender : String
)