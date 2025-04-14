package com.heetox.app.Composables.GeneralCompose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray


@Composable
fun AboutUsHome(navController:NavHostController){


    Column(

        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

Column(

    modifier = Modifier
        .padding(20.dp)
,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
){


    Text(text = "Want to know who we are " +
            "and why we are Doing this !!",
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,

        modifier = Modifier
            .padding(horizontal = 20.dp),

        color = HeetoxDarkGray
    )


    Box(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(.8f)
        .height(2.dp)
        .background(HeetoxLightGray)
    )


    Button(onClick = {
navController.navigate("aboutus")
    },
        colors = ButtonDefaults.buttonColors(

            containerColor = Color.White,
            contentColor = HeetoxDarkGreen

        ),
        border = BorderStroke(2.dp, HeetoxDarkGreen)

    ) {

        Text(text = "About Us ")

        Icon(imageVector = Icons.Filled.ArrowCircleRight, contentDescription = "Arrow")

    }


}


    }



}