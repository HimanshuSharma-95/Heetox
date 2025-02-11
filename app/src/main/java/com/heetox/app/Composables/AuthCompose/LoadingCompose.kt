package com.heetox.app.Composables.AuthCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.extended.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay


@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = androidx.compose.ui.Modifier
//                .shadow(50.dp)
                .fillMaxSize(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .background(HeetoxWhite)
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
//            CircularProgressIndicator(color = Color.White)
          Column( horizontalAlignment = Alignment.CenterHorizontally ){

              var degree by remember { mutableStateOf(0) }

                 LaunchedEffect(key1 = Unit) {
                     while(true){
                     delay(5)
                         degree = (degree+5) % 360

                 }
              }

              Image(painter = painterResource(id = com.heetox.app.R.drawable.loadingcircle), contentDescription = "",
                  modifier = Modifier
                      .size(50.dp)
                      .rotate(degree.toFloat())
                  )
          }
        }
    }
}