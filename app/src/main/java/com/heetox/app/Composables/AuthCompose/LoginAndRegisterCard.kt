package com.heetox.app.Composables.AuthCompose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray


@Composable
fun LoginAndRegisterCard(navController: NavHostController){


    Box( modifier = Modifier
        .padding(15.dp)
        .clip(RoundedCornerShape(30.dp))
        .fillMaxWidth()
        .background(Color.White),

    ){

        Column( 
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

Text(text = "Seems Like You Haven't Signed in yet!",
modifier = Modifier,
    fontSize = 15.sp
    )

            Box(modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(.9f)
                .height(1.5.dp)
                .background(HeetoxLightGray)
            )

           Row(modifier = Modifier
               .padding()

           ){


               //button1
               Button(

modifier = Modifier
    .padding(horizontal = 5.dp)
    .width(120.dp)

, colors = ButtonDefaults.run {
    buttonColors(
                   contentColor = Color.White,
                   containerColor = HeetoxDarkGreen
               )},
                   shape = RoundedCornerShape(10.dp),
                   onClick = {
                       navController.navigate("register")
                   }) {

                   Text(text = "Register",
                       fontWeight = FontWeight.Bold,
                       fontSize = 15.sp)

               }




               //button2
               Button(

                   modifier = Modifier
                       .padding(horizontal = 5.dp)
                       .width(120.dp),

                    colors = ButtonDefaults.run {
                       buttonColors(
                           contentColor = HeetoxDarkGreen,
                           containerColor = Color.White
                       )},
                   shape = RoundedCornerShape(10.dp),
border = BorderStroke(2.dp, HeetoxDarkGreen),
                   onClick = {
                       navController.navigate("login")
                   }) {

                   Text(text = "Login",
                       fontWeight = FontWeight.Bold,
                       fontSize = 15.sp)

               }

           }



        }
        
        
    }



}