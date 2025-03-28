package com.heetox.app.Screens.AuthenticationScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Composables.AuthCompose.VerifyEmailStep2
import com.heetox.app.Composables.AuthCompose.VerifyemailStep1
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel


@Composable
fun verifyemailscreen(navController: NavHostController){

    val viewmodel : AuthenticationViewModel = hiltViewModel()

    val Localdata = viewmodel.Localdata.collectAsState()

    val SendOtpData = viewmodel.SendOtpData.collectAsState()

    val VerifyOtpData = viewmodel.VerifyOtpData.collectAsState()

    val context = LocalContext.current
    
    val token by rememberSaveable {
        mutableStateOf(Localdata.value?.Token)
    }
    val email by rememberSaveable {
        mutableStateOf(Localdata.value?.Email)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }


    var step by rememberSaveable{
        mutableStateOf(1)
    }

    var loading by rememberSaveable{
mutableStateOf(false)
    }




    if(loading == true){

        LoadingOverlay(isLoading = loading)

    }else{


        if(step == 1 ){


              Column {

                  VerifyemailStep1(viewmodel,email!!,navController)
              }


        }else if(step == 2){

            Column {

                VerifyEmailStep2(viewmodel = viewmodel, token = token!!, email = email!!, error)

            }
        }


    }



    LaunchedEffect(key1 = SendOtpData.value) {

        when(SendOtpData.value){

            is Resource.Error -> {

                loading = false
                error =  SendOtpData.value.error.toString()
                Toast.makeText(context,"Otp cannot be Sent Try Again",Toast.LENGTH_SHORT).show()


            }
            is Resource.Loading -> {

                loading = true
                error = "Sending OTP"

            }
            is Resource.Nothing -> {

                loading = false
                error = ""

            }
            is Resource.Success -> {

                 step = 2
                loading = false
                error = ""


            }

        }
        
    }







    LaunchedEffect(key1 = VerifyOtpData.value) {

         when(VerifyOtpData.value){
             is Resource.Error -> {

                 loading = false
                 error = VerifyOtpData.value.error.toString()

             }
             is Resource.Loading -> {

                 loading = true
                 error = "Loading"

             }
             is Resource.Nothing -> {

              loading = false
                 error = ""
             }
             is Resource.Success -> {

                 loading = false
                Toast.makeText(context,"Email Verified",Toast.LENGTH_SHORT).show()

                 viewmodel.savetoken(LocalStoredData(
                     true,
                     Localdata.value!!.Token,
                     VerifyOtpData.value.data!!.fullname,
                     VerifyOtpData.value.data!!.email,
                     VerifyOtpData.value.data!!.is_email_verified,
                     VerifyOtpData.value.data!!.age,
                     VerifyOtpData.value.data!!.phone_number,
                     VerifyOtpData.value.data!!.DOB,
                     VerifyOtpData.value.data!!.avatar,
                     VerifyOtpData.value.data!!.gender
                 ))

                 navController.navigate("profile")


             }
         }


    }






}