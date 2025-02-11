package com.heetox.app.Screens.AuthenticationScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.heetox.app.Composables.AuthCompose.Input
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite


//@Preview()
@Composable
fun Forgotpasswordscreen(navController: NavHostController){

    val viewmodel : AuthenticationViewModel = hiltViewModel()
    val ForgotPasswordData = viewmodel.ForgotPasswordData.collectAsState()
    val scrollState = rememberScrollState()

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var step by rememberSaveable {
        mutableStateOf(1)
    }

    var laoding by rememberSaveable {
        mutableStateOf(false)
    }





    if(laoding){
        LoadingOverlay(isLoading = laoding)
    }else{


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
                        text = "Forgot Password",
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




                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(HeetoxWhite),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){

                        if(step == 1){


                            Spacer(modifier = Modifier
                                .height(140.dp)
                            )
                            Text(text =error,
                                color = HeetoxDarkGreen
                            )

                            Spacer(modifier = Modifier
                                .height(30.dp)
                            )


                            Text(text = "Enter your email below",
                                color = HeetoxDarkGray
                            )

                            Spacer(modifier = Modifier
                                .height(10.dp)
                            )

                            Input(label = "Email", value = email, onValueChange = {email = it})


                            val emailregex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")


                            Spacer(modifier = Modifier
                                .height(20.dp)
                            )

                            Button(onClick = {

                                if(emailregex.matches(email.trim())){

                                    viewmodel.forgotpassword(email)

                                }else{

                                    error = "Email is not correct"

                                }


                            },

                                modifier = Modifier
                                    .padding(30.dp),
                                colors = ButtonDefaults.buttonColors(

                                    containerColor = HeetoxDarkGreen,
                                    contentColor = Color.White

                                ),
                                shape = RoundedCornerShape(10.dp)

                            ) {
                                Text(text = "Send Email")
                            }


                            Spacer(modifier = Modifier
                                .height(50.dp)
                            )


                        }else {


                        Column (

                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Spacer(modifier = Modifier.height(200.dp))


                            Text(
                                text = "Email has been Sent",
                                color = HeetoxDarkGray,
                                modifier = Modifier
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "check your email To reset your password",
                                color = HeetoxDarkGray,
                                modifier = Modifier
                            )

                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Go back to Login",
                                color = HeetoxDarkGray
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(onClick = {

                                navController.navigate("login")

                            },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = HeetoxDarkGreen
                                ),
                                shape = RoundedCornerShape(10.dp)
                                ){

                                Text(text = "Login",)

                            }

                        }

                        }

                    }






                }




            }



        }


    }











    LaunchedEffect(key1 = ForgotPasswordData.value) {

        when(ForgotPasswordData.value){

            is Resource.Error -> {
                error = ForgotPasswordData.value.error.toString()
                laoding = false
            }
            is Resource.Loading -> {

                error = "Sending Email"
                laoding = true

            }
            is Resource.Nothing -> {

                error = ""
                laoding = false

            }
            is Resource.Success -> {

                error = ""
                step = 2
                laoding = false

            }

        }

    }


}


