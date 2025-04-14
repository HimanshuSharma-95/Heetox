package com.heetox.app.Screens.AuthenticationScreens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.heetox.app.Composables.AuthCompose.Input
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Authentication.UpdateProfileSend
import com.heetox.app.Model.Authentication.UpdateUserProfileImageSend
import com.heetox.app.R
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.flow.Flow


@Composable
fun UpdateProfileScreen(navHostController: NavHostController,
                        updateProfileImage:(UpdateUserProfileImageSend)->Unit,
                        removeProfileImage:(String)->Unit,
                        userData:LocalStoredData?,
                        updateProfile:(String,UpdateProfileSend)->Unit,
                        uiEvent:Flow<UiEvent>
                        ) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val token = userData?.Token ?: ""


    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imageUri = uri
        uri?.let {

            updateProfileImage(UpdateUserProfileImageSend(
                context = context,
                uri = it,
                token = token
            ))

        }
    }


    var name by rememberSaveable {
        mutableStateOf(userData!!.Name)
    }

    var email by rememberSaveable {
        mutableStateOf(userData!!.Email)
    }

    var phone by rememberSaveable {
        mutableStateOf(userData!!.Phone)
    }

    var dob by rememberSaveable {
        mutableStateOf(userData!!.Dob)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    var gender by rememberSaveable {
        mutableStateOf(userData!!.gender)
    }

    val genderoptions = listOf("male","female","other")

    var isgenderexpanded by rememberSaveable { mutableStateOf(false) }

    if(loading){

        LoadingOverlay(isLoading = loading)

    }else{


        val ImageUrl by rememberSaveable {
            mutableStateOf(userData!!.avatar)
        }


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
                    Text(text = "Update Profile",
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
                ){


                    if (ImageUrl == "") {

                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile Icon",
                            modifier = Modifier
                                .size(200.dp)
                        )

                    } else {

                        val imageRequest = ImageRequest.Builder(context)
                            .data(ImageUrl)
                            .crossfade(true)
                            .error(R.drawable.profile)
                            .transformations(CircleCropTransformation())
                            .listener(
                                onError = { request, throwable ->
//                                    Log.e(
//                                        "Profile Image",
//                                        "Error loading image: ${request.data} , $throwable"
//                                    )
                                }
                            )
                            .build()

                        AsyncImage(
                            model = imageRequest,
                            contentDescription = "Profile Image",
                            placeholder = painterResource(R.drawable.profile),
                            error = painterResource(R.drawable.profile),
                            modifier = Modifier
                                .padding(30.dp, 30.dp, 30.dp, 0.dp)
                                .size(200.dp)
                                .clip(CircleShape)


                        )


                    }


                    Spacer(modifier = Modifier
                        .height(10.dp)
                    )


                    Row {


                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = HeetoxDarkGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.padding(horizontal = 3.dp, vertical = 10.dp)
                        ) {
                            Text(text = "Update Image")
                        }



                    if(ImageUrl != ""){

                        Button(onClick = {
                            removeProfileImage(token)
                        }
                            ,
                            colors = ButtonDefaults.run {
                                buttonColors(
                                    contentColor = HeetoxDarkGreen,
                                    containerColor = HeetoxWhite
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(2.dp, HeetoxDarkGreen),
                            modifier = Modifier
                                .padding(horizontal = 3.dp , vertical = 10.dp)

                        ) {
                            Text(text = "Remove Image")
                        }
                    }




                }


                    Spacer(modifier = Modifier
                        .height(15.dp)
                    )



                 if(error != ""){
                     Text(text = error,
                         color = HeetoxDarkGreen,
                         style = TextStyle(
                             color = HeetoxDarkGreen,
                             fontSize = 15.sp
                         ),
                         modifier = Modifier
                             .padding(20.dp)
                     )
                 }


                    Input(label = "Name", value = name, onValueChange ={name = it} )


                    Input(label = "Email", value = email, onValueChange ={email = it} )


                    Input(label = "Dob YYYY-MM-DD", value = dob, onValueChange ={dob = it} )


                    OutlinedTextField(
                        value = gender,
                        onValueChange = { gender = it },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                            .width(300.dp)
                            .clickable { isgenderexpanded = true },
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
                            expanded =isgenderexpanded,
                            onDismissRequest = { isgenderexpanded = false },
                            modifier = Modifier
                                .width(300.dp)
                        ) {
                            genderoptions.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        gender = item
                                        isgenderexpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Input(label = "Phone", value = phone, onValueChange ={phone = it} )


                    val dobRegex = Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
                    val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")


                    Button(onClick = {

                        if(name == ""){
                            error = "Name cannot be empty"
                        }else if(!dobRegex.matches(dob)){
                            error = "DOB not in Correct Format"
                        }else if(gender == ""){
                            error = "Gender cannot be empty"
                        }else if(phone.length != 10){
                            error = "Phone no. is not Correct"
                        }else if(!emailRegex.matches(email)) {
                            error = "Email is not Correct"
                        }else {

                            error = ""

                            updateProfile(token,
                                UpdateProfileSend(
                                    email,
                                    name,
                                    phone,
                                    dob,
                                    gender
                                )
                            )


                        }

                    },
                        colors = ButtonDefaults.run {
                            buttonColors(
                                contentColor = Color.White,
                                containerColor = HeetoxDarkGreen
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .padding(50.dp)


                        ) {

                        Text(text = "Update Data")
                    }

                    Spacer(modifier = Modifier
                        .height(30.dp)
                    )



                }

            }


        }
    }




    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    loading = true
                    error = when (event.action) {
                        Action.UpdateUserImage -> "Updating image..."
                        Action.RemoveUserProfile -> "Removing image..."
                        Action.UpdateUserData -> "Updating profile..."
                        else -> ""
                    }
                }

                is UiEvent.Success -> {
                    loading = false
                    error = ""
                    when (event.action) {
                        Action.UpdateUserImage -> {
                            Toast.makeText(context, "Image updated", Toast.LENGTH_SHORT).show()
                            navHostController.navigate("profile")
                        }
                        Action.RemoveUserProfile -> {
                            Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show()
                            navHostController.navigate("profile")
                        }
                        Action.UpdateUserData -> {
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                            navHostController.navigate("profile")
                        }
                        else -> {}
                    }
                }

                is UiEvent.Error -> {
                    loading = false
                    error = ""
                    Toast.makeText(context, "Couldn't update. Try again.", Toast.LENGTH_SHORT).show()
                }

                UiEvent.Idle -> {
                    loading = false
                    error = ""
                }
            }
        }
    }

}