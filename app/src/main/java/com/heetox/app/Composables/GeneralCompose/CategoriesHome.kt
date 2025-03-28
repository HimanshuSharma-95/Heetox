package com.heetox.app.Composables.GeneralCompose

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay


@Composable
fun CategoriesHome(Productviewmodel : ProductsViewModel,navController: NavController){

    val CategoriesData = Productviewmodel.CategoriesData.collectAsState()
    val CategoriesList = CategoriesData.value.data?.categories

    var Loading by rememberSaveable {
        mutableStateOf(true)
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


    ){

        Text( text = "Categories",
            fontSize = 17.sp,
color = HeetoxDarkGray,
            modifier = Modifier
                .padding(15.dp,20.dp,0.dp,0.dp)
            )




      if(Loading || error == "Sorry Couldn't Load Categories"){

          var degree by remember { mutableStateOf(0) }

         if(Loading){
             Column(
                 modifier = Modifier
                     .fillMaxWidth()
                     .fillMaxHeight(),

                 verticalArrangement = Arrangement.Center,
                 horizontalAlignment = Alignment.CenterHorizontally

             ){


                 Image(painter = painterResource(id = R.drawable.loadingcircle), contentDescription = "",
                     modifier = Modifier
                         .padding(20.dp)
                         .size(30.dp)
                         .rotate(degree.toFloat()),
                 )

             }
         }else{

             Column(
                 modifier = Modifier
                     .fillMaxWidth()
                     .fillMaxHeight(),

                 verticalArrangement = Arrangement.Center,
                 horizontalAlignment = Alignment.CenterHorizontally

             ){


                 Text(text = error,
                     modifier = Modifier .padding(20.dp),
                     fontSize = 14.sp,
                     color = HeetoxDarkGray,
                     )

             }



         }

          LaunchedEffect(key1 = Unit) {
              while(true){
                  delay(5)
                  degree = (degree+5) % 360

              }
          }

      }else{

          LazyRow(

              modifier = Modifier
                  .padding(10.dp,10.dp,0.dp,20.dp)
//                  .background(HeetoxDarkGray)

          ){

              if(CategoriesList != null){

                  items(CategoriesList){
                          item ->

                      CategoryItem(item = item,navController,Productviewmodel)

                  }
              }

          }
      }


      }


    LaunchedEffect(key1 = CategoriesData.value ){

       when(CategoriesData.value){

           is Resource.Error -> {
               error = "Sorry Couldn't Load Categories"
               Loading = false
           }

           is Resource.Loading -> {
               error = " Just a second ;) "
               Loading = true
           }

           is Resource.Nothing -> {
               error = ""
           }

           is Resource.Success -> {
               Loading = false
               error = ""
           }
       }

    }


}






@Composable
fun CategoryItem(item : String,navController: NavController,productsViewModel: ProductsViewModel){


    Column(
modifier = Modifier
    .padding(horizontal = 5.dp)
//    .width(120.dp)
    .clip(RoundedCornerShape(30.dp))
    .background(HeetoxWhite)
    .clickable {

        navController.navigate("productlist/${item}/HOME")
        productsViewModel.getSubCategory(item)
        productsViewModel.clearAlternativeProductList()

    }
    .padding(horizontal = 20.dp, vertical = 8.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally

    ){

    Text(text = item,
      modifier = Modifier
   ,
    fontSize = 14.sp,


    )

    }


}












