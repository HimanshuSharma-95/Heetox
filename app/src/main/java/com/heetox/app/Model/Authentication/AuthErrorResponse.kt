package com.heetox.app.Model.Authentication

data class ErrorResponse(
    val statusCode: Int,
    val data: Any?,
    val success: Boolean,
    val errors: List<Any>,
    val message: String
)
