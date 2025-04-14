package com.heetox.app.Screens.AuthenticationScreens

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.Composables.AuthCompose.BaseHeading
import com.heetox.app.Composables.AuthCompose.DatePickerModal
import com.heetox.app.Composables.AuthCompose.Input
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Composables.AuthCompose.PasswordInput
import com.heetox.app.Composables.AuthCompose.formatDate
import com.heetox.app.Model.Authentication.LoginSend
import com.heetox.app.Model.Authentication.RegisterSend
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.Utils.rememberImeState
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.flow.Flow

@Composable
fun RegisterScreen(navController: NavHostController,
                   loginUser: (LoginSend) -> Unit,
                   registerUser: (RegisterSend) -> Unit,
                   uiEvent: Flow<UiEvent>
                   ){


Column(modifier = Modifier
       .background(HeetoxGreen)

   ) {

       RegisterInputs(
           navController, login = {
               loginUser(it)
           },
           register = {
               registerUser(it)
           },
           uiEvent = uiEvent
       )

    Spacer(modifier = Modifier
        .fillMaxSize()
        .background(HeetoxWhite)
        .imePadding()

    )
}

}




@Composable
fun RegisterInputs(navController: NavHostController, uiEvent: Flow<UiEvent>, login:(LoginSend)->Unit, register:(RegisterSend)->Unit){

    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var loading by rememberSaveable { mutableStateOf(false) }

    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val genderOptions = listOf("male","female","other")
    var isGenderExpanded by rememberSaveable { mutableStateOf(false) }


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

        Column( modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
        ){

            BaseHeading()

            Column( modifier = Modifier
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

                Column(horizontalAlignment = Alignment.CenterHorizontally  ){
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(0.dp,25.dp,0.dp,15.dp)
                    )
                  Row {
                      Text(text = "Already Have an account? ", modifier = Modifier,
                          fontSize = 15.sp,
                          color = HeetoxDarkGray
                      )

                      val annotatedString = buildAnnotatedString {

                          withStyle(style = SpanStyle(color = HeetoxDarkGreen,
                              fontSize = 15.sp,
                              fontWeight = FontWeight.Bold,

                          )
                          ) {
                              append("Login")
                          }
                          addStringAnnotation(
                              tag = "login",
                              annotation = "login",
                              start = 0,
                              end = "login".length
                          )
                      }
                      
                     Box(modifier = Modifier.padding(horizontal = 1.dp, vertical = 3.dp)){
                         ClickableText(text = annotatedString,
                             onClick = { offset ->
                                 annotatedString.getStringAnnotations(tag = "login", start = offset, end = offset)
                                     .firstOrNull()?.let {
                                         navController.navigate(it.item)
                                     }
                             }
                         )
                     }
                      
                  }

                    Text(text = message , modifier = Modifier .padding(15.dp) ,
                        style = TextStyle(
                            color = HeetoxDarkGreen,
                            fontSize = 15.sp
                        )
                    )




                    Input("name", name, { name = it }, KeyboardOptions.Default.copy(imeAction = ImeAction.Next))




//                    Input("D0B YYYY-MM-DD", dob, { dob = it.trimEnd() },KeyboardOptions.Default.copy(imeAction = ImeAction.Next) )


                    OutlinedTextField(
                        value = dob,
                        onValueChange = { },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                            .width(300.dp)
                            .clickable { showDatePicker = true },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        readOnly = true,
                        visualTransformation = VisualTransformation.None,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedLabelColor = HeetoxDarkGray,
                            unfocusedBorderColor = Color.Black,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = HeetoxDarkGray,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        label = { Text(text = "DOB") },
                        enabled = false

                    )



                    if (showDatePicker) {
                        DatePickerModal(
                            onDateSelected = { selectedDateMillis ->
                                selectedDateMillis?.let {
                                    dob = formatDate(it) // Format to YYYY-MM-DD
                                    println("Date Selected: $dob") // Debug Log
                                }
                                showDatePicker = false
                            },
                            onDismiss = {
                                println("DatePicker Dismissed") // Debug Log
                                showDatePicker = false
                            }
                        )
                    }



                    OutlinedTextField(
                           value = gender,
                           onValueChange = { gender = it },
                           modifier = Modifier
                               .padding(horizontal = 16.dp, vertical = 2.dp)
                               .width(300.dp)
                               .clickable { isGenderExpanded = true },
                           keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                           readOnly = true,
                           visualTransformation = VisualTransformation.None,
                           colors = OutlinedTextFieldDefaults.colors(
                               focusedTextColor = Color.Black,
                               unfocusedLabelColor = HeetoxDarkGray,
                               unfocusedBorderColor = Color.Black,
                               disabledBorderColor = MaterialTheme.colorScheme.outline,
                               disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                               disabledLabelColor = HeetoxDarkGray,
                               disabledTextColor = MaterialTheme.colorScheme.onSurface,
                           ),
                           label = { Text(text = "Gender") },
                           enabled = false

                       )
                   


Column(
){

    DropdownMenu(
        expanded = isGenderExpanded,
        onDismissRequest = { isGenderExpanded = false },
        modifier = Modifier
            .width(300.dp)
    ) {
        genderOptions.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = item) },
                onClick = {
                    gender = item
                    isGenderExpanded = false
                }
            )
        }
    }
}



                    Input("Phone no.", phone, { phone = it },KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone))

                    Input("Email", email, { email = it },KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email))

                    PasswordInput(
                        label = "Password",
                        password = password,
                        {password = it}
                    )



                    val dobregex = Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
                    val emailregex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

                    Button(onClick = {

                        if(name == ""){
                            message = "Name cannot be empty"
                        }else if(!dobregex.matches(dob)){
                            message = "DOB not in Correct Format"
                        }else if(gender == ""){
                            message = "Gender cannot be empty"
                        }else if(phone.length != 10){
                            message = "Phone no. is not Correct"
                        }else if(!emailregex.matches(email.trim())){
                            message = "Email is not Correct"
                        }else if(password.length < 8){
                            message = "Password Length is minimum 8 characters"
                        }
                        else{
                            message = ""

                            register(
                                RegisterSend(
                                    dob,
                                    email,
                                    name,
                                    password,
                                    phone,
                                    gender
                                ))
                        }
                    } ,
                        modifier = Modifier
                            .padding(50.dp)
                            .width(160.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = HeetoxDarkGreen
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )  {
                        Text(text = "Create Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                }

            }
        }


    }




    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    loading = true
                    message = when (event.action) {
                        Action.Register -> "Registering..."
                        Action.Login -> "Logging in..."
                        else -> "Processing..."
                    }
                }

                is UiEvent.Success -> {
                    loading = false
                    when (event.action) {
                        Action.Register -> {
                            message = "Registered successfully"
                            // Automatically trigger login after successful registration
                            login(LoginSend(email, password))
                        }

                        Action.Login -> {
                            message = "Login done"
                            Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }

                        else -> {
                            // Handle other actions if needed
                        }
                    }
                }

                is UiEvent.Error -> {
                    loading = false
                    message = event.message
                }

                UiEvent.Idle -> {
                    loading = false
                    message = ""
                }
            }
        }
    }


}

