package com.heetox.app.Composables.AuthCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
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

          Column( horizontalAlignment = Alignment.CenterHorizontally ){
              CircularProgressIndicator(
                  modifier = Modifier.size(30.dp),
                  color = HeetoxBrightGreen
              )
          }
        }
    }
}