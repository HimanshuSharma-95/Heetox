package com.heetox.app.Model.Authentication

data class ChangePasswordSend(
    val oldpassword: String,
    val newpassword: String
)