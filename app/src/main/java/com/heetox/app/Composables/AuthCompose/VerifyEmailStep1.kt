package com.heetox.app.Composables.AuthCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.Model.Authentication.SendOtpSend
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun VerifyemailStep1(viewmodel: AuthenticationViewModel,email : String, navController: NavHostController){

    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(HeetoxWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){


       Box(
           modifier = Modifier
               .fillMaxSize()
               .background(HeetoxWhite)
       ) {


           Column(

               modifier = Modifier
                   .background(HeetoxGreen)
                   .verticalScroll(scrollState, true)
           ) {

               Column(

                   modifier = Modifier
                       .height(100.dp)
                       .fillMaxWidth(),
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally


               ){
                   Text(text = "Verify Email",
                       fontWeight = FontWeight.W900,
                       fontSize = 30.sp
                   )

               }

               Column(modifier = Modifier
                   .fillMaxSize()
                   .shadow(50.dp)
                   .clip(
                       RoundedCornerShape(
                           topStart = 30.dp,
                           topEnd = 30.dp,
                           bottomEnd = 0.dp,
                           bottomStart = 0.dp
                       )
                   )
                   .background(HeetoxWhite)
                   ,
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {



                   Spacer(
                       modifier = Modifier
                           .height(180.dp)
                   )





                       Text(
                           text = "An OTP will be Sent to Your Email Account",
                           fontSize = 15.sp,
                           color = HeetoxDarkGray,
                           modifier = Modifier

                       )


                       Spacer(
                           modifier = Modifier
                               .height(15.dp)
                       )

                       Text(
                           text = "your Email",
                           fontSize = 15.sp,
                           color = HeetoxDarkGray,

                           )

                       Text(
                           text = email,
                           fontSize = 15.sp,
                           color = HeetoxDarkGray,
                           modifier = Modifier
                               .padding(5.dp),
                           fontWeight = FontWeight.Bold
                       )


                       Button(
                           onClick = {

                               navController.navigate("updateprofile")

                           },
                           modifier = Modifier
                               .padding(2.dp)
                           ,
                           colors = ButtonDefaults.buttonColors(
                               contentColor = HeetoxDarkGreen,
                               containerColor = HeetoxWhite
                           ),
                           shape = RoundedCornerShape(10.dp),

                           ) {

                           Text(text = "Update Email")


                       }



                       Box(

                           modifier = Modifier
                               .padding(0.dp, 30.dp, 0.dp, 20.dp)
                               .height(2.dp)
                               .fillMaxWidth(0.8f)
                               .background(HeetoxLightGray)

                       ) {

                       }


                       Button(
                           onClick = {


                               viewmodel.sendotp(
                                   SendOtpSend(email)
                               )


                           },
                           modifier = Modifier
                               .padding(10.dp)
                               .width(130.dp)
                           ,
                           colors = ButtonDefaults.buttonColors(
                               contentColor = Color.White,
                               containerColor = HeetoxDarkGreen
                           ),
                           shape = RoundedCornerShape(10.dp)


                       ) {

                           Text(text = "Send Otp")

                       }




               }

           }

       }

    }


}