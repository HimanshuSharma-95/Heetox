package com.heetox.app.Composables.AuthCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ui.theme.HeetoxWhite



@Composable
fun ProfileInputs(navController: NavHostController , viewmodel: AuthenticationViewModel){


    var UserData = viewmodel.Localdata.collectAsState()
    val scrollState = rememberScrollState()

    Box ( modifier = Modifier

    ){


        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ){


            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
            ) {




            if(UserData.value == null){

                LoginAndRegisterCard(navController)

            }else{
                UserProfileCard(navController)
            }


            }

        }
    }


    }



