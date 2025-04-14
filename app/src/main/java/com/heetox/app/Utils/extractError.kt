package com.heetox.app.Utils

import com.google.gson.Gson
import com.heetox.app.Model.Authentication.ErrorResponse
import retrofit2.Response


fun extractErrorMessage(response: Response<*>): String {
    return try {
        val errorBody = response.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        errorResponse?.message ?: "Unknown error"
    } catch (e: Exception) {
        "Unexpected error occurred"
    }
}