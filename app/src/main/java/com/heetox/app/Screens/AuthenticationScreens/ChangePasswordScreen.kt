package com.heetox.app.Screens.AuthenticationScreens

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Composables.AuthCompose.PasswordInput
import com.heetox.app.Model.Authentication.ChangePasswordSend
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.flow.Flow


@Composable
fun ChangePasswordScreen(navController : NavHostController,
                         userData : LocalStoredData?,
                         changePassword: (String,ChangePasswordSend) -> Unit,
                         uiEvent:Flow<UiEvent>

){


    val context = LocalContext.current
    val scrollState = rememberScrollState()


    var oldPassword by rememberSaveable {
        mutableStateOf("")
    }


    var newPassword by rememberSaveable {
        mutableStateOf("")
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    val token by rememberSaveable {
        mutableStateOf(userData!!.Token)
    }


if(loading){

    LoadingOverlay(isLoading = loading)

}else{


   Box(modifier = Modifier
       .fillMaxSize()
       .background(HeetoxWhite)
   ){

       Column(
           modifier = Modifier
               .background(HeetoxGreen)
               .verticalScroll(scrollState, true)
       ){


           Column(

               modifier = Modifier
                   .height(100.dp)
                   .fillMaxWidth(),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally


           ){
               Text(text = "Change Password",
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
               horizontalAlignment = Alignment.CenterHorizontally
           ){




                   Spacer(modifier = Modifier
                       .height(130.dp)
                   )

                   Text(text = error,
                       fontSize = 15.sp,
                       color = HeetoxDarkGreen
                   )

                   Spacer(modifier = Modifier
                       .height(32.dp)
                   )


                   PasswordInput(label ="Old Password" , password = oldPassword , onPasswordChange ={oldPassword = it} )

                   PasswordInput(label ="New Password" , password = newPassword , onPasswordChange ={newPassword = it} )


                   Spacer(modifier = Modifier
                       .height(60.dp)
                   )

                   Button(onClick = {

                       if(oldPassword.length < 8 || newPassword.length < 8){
                           error = "Password minimum of 8 Characters"
                       }else{

                           token?.let {
                               changePassword(
                                   it, ChangePasswordSend(
                                       oldPassword,
                                       newPassword
                                   )
                               )
                           }

                       }

                   },
                       modifier = Modifier
                           .padding(15.dp)
                           .width(130.dp)
                           .height(40.dp)
                       ,
                       colors = ButtonDefaults.buttonColors(
                           contentColor = Color.White,
                           containerColor = HeetoxDarkGreen
                       ),
                       shape = RoundedCornerShape(10.dp)
                   ) {

                       Text(text = "Change")


                   }


               Spacer(modifier = Modifier
                   .height(130.dp)
               )




               }




       }

   }





}


    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    if (event.action == Action.ChangePassword) {
                        loading = true
                        error = "Changing password..."
                    }
                }

                is UiEvent.Success -> {
                    if (event.action == Action.ChangePassword) {
                        loading = false
                        error = "Password Changed"
                        Toast.makeText(context, "Password Changed", Toast.LENGTH_SHORT).show()
                        navController.navigate("profile")
                    }
                }

                is UiEvent.Error -> {
                    if (event.action == Action.ChangePassword) {
                        loading = false
                        error = event.message
//                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is UiEvent.Idle -> {}
            }
        }
    }
}