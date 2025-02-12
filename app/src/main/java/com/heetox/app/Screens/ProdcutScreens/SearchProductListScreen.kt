package com.heetox.app.Screens.ProdcutScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.Composables.GeneralCompose.LogoAndSearchBar
import com.heetox.app.Composables.ProductCompose.ProductCard
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay



//list after searching

@Composable
fun SearchProductListScreen(search: String ,ProductVM: ProductsViewModel,AuthVM : AuthenticationViewModel,navController: NavHostController){


    BackHandler {
        navController.navigate("searchscreen")
    }


    val context = LocalContext.current
    val AllProducts = ProductVM.SearchData.collectAsState()

    val dataList  = AllProducts.value.data
    val UserData = AuthVM.Localdata.collectAsState()



    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }


    var isApiCalled by rememberSaveable {
        mutableStateOf(false)
    }


    val currentSearch = rememberUpdatedState(search)
    val currentToken = rememberUpdatedState(UserData.value?.Token)




    LaunchedEffect(key1 = Unit){


        val token = currentToken.value ?: ""



        if(!isApiCalled) {
            ProductVM.searchproducts(
                currentSearch.value,
                token
            )

            isApiCalled = true

        }



    }






    if(loading || error.isNotEmpty()){


        var degree by rememberSaveable {
            mutableStateOf(0)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HeetoxWhite),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            if(loading){

                Image(painter = painterResource(id = R.drawable.loadingcircle), contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .rotate(degree.toFloat()),
                )


                LaunchedEffect(key1 = Unit) {
                    while(true){
                        delay(5)
                        degree = (degree+5) % 360

                    }
                }

            }else{


                Text(text = "oops! Couldn't Load Products",
                    fontSize = 15.sp,
                    color = HeetoxDarkGray
                )



            }

        }


    }else{



        val listState = rememberLazyListState()



        Column(
            modifier = Modifier
                .padding(bottom = 0.dp)
                .fillMaxSize()
                .background(HeetoxWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ){


            if (dataList != null) {
                if(dataList.size != 0){


                    LazyColumn(
                        state = listState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){

                        item(
                        ){
                            Spacer(modifier = Modifier.height(10.dp))
                            LogoAndSearchBar(navHostController = navController,search)
                            Spacer(modifier = Modifier.height(4.dp))
                        }


                        items(dataList, key = {data -> data.product_barcode}){

                                data ->

                            var isLiked by remember { mutableStateOf(data.isliked) }
                            var likeCount by remember { mutableStateOf(data.likesCount) }


                            ProductCard(
                                data = data,
                                ProductVM = ProductVM,
                                UserData = UserData,
                                context = context,
                                navController,
                                isLiked = isLiked,
                                likeCount = likeCount ,
                                onLikeCountChange = { likeCount = it },
                                onLikeChange = { isLiked = it }
                            )

                        }


                    }


                }else{

                    Column(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                        .background(HeetoxWhite),
                    ){

                            Spacer(modifier = Modifier.height(10.dp))
                            LogoAndSearchBar(navHostController = navController,search)
                            Spacer(modifier = Modifier.height(4.dp))



                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center

                        ){

                            Text(text = "oops! No product Matched",
                                fontSize = 15.sp,
                                color = HeetoxDarkGray
                            )


                        }


                    }


                }
            }


        }







    }





    LaunchedEffect(key1 = AllProducts.value) {

        when(AllProducts.value){

            is Resource.Error -> {
                error = "oops! Couldn't Load :("
                loading = false
            }

            is Resource.Loading -> {
                error = " Just a second ;) "
                loading = true
            }

            is Resource.Nothing -> {
            }

            is Resource.Success -> {
                loading = false
                error = ""
            }
        }




    }






    }