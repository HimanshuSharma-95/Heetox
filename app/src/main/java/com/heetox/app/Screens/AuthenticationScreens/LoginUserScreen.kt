package com.heetox.app.Screens.AuthenticationScreens

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.Composables.AuthCompose.BaseHeading
import com.heetox.app.Composables.AuthCompose.Input
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Composables.AuthCompose.PasswordInput
import com.heetox.app.Model.Authentication.LoginSend
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.Utils.rememberImeState
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.flow.Flow

@Composable
fun LoginScreen(navController: NavHostController,
                uiEvent: Flow<UiEvent>,
                loginUser: (LoginSend) -> Unit){

Column(
    modifier = Modifier
        .background(HeetoxGreen)
){
    LoginInputs(navController,
        uiEvent,
        login = {
        loginUser(it)
    }
    )

    Spacer(modifier = Modifier
        .fillMaxSize()
        .background(HeetoxWhite)
    ) }
}


@Composable
fun LoginInputs(navController: NavHostController, uiEvent: Flow<UiEvent>, login:(LoginSend)->Unit){

    var error by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var loading by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current


if(loading){

    BaseHeading()
    LoadingOverlay(isLoading = loading)

}else{


    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {

        if(imeState.value){
            scrollState.animateScrollTo(scrollState.maxValue-300, tween(1000)
            )
        }
    }

    Column(
        modifier = androidx.compose.ui.Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ){
        BaseHeading()
        Column {

            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {


                Column(horizontalAlignment = Alignment.CenterHorizontally  ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.W900,
                        modifier = androidx.compose.ui.Modifier
                            .padding(0.dp, 35.dp, 0.dp, 15.dp)

                    )
                    Row {
                        Text(text = "Don't Have an account? ", modifier = Modifier,
                            fontSize = 15.sp,
                            color = HeetoxDarkGray
                        )

                        val annotatedString = buildAnnotatedString {

                            withStyle(style = SpanStyle(color = HeetoxDarkGreen,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,

                                )
                            ) {
                                append("Register")
                            }
                            addStringAnnotation(
                                tag = "register",
                                annotation = "register",
                                start = 0,
                                end = "register".length
                            )
                        }

                        Box(modifier = Modifier.padding(horizontal = 1.dp, vertical = 3.dp)){
                            ClickableText(text = annotatedString,
                                onClick = { offset ->
                                    annotatedString.getStringAnnotations(tag = "register", start = offset, end = offset)
                                        .firstOrNull()?.let {
                                            navController.navigate(it.item)
                                        }
                                }
                            )
                        }

                    }

                    Text( text = error, modifier = androidx.compose.ui.Modifier.padding(15.dp),
                        style = TextStyle(
                            color = HeetoxDarkGreen,
                            fontSize = 15.sp
                        )
                    )



                    Input(
                        "Email", email, { email = it },
                        KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        )
                    )

                    PasswordInput(
                        label = "Password",
                        password = password,
                        { password = it }
                    )

                    Row {

                        val annotatedString = buildAnnotatedString {

                            withStyle(style = SpanStyle(color = HeetoxDarkGreen,
                                fontSize = 15.sp,

                                )
                            ) {
                                append("Forgot Password")
                            }
                            addStringAnnotation(
                                tag = "forgotpassword",
                                annotation = "forgotpassword",
                                start = 0,
                                end = "forgotpassword".length
                            )
                        }

                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            .width(300.dp),
                            horizontalAlignment = Alignment.End


                            ){
                            ClickableText(text = annotatedString,
                                onClick = { offset ->
                                    annotatedString.getStringAnnotations(tag = "forgotpassword", start = offset, end = offset)
                                        .firstOrNull()?.let {
                                            navController.navigate(it.item)
                                        }
                                }
                            )
                        }



                    }



                    val emailregex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

                    Button(onClick = {

                        if(!emailregex.matches(email.trim())){
                            error = "Email is not Correct"
                        }else if(password.length < 8){
                            error = "Password not Correct"
                        }
                        else{

                            error = ""
                            login(LoginSend(
                                email,password
                            ))

                        }

                    } ,
                        modifier = androidx.compose.ui.Modifier
                            .padding(70.dp)
                            .width(160.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = HeetoxDarkGreen
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )  {
                        Text(text = "Login",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }}}}}



}


    LaunchedEffect(Unit) {
       uiEvent.collect { event ->
            when (event) {
                is UiEvent.Error -> {
                    if (event.action == Action.Login) {
                        loading = false
                        error = event.message
                    }
                }
                UiEvent.Idle -> {}
                is UiEvent.Loading -> {
                    if (event.action == Action.Login) {
                        loading = true
                        error = "Logging in..."
                    }
                }
                is UiEvent.Success -> {
                    if (event.action == Action.Login) {
                        loading = false
                        error = ""
                        Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            }}}

}