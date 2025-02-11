package com.heetox.app.Model.Authentication

data class LogoutResponse(
    val data: Any?,
    val message: String,
    val statuscode: Int,
    val success: Boolean
)