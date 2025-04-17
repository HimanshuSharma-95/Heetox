package com.heetox.app.Composables.GeneralCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.heetox.app.Model.Product.CategoriesResponse
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.Resource
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


@Composable
fun CategoriesHome(categoriesData : Resource<CategoriesResponse>, navController: NavController,uiEvents:Flow<UiEvent>){

    val categoriesList = categoriesData.data?.categories

    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }


    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
//            .height(110.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(50.dp, RoundedCornerShape(20.dp), true, HeetoxDarkGray, HeetoxDarkGray)
            .background(Color.White)


    ) {

        Text(
            text = "Categories",
            fontSize = 17.sp,
            color = HeetoxDarkGray,
            modifier = Modifier
                .padding(15.dp, 20.dp, 0.dp, 0.dp)
        )




        if (loading || error.isNotEmpty()) {

            var degree by remember { mutableIntStateOf(0) }

            if (loading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(20.dp)
                    ,

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = HeetoxBrightGreen
                    )

                }
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(20.dp),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {


                    Text(
                        text = error,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 14.sp,
                        color = HeetoxDarkGray,
                    )

                }


            }

            LaunchedEffect(key1 = Unit) {
                while (true) {
                    delay(5)
                    degree = (degree + 5) % 360

                }
            }

        } else {

            LazyRow(

                modifier = Modifier
                    .padding(10.dp, 10.dp, 0.dp, 20.dp)
//                  .background(HeetoxDarkGray)

            ) {

                if (categoriesList != null) {

                    items(categoriesList) { item ->

                        CategoryItem(item = item, navController)

                    }
                }

            }
        }


    }


    LaunchedEffect(Unit){

        uiEvents.collect{ event ->
            when (event) {
                is UiEvent.Loading -> {
                    if (event.action == Action.MainCategories) {
                        loading = true
                        error = ""
                    }
                }

                is UiEvent.Success -> {
                    if (event.action == Action.MainCategories) {
                        loading = false
                        error = ""
                    }
                }

                is UiEvent.Error -> {
                    if (event.action == Action.MainCategories) {
                        loading = false
                        error = event.message
                    }
                }

                UiEvent.Idle -> Unit
            }

        }

    }

}


@Composable
fun CategoryItem(item: String, navController: NavController) {


    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
//    .width(120.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(HeetoxWhite)
            .clickable {
                navController.navigate("productlist/${item}/HOME")
            }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = item,
            modifier = Modifier,
            fontSize = 14.sp,


            )

    }


}












