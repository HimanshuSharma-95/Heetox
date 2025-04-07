package com.heetox.app.Composables.ProductCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.heetox.app.Composables.GeneralCompose.LogoAndSearchBar
import com.heetox.app.Model.Product.MostScannedResponse
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay


@Composable
fun MostScannedProductsSix(navController : NavHostController,ProductVM : ProductsViewModel){


    var productdata = ProductVM.MostScannedProductsData.collectAsState()
    var productlist = productdata.value.data
    val firstsixproduct = productlist?.take(6)


    var Loading by rememberSaveable {
        mutableStateOf(true)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
    ){
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
        ){
            Text(text = "Most Scanned",
                color =Color.DarkGray,
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 5.dp)
                ,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }


  if(Loading || error.isNotEmpty()){

      var degree by remember { mutableStateOf(0) }

          Column(

              modifier = Modifier
                  .padding(horizontal = 20.dp)
                  .fillMaxWidth()
                  .height(200.dp)
                  .clip(RoundedCornerShape(20.dp))
                  .background(Color.White)
              ,
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally

          ){


             if (Loading){
                 Image(painter = painterResource(id = R.drawable.loadingcircle), contentDescription = "",
                     modifier = Modifier
                         .size(30.dp)
                         .rotate(degree.toFloat()),
                 )
             }else{

                 Text(text = error,
                     fontSize = 14.sp,
                     color = HeetoxDarkGray,
                 )

             }



          LaunchedEffect(key1 = Unit) {
              while(true){
                  delay(5)
                  degree = (degree+5) % 360

              }
          }
      }



  }else{

      Column(modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 10.dp)  ,
          horizontalAlignment = Alignment.CenterHorizontally

      ) {
        if (firstsixproduct != null) {
            if(firstsixproduct.size != 0){
                for (rowIndex in 0 until 3) { // 3 rows
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        for (colIndex in 0 until 2) { // 2 columns
                            val itemIndex = rowIndex * 2 + colIndex
                            if (itemIndex <firstsixproduct!!.size) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    MostScannedProductsCard(firstsixproduct[itemIndex]){

                                        navController.navigate("productdetails/${firstsixproduct[itemIndex].product_barcode}")

                                    }
                                }
                            }else{
                                Spacer(modifier = Modifier
                                    .height(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }


       Column(
           modifier = Modifier
               .fillMaxWidth()
           ,
           horizontalAlignment = Alignment.End

       ){
           Button(onClick = {
               navController.navigate("mostscanned")
           },
               colors = ButtonDefaults.buttonColors(
                   contentColor = HeetoxDarkGreen,
                   containerColor = HeetoxWhite
               )
           ) {

               Text(text = "Explore more"
               )

               Icon(imageVector = Icons.Filled.KeyboardDoubleArrowRight, contentDescription ="Arrow" )

           }
       }

      }

  }



    LaunchedEffect(key1 = productdata.value) {

        when(productdata.value){

            is Resource.Error -> {
                error = "oops! Couldn't Load :("
                Loading = false
            }

            is Resource.Loading -> {
                error = " Just a second ;) "
                Loading = true
            }

            is Resource.Nothing -> {

            }

            is Resource.Success -> {
                Loading = false
                error = ""
            }
        }


    }

}





@Composable
fun MostScannedProductsAll(data : List<MostScannedResponse>,navController: NavHostController) {


    val listState = rememberLazyGridState()



    Spacer(modifier = Modifier.height(2.dp))
    LogoAndSearchBar(navHostController = navController,"Most scanned")
    Spacer(modifier = Modifier.height(1.dp))

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(HeetoxLightGray)
    )


    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Set the number of columns to 2
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize(),
        state = listState

    ){




        items(data,key = {data -> data.product_barcode}){

           data ->
            MostScannedProductsCard(data = data ){

                navController.navigate("productdetails/${data.product_barcode}")

            }

        }

    }

}
















@Composable
fun MostScannedProductsCard( data : MostScannedResponse , onClick :(barcode : String)-> Unit ){



   Box(
          modifier = Modifier
              .padding(4.dp)
              .fillMaxWidth()
              .shadow(5.dp, RoundedCornerShape(20.dp), clip = false, HeetoxDarkGray, HeetoxDarkGray)
              .clip(RoundedCornerShape(20.dp))
              .background(Color.White)
              .clickable {
                  onClick(data.product_barcode)
              }

      ){

          Column(
              modifier = Modifier
                  .padding(horizontal = 15.dp, vertical = 10.dp)
                  .fillMaxWidth()
              ,
              horizontalAlignment = Alignment.CenterHorizontally
          ){


              val imageid =
                  if(data.product_nutriscore == "A"){
                      R.drawable.arating
                  }else if(data.product_nutriscore == "B"){
                      R.drawable.brating
                  }else if(data.product_nutriscore == "C"){
                      R.drawable.crating
                  }else if(data.product_nutriscore == "D"){
                      R.drawable.drating
                  }else if(data.product_nutriscore == "E"){
                      R.drawable.erating
                  }else{
                      "No rating"
                  }



                  AsyncImage(model = data.product_front_image, contentDescription = "",
                      modifier = Modifier
                          .height(100.dp)
                  )





              Column (
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.height(80.dp),

                  verticalArrangement = Arrangement.Center
              ){
                  Text(text = data.product_name,
                      modifier = Modifier,
                      fontSize = 14.sp,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis

                      )

                  Spacer(modifier = Modifier
                      .height(15.dp)
                  )





                  if(imageid == "No rating"){
                      Text(text = "No Rating")
                  }else {

                      AsyncImage(
                          model = imageid, contentDescription = "",
                          modifier = Modifier
                              .width(110.dp)
                      )
                  }
              }



          }
      }


}



















