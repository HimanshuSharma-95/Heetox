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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heetox.app.Model.Authentication.VerifyEmailSend
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun VerifyEmailStep2(viewmodel : AuthenticationViewModel,token :String, email : String, error:String){

    val scrollState = rememberScrollState()

    var otp by rememberSaveable {
        mutableStateOf("")
    }

    var step2error by rememberSaveable {
        mutableStateOf(error)
    }


Box(
    modifier = Modifier
        .fillMaxSize()
        .background(HeetoxWhite)
){

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


        ) {
            Text(
                text = "Verify Email",
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
                .background(HeetoxWhite),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){


                Spacer(modifier = Modifier
                    .height(140.dp)
                )


                    Text(text = "OTP has been Sent to Your Email Account",
                        fontSize = 15.sp,
                        color = HeetoxDarkGray,
                        modifier = Modifier

                    )

                Spacer(modifier = Modifier
                    .height(10.dp)
                )


                    Text(text = step2error,
                        fontSize = 15.sp,
                        color = HeetoxDarkGreen,
                        modifier = Modifier.padding(10.dp)
                    )


                    Spacer(modifier = Modifier
                        .height(10.dp)
                    )


                    Text(text = "Enter OTP Below" ,
                        fontSize = 15.sp,
                        color = HeetoxDarkGray,
                        modifier = Modifier
                            .padding(5.dp),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier
                        .height(0.dp)
                    )


                    Input(label = "6 Digit OTP", value = otp, onValueChange = {otp = it},
                        KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone) )


                Spacer(modifier = Modifier
                    .height(40.dp)
                )


                    Button(onClick = {

                        if(otp.length > 6 || otp.length < 6){

                            step2error = "Incorrect OTP"

                        }else{

                            viewmodel.verifyotp(token, VerifyEmailSend(
                                email,otp
                            ))

                        }

                    },
                        modifier = Modifier
                            .padding(10.dp)
                            .width(120.dp)
                        ,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = HeetoxDarkGreen
                        ),
                        shape = RoundedCornerShape(10.dp)



                    ) {

                        Text(text = "Submit")

                    }


                Spacer(modifier = Modifier
                    .height(100.dp)
                )



            }



    }

}




}