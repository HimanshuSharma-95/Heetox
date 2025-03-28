package com.heetox.app.Composables.AuthCompose

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.heetox.app.Composables.ProductCompose.LazyLikedProduct
import com.heetox.app.R
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun UserProfileCard(
    navController : NavHostController
){



    val viewmodel : AuthenticationViewModel = hiltViewModel()
    val UserData = viewmodel.Localdata.collectAsState()
    val UploadProfileimage = viewmodel.UploadProfileImageData.collectAsState()
    val context = LocalContext.current
    val ProductVM : ProductsViewModel = hiltViewModel()



    var email by rememberSaveable {
        mutableStateOf(UserData.value!!.Email )
    }
    var age by rememberSaveable {
        mutableStateOf(UserData.value!!.Age)
    }

    var gender by rememberSaveable {
        mutableStateOf(UserData.value!!.gender)
    }

    var phone by rememberSaveable {
        mutableStateOf(UserData.value!!.Phone)
    }

    var ImageUrl by rememberSaveable {
        mutableStateOf(UserData.value!!.avatar)
    }



    Column(
          modifier = Modifier
              .fillMaxSize()
              .background(HeetoxWhite)

      ) {


        //above
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(HeetoxWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LaunchedEffect(ImageUrl) {
                ImageUrl = UserData.value?.avatar ?: ""
            }


            if (ImageUrl == "") {

                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(200.dp)
                )
            } else {


                val imageRequest = ImageUrl.let {

                    ImageRequest.Builder(context)
                        .data(it)
                        .crossfade(true)
                        .error(R.drawable.profile)
                        .transformations(CircleCropTransformation())
                        .listener(
                            onError = { request, throwable ->
                                Log.e(
                                    "Profile Image",
                                    "Error loading image: ${request.data} , $throwable"
                                )
                            }
                        )
                        .build()

                }

                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Profile Image",
                    placeholder = painterResource(R.drawable.profile),
                    error = painterResource(R.drawable.profile),
                    modifier = Modifier
                        .padding(30.dp)
                        .size(200.dp)
                        .clip(CircleShape)

                )


            }



            Text(
                text = UserData.value!!.Name,
                modifier = Modifier,
                fontSize = 30.sp,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = HeetoxDarkGray
            )

        }



              Row(modifier = Modifier
//
                  .padding(top = 20.dp)
                  .fillMaxWidth()
                  ,
                  horizontalArrangement = Arrangement.End,
                  verticalAlignment = Alignment.CenterVertically

              ){

                  Button(onClick = {

                      navController.navigate("consumed")

                  },

                      colors = ButtonDefaults.buttonColors(
                          contentColor = Color.White,
                          containerColor = HeetoxDarkGreen,
                      ),
                      modifier = Modifier
                          .padding(horizontal = 10.dp)
                          .fillMaxWidth(),
                      shape = RoundedCornerShape(10.dp)

                  ) {

                      Text(text = "Consumed Products")
                  }


//                  Button(onClick = {
//
//                                   navController.navigate("updateprofile")
//
//                                   },
//
//                      colors = ButtonDefaults.buttonColors(
//                          contentColor = Color.White,
//                          containerColor = HeetoxDarkGray,
//                      ),
//                      modifier = Modifier
////                          .width(80.dp)
//
//                  ) {
//
//                      Text(text = "Edit Profile")
//                  }
              }



              //email
Box(
    modifier = Modifier
        .padding(horizontal = 10.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(15.dp))
        .background(Color.White)


){


   Column {
       Text(text = "Email",
           modifier = Modifier
               .padding(20.dp,15.dp,20.dp,0.dp),
           fontWeight = FontWeight.Bold,
           color = HeetoxDarkGray

       )
       Text(text = email,
           modifier = Modifier
               .padding(20.dp,5.dp,20.dp,15.dp),
           color = HeetoxDarkGray
       )

       if(UserData.value?.EmailStatus == false){

        Column(
            modifier = Modifier .fillMaxWidth() ,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(2.dp)
                .background(HeetoxLightGray)
                .fillMaxWidth(.8f)
            )

            Button(onClick = {

                             navController.navigate("verifyemail")
            },
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .width(200.dp),

                colors = ButtonDefaults.run {
                    buttonColors(
                        contentColor = HeetoxDarkGreen,
                        containerColor = Color.White
                    )},
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp, HeetoxDarkGreen),
            ) {

                Text(text = "Verify Email",
                    modifier = Modifier,
//                      .padding(20.dp,10.dp,20.dp,15.dp),
                    color = HeetoxDarkGreen,
                    fontWeight = FontWeight.Bold
                )

            }
        }

       }else{

           Box(modifier = Modifier
               .padding(horizontal = 20.dp)
               .height(2.dp)
               .background(HeetoxLightGray)
               .fillMaxWidth(.8f)
           )

         Row {
             Text(text = "Email Verified",

                 modifier = Modifier
                     .padding(20.dp,10.dp,5.dp,20.dp),
                 color = HeetoxDarkGreen,
                 fontWeight = FontWeight.Bold,
                 fontSize = 15.sp

             )


             Icon(
                 imageVector = Icons.Filled.MarkEmailRead,
                 contentDescription = "Attach Email Icon",
                 modifier = Modifier
                     .padding(vertical = 10.dp)
                     .size(22.dp),
                 tint = HeetoxDarkGreen

             )


         }


       }
   }

}



//           Row(){
               //2
               Box(
                   modifier = Modifier
                       .padding(10.dp, 0.dp, 10.dp, 10.dp)
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(15.dp))
                       .background(Color.White)

               ){

                   Column {
                       Text(text = "Age",
                           modifier = Modifier
                               .padding(20.dp,15.dp,20.dp,0.dp),
                           fontWeight = FontWeight.Bold,
                           color = HeetoxDarkGray

                       )
                       Text(text = age,
                           modifier = Modifier
                               .padding(20.dp,5.dp,20.dp,15.dp),
                           color = HeetoxDarkGray
                       )
                   }

               }





        //3
        Box(
            modifier = Modifier
                .padding(10.dp, 0.dp, 10.dp, 10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
            ,

            ){

            Column {
                Text(text = "Gender",
                    modifier = Modifier
                        .padding(20.dp,15.dp,20.dp,0.dp),
                    fontWeight = FontWeight.Bold,
                    color = HeetoxDarkGray

                )
                Text(text = gender,
                    modifier = Modifier
                        .padding(20.dp,5.dp,20.dp,15.dp),
                    color = HeetoxDarkGray
                )
            }

        }







               //4
               Box(
                   modifier = Modifier
                       .padding(10.dp, 0.dp, 10.dp, 10.dp)
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(15.dp))
                       .background(Color.White)
                   ,

               ){

                   Column {
                       Text(text = "Phone",
                           modifier = Modifier
                               .padding(20.dp,15.dp,20.dp,0.dp),
                           fontWeight = FontWeight.Bold,
                           color = HeetoxDarkGray

                       )
                       Text(text = phone,
                           modifier = Modifier
                               .padding(20.dp,5.dp,20.dp,15.dp),
                           color = HeetoxDarkGray
                       )
                   }

               }










        Spacer(modifier = Modifier.height(20.dp))



        UserData.value!!.Token?.let { LazyLikedProduct(authorization = it, ProductVM = ProductVM, navController = navController) }



              //col end
          }


}



