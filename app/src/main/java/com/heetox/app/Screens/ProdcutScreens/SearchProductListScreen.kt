package com.heetox.app.Screens.ProdcutScreens

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.heetox.app.Composables.GeneralCompose.LogoAndSearchBar
import com.heetox.app.Composables.ProductCompose.ProductCard
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ViewModel.ProductsVM.ProductListViewModel
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite


//list after searching
@Composable
fun SearchProductListScreen(search: String, navController: NavHostController,userData: LocalStoredData? ){


    val productListVM : ProductListViewModel = hiltViewModel()

    val context = LocalContext.current
    val allProducts = productListVM.searchData.collectAsState()

    val dataList  = allProducts.value.data

    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }


    val currentSearch = rememberUpdatedState(search)
    val currentToken = rememberUpdatedState(userData?.Token)


    LaunchedEffect(key1 = Unit){

        val token = currentToken.value ?: ""

            productListVM.searchProducts(
                currentSearch.value,
                token
            )

    }




    if(loading || error.isNotEmpty()){


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HeetoxWhite),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            if(loading){

                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = HeetoxBrightGreen
                )

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
//                                ProductVM = ProductVM,
                                 userData = userData,
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




    LaunchedEffect(Unit) {
        productListVM.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    if (event.action == Action.SearchProduct) {
                        loading = true
                        error = "Just a second ;)"
                    }
                }

                is UiEvent.Success -> {
                    if (event.action == Action.SearchProduct) {
                        loading = false
                        error = ""
                    }
                }

                is UiEvent.Error -> {
                    if (event.action == Action.SearchProduct) {
                        loading = false
                        error = event.message
                    }
                }
                UiEvent.Idle -> Unit
            }
        }
    }

}