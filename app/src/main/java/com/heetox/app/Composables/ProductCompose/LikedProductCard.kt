package com.heetox.app.Composables.ProductCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.heetox.app.Model.Product.LikedProduct
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import kotlinx.coroutines.delay





@Composable
fun LazyLikedProduct(authorization : String,ProductVM: ProductsViewModel,navController: NavHostController){

    val LikedProductsData = ProductVM.LikedProductsData.collectAsState()
    val likedProductsList = LikedProductsData.value.data?.liked_products?.reversed() ?: emptyList()
    val listState = rememberLazyListState()

    var Loading by rememberSaveable {
        mutableStateOf(true)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }



    LaunchedEffect(key1 = Unit) {

        ProductVM.getlikedproducts(authorization)

    }


    Spacer(modifier = Modifier.height(2.dp))

    Text(text = "Liked Products",
        color =Color.DarkGray,
        fontWeight = FontWeight.W700,
        fontSize = 16.sp,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 0.dp)
        ,
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.height(1.dp))


    if(Loading || error.isNotEmpty()){

        var degree by remember { mutableStateOf(0) }

        Column(

            modifier = Modifier
                .padding(10.dp)
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
                    delay (5)
                    degree = (degree+5) % 360

                }
            }
        }


    }else {


        if(likedProductsList.size != 0){

        LazyRow(

            contentPadding = PaddingValues(8.dp),
            state = listState,
                    modifier = Modifier.fillMaxWidth()
        ) {

            items(likedProductsList, key = { item -> item.liked_product.product_barcode }) {

                    item ->

                LikedProductCard(data = item) {

                    navController.navigate("productdetails/${item.liked_product.product_barcode}")

                }

            }


        }
        }else{

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)

                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Image(painter = painterResource(id = R.drawable.search),
                    contentDescription ="searching image",
                    modifier = Modifier
                        .size(50.dp)
                    )

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "No Liked Products Yet!",
                    fontSize = 14.sp,
                    color = HeetoxDarkGray,
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(text = "Explore!",
                    fontSize = 14.sp,
                    color = HeetoxDarkGreen,
                    modifier = Modifier
                        .clickable { navController.navigate("home"){
                            popUpTo("home"){
                                inclusive = true
                            }
                        } }

                )


            }

        }

    }


    LaunchedEffect(key1 = LikedProductsData.value) {

        when(LikedProductsData.value){

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
fun LikedProductCard(data : LikedProduct, onClick :(barcode : String)-> Unit ){



    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(20.dp), clip = false, HeetoxDarkGray, HeetoxDarkGray)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable {
                onClick(data.liked_product.product_barcode)
            }

    ){

        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .width(150.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ){


            val imageid =
                if(data.ratings.product_nutriscore == "A"){
                    R.drawable.arating
                }else if(data.ratings.product_nutriscore == "B"){
                    R.drawable.brating
                }else if(data.ratings.product_nutriscore == "C"){
                    R.drawable.crating
                }else if(data.ratings.product_nutriscore == "D"){
                    R.drawable.drating
                }else if(data.ratings.product_nutriscore == "E"){
                    R.drawable.erating
                }else{
                    "No rating"
                }



            AsyncImage(model = data.liked_product.product_front_image, contentDescription = "",
                modifier = Modifier
                    .height(100.dp)
            )





            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(80.dp),

                verticalArrangement = Arrangement.Center
            ){
                Text(text = data.liked_product.product_name,
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