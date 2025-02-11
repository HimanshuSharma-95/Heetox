package com.heetox.app.Model.Authentication

import androidx.compose.ui.graphics.vector.ImageVector

data class NavDrawer(
    val title : String,
    val SelectedIcon:ImageVector,
    val UnselectedIcon : ImageVector,
    val route : String? = null
)
