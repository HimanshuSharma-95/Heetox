package com.heetox.app.Model.Authentication

import android.content.Context
import android.net.Uri

data class UpdateProfileFunctionSend(
    val context : Context,
    val token:String,
    val uri:Uri
)