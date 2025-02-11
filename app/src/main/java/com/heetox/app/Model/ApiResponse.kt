package com.heetox.app.Model

data class ApiResponse<T>(
    val message: String,
    val data: T,
    val statuscode: Int,
    val success: Boolean,
)