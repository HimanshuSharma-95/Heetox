package com.heetox.app.Model.General

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavData(
    val title : String,
    val SelectedIcon : ImageVector,
    val unSelectedIcon : ImageVector,
    val hasNews:Boolean,
    val badgeCount : Int? = null

)
