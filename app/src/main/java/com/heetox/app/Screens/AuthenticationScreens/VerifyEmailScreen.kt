package com.heetox.app.Screens.AuthenticationScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Composables.AuthCompose.VerifyEmailStep1
import com.heetox.app.Composables.AuthCompose.VerifyEmailStep2
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Authentication.SendOtpSend
import com.heetox.app.Model.Authentication.VerifyEmailSend
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import kotlinx.coroutines.flow.Flow


@Composable
fun VerifyEmailScreen(navController: NavHostController,
                      userData:LocalStoredData?,
                      uiEvent:Flow<UiEvent>,
                      sendOtp:(SendOtpSend)->Unit,
                      verifyOtp:(String,VerifyEmailSend)->Unit
                      ){


    val context = LocalContext.current
    
    val token by rememberSaveable {
        mutableStateOf(userData?.Token)
    }
    val email by rememberSaveable {
        mutableStateOf(userData?.Email)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }


    var step by rememberSaveable{
        mutableIntStateOf(1)
    }

    var loading by rememberSaveable{
mutableStateOf(false)
    }




    if(loading){

        LoadingOverlay(isLoading = loading)

    }else{


        if(step == 1 ){


              Column {

                  email?.let { emailValue ->
                      VerifyEmailStep1(
                          sendOtp = { otp ->
                              sendOtp(otp)
                          },
                          emailValue,
                          navController
                      )
                  }
              }


        }else if(step == 2){

            Column {

                if (token != null && email != null) {
                    VerifyEmailStep2(
                        verifyEmail = { token, data -> verifyOtp(token, data)},
                        token = token!!,
                        email = email!!,
                        error = error
                    )
                }

            }
        }

    }


    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    when (event.action) {
                        Action.SendOTP -> {
                            loading = true
                            error = "Sending OTP"
                        }
                        Action.VerifyOTP -> {
                            loading = true
                            error = "Verifying..."
                        }
                        else -> {}
                    }
                }

                is UiEvent.Success -> {
                    when (event.action) {
                        Action.SendOTP -> {
                            step = 2
                            loading = false
                            error = ""
                            Toast.makeText(context, "OTP sent", Toast.LENGTH_SHORT).show()
                        }
                        Action.VerifyOTP -> {
                            loading = false
                            error = ""
                            Toast.makeText(context, "Email Verified", Toast.LENGTH_SHORT).show()
                            navController.navigate("profile")
                        }
                        else -> {}
                    }
                }

                is UiEvent.Error -> {
                    when (event.action) {
                        Action.SendOTP -> {
                            loading = false
                            error = event.message
                            Toast.makeText(context, "OTP cannot be sent. Try again.", Toast.LENGTH_SHORT).show()
                        }
                        Action.VerifyOTP -> {
                            loading = false
                            error = event.message
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }

                UiEvent.Idle -> {
                    loading = false
                    error = ""
                }
            }
        }
    }



}