package com.heetox.app.Model.Authentication

import android.content.Context
import android.net.Uri

data class UpdateUserProfileImageSend(
    val context: Context,
    val uri: Uri,
    val token: String
)