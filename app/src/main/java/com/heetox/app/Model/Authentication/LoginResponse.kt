package com.heetox.app.Model.Authentication

data class LoginResponse(
    val data: LoginData,
    val message: String,
    val statuscode: Int,
    val success: Boolean
)