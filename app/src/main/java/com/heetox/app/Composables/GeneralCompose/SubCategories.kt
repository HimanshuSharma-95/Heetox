package com.heetox.app.Composables.GeneralCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxWhite



@Composable
fun SubCategoriesItem(item: String, isSelected: Boolean, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .width(120.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(if (isSelected) HeetoxDarkGreen else HeetoxWhite)
            .clickable {
                onClick(item)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item,
            modifier = Modifier.padding(8.dp),
            fontSize = 14.sp,
            color = if (isSelected) HeetoxWhite else Color.Black
        )
    }
}
