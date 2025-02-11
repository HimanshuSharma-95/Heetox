package com.heetox.app.Composables.GeneralCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.R
import com.heetox.app.ui.theme.HeetoxBrightYellow
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen


@Composable
fun TextAndScanHome(navcontoller : NavHostController,name : String? = null){

    Row(
        modifier = Modifier
            .width(400.dp)
            .height(200.dp)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){


 Column{
     if(name != null){
         Text(
             text = "Hi, ${name.split(" ")[0]}" ,
             fontWeight = FontWeight.W800,
             fontSize = 21.sp,
             style = MaterialTheme.typography.bodyLarge,
//             modifier = Modifier.fillMaxWidth(),
             color = HeetoxDarkGray,
         )
     }

     Text(
         text = "Know whats in \n" +
                 "Your Food",
         fontWeight = FontWeight.W800,
         fontSize = 21.sp,
         style = MaterialTheme.typography.bodyLarge,
         color = HeetoxDarkGreen
     )

     Text(
         text = "Scan Barcode",
         fontWeight = FontWeight.W800,
         fontSize = 21.sp,
         style = MaterialTheme.typography.bodyLarge,
         color = HeetoxBrightYellow
     )

 }

//            Spacer(modifier = Modifier
//                .height(7.dp)
//            )


        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(HeetoxGreen)
                .clickable {
navcontoller.navigate("scan")
                }
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Image(painter = painterResource(id = R.drawable.barcodeimagehome), contentDescription = "Barcode Image",

                modifier = Modifier.size(100.dp)

                )

            Text(text = "Scan Now",
color = HeetoxDarkGray,
                style = MaterialTheme.typography.bodyMedium
                )




        }
    }


}
