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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.heetox.app.Composables.AuthCompose.Input
import com.heetox.app.Composables.AuthCompose.LoadingOverlay
import com.heetox.app.Model.Authentication.UpdateProfileSend
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun updateprofilescreen(navHostController: NavHostController){

    val viewmodel : AuthenticationViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    val Localdata = viewmodel.Localdata.collectAsState()
    val UpdateProfileStatus = viewmodel.UpdateUserData.collectAsState()
    val UplaodImage = viewmodel.UploadProfileImageData.collectAsState()
    val RemoveImage = viewmodel.RemoveProfileImage.collectAsState()
    val context = LocalContext.current
    val token = Localdata.value?.Token


    var imageUri by remember { mutableStateOf<Uri?>(null) }



    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imageUri = uri
        uri?.let {
            if (token != null) {
                viewmodel.updateprofileimage(context, token, it)
            }
        }
    }

//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = { uri: Uri? ->
//            imageUri = uri
//            uri?.let {
//                if (token != null) {
//                    imageUri?.let { it1 -> viewmodel.updateprofileimage(context,token, it1) }
//                }
//            }
//        }
//    )

    var name by rememberSaveable {
        mutableStateOf(Localdata.value!!.Name)
    }

    var email by rememberSaveable {
        mutableStateOf(Localdata.value!!.Email)
    }

    var phone by rememberSaveable {
        mutableStateOf(Localdata.value!!.Phone)
    }

    var dob by rememberSaveable {
        mutableStateOf(Localdata.value!!.Dob)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    var gender by rememberSaveable {
        mutableStateOf(Localdata.value!!.gender)
    }

    val genderoptions = listOf("male","female","other")

    var isgenderexpanded by rememberSaveable { mutableStateOf(false) }

    if(loading){

        LoadingOverlay(isLoading = loading)

    }else{


        val ImageUrl by rememberSaveable {
            mutableStateOf(Localdata.value!!.avatar)
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

//                    Button(onClick = { launcher.launch("image/*") }
//                        ,
//                        colors = ButtonDefaults.run {
//                            buttonColors(
//                                contentColor = Color.White,
//                                containerColor = HeetoxDarkGreen
//                            )
//                        },
//                        shape = RoundedCornerShape(10.dp),
////                        border = BorderStroke(2.dp, HeetoxDarkGreen),
//                        modifier = Modifier
//                            .padding(horizontal = 3.dp , vertical = 10.dp)
//                    ) {
//                        Text(text = "Update Image")
//                    }


                    if(ImageUrl != ""){
                        Button(onClick = {

                            if (token != null) {
                                viewmodel.removeprofileimage(token)
                            }

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


                    val dobregex = Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
                    val emailregex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")


                    Button(onClick = {

                        val token = Localdata.value?.Token

                        if(name == ""){
                            error = "Name cannot be empty"
                        }else if(!dobregex.matches(dob)){
                            error = "DOB not in Correct Format"
                        }else if(gender == ""){
                            error = "Gender cannot be empty"
                        }else if(phone.length != 10){
                            error = "Phone no. is not Correct"
                        }else if(!emailregex.matches(email)) {
                            error = "Email is not Correct"
                        }else {

                            error = ""

                            if (token != null) {
                                viewmodel.updateuser(token,
                                    UpdateProfileSend(
                                        email,
                                        name,
                                        phone,
                                        dob,
                                        gender
                                    )
                                )
                            }


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


    LaunchedEffect(key1 = UplaodImage.value ) {

        when(UplaodImage.value){

            is Resource.Error ->{

                Toast.makeText(context,"Couldn't Update Try again",Toast.LENGTH_SHORT).show()
                error = ""
                loading = false

            }

            is Resource.Loading -> {
                error = "updating.."
               loading = true
            }

            is Resource.Nothing -> {
                error = ""
            }

            is Resource.Success -> {

                loading = false
                error = ""
                Toast.makeText(context,"Image updated",Toast.LENGTH_SHORT).show()

            }
        }

    }


    LaunchedEffect(key1 = RemoveImage.value) {

        when(RemoveImage.value){
            is Resource.Error -> {
                Toast.makeText(context,"Couldn't Update Try again",Toast.LENGTH_SHORT).show()
                error = ""
                loading = false
            }

            is Resource.Loading -> {
                error = "removing.."
                loading = true
            }
            is Resource.Nothing -> {
                error = ""
            }
            is Resource.Success -> {

                loading = false
                error = ""
                Toast.makeText(context,"Image Removed",Toast.LENGTH_SHORT).show()

            }
        }



    }






    LaunchedEffect(key1 = UpdateProfileStatus.value) {

        when(UpdateProfileStatus.value){

            is Resource.Error -> {
                Toast.makeText(context,"Couldn't Update Try again",Toast.LENGTH_SHORT).show()
                error = ""
                loading = false
            }
            is Resource.Loading -> {
                error = "Updating..."
                loading = true
            }
            is Resource.Nothing -> {
            loading = false
            }
            is Resource.Success -> {

                navHostController.navigate("profile")
                Toast.makeText(context,"Profile Updated",Toast.LENGTH_SHORT).show()
                loading = false
                error = "Profile updated"

            }

        }
    }


}