package com.heetox.app.Composables.GeneralCompose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.R
import com.heetox.app.ui.theme.HeetoxDarkGray
import kotlinx.coroutines.delay


//@Preview(showSystemUi = true )
@Composable
fun LogoAndSearchBar(
    navHostController: NavHostController,item : String?
){


    var index by rememberSaveable {
        mutableStateOf(0)
    }

    val itemlist = listOf("Lays","Frooti","Bhujia","Red Bull","Gummies")





    Row(

        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .height(56.dp)

        ,
        verticalAlignment = Alignment.CenterVertically

    ){


        Row(
            modifier = Modifier
                .weight(1f)
        ){
            Image(painter = painterResource(id = R.drawable.heetoxlogobg), contentDescription ="" ,

                modifier = androidx.compose.ui.Modifier
                    .padding(start = 10.dp)
                    .width(90.dp),
//                contentScale = ContentScale.Crop,
            )
        }



        Row(
            modifier = androidx.compose.ui.Modifier
                .padding(horizontal = 10.dp)
                .weight(3f)
//                .fillMaxWidth(1f)
                .height(48.dp)
                .shadow(3.dp, RoundedCornerShape(30.dp), true, HeetoxDarkGray, HeetoxDarkGray)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .clickable {
                    navHostController.navigate("search")
                }
            ,

         verticalAlignment = Alignment.CenterVertically
        ){



            if(item != null){


                Text(
                    text =  item ,
                    modifier = Modifier
                        .padding(20.dp, 10.dp, 0.dp, 10.dp),
                    color = HeetoxDarkGray,
                    fontSize = 15.sp
                )

            }else {


                Text(
                    text = "Search ",
                    modifier = Modifier
                        .padding(20.dp, 10.dp, 0.dp, 10.dp),
                    color = HeetoxDarkGray,
                    fontSize = 15.sp
                )



                AnimatedContent(
                    targetState = itemlist[index],
                    transitionSpec = {

                        (slideInVertically(initialOffsetY = { it }) + fadeIn(tween(durationMillis = 500))).togetherWith(
                            slideOutVertically(targetOffsetY = { -it }) + fadeOut(
                                tween(
                                    durationMillis = 500
                                )
                            )
                        ) using
                                SizeTransform(false)
                    }
                ) { targetPrompt ->

                    Text(
                        text = "\" ${targetPrompt} \" ",
                        color = HeetoxDarkGray,
                        fontSize = 15.sp

                    )
                }

            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(imageVector = Icons.Filled.Search, contentDescription = "",
tint = HeetoxDarkGray,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                )

    }



    LaunchedEffect(key1 = Unit) {

        while(true) {
            delay(2500)
            index = (index + 1) % itemlist.size
        }

        }


    }

}


