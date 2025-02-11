package com.heetox.app.Composables.AuthCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heetox.app.R
import com.heetox.app.ui.theme.HeetoxDarkGray


@Composable
fun BaseHeading(){

    Column(
        modifier = Modifier
            .padding(25.dp)
    ) {

        Text(
            text = "Welcome To,",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = HeetoxDarkGray
            )
        )
        Image(
            painter = painterResource(id = R.drawable.heetoxlogobg),
            contentDescription = "",
            modifier = Modifier
                .width(180.dp)
        )

    }

}