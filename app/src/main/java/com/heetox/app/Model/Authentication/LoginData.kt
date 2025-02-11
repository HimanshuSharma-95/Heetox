package com.heetox.app.Model.Authentication

data class LoginData(
    val user: AuthUser,
    val accesstoken: String,
    val refreshtoken: String,
)