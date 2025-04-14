package com.heetox.app.Screens.ProdcutScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.heetox.app.Composables.ProductCompose.ProductCard
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.ProductsVM.ProductListViewModel
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite


@SuppressLint("MutableCollectionMutableState")
@Composable
fun BetterAlternativesScreen(
    subCategory :String,
    navController: NavHostController,
    userData : LocalStoredData?
){

    val productListVM: ProductListViewModel = hiltViewModel()

    val context = LocalContext.current

    val token = userData?.Token ?: ""

    val allProducts = productListVM.alternativeProductData.collectAsState()
    var dataList by remember { mutableStateOf(allProducts.value.data) }


    //states for sub categories Product list
    var loadingSubcategoriesList by rememberSaveable { mutableStateOf(true) }
    var errorSubcategoriesList by rememberSaveable { mutableStateOf("") }


    // Swipe to refresh functionality
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
//        ProductVM.getSubCategory(category) // Trigger refresh
        productListVM.getAlternativeProducts(subCategory,token)

    }) {

        Column(
            modifier = Modifier
                .padding(bottom = 0.dp)
                .fillMaxSize()
                .background(HeetoxWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                item {

                    Spacer(modifier = Modifier.height(15.dp))

                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
//                            .fillMaxWidth()
//                            .clip(RoundedCornerShape(15.dp))
//                            .background(Color.White)
                            .padding(4.dp),
                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                "Better Alternatives",
                                fontSize = 16.sp,
                                color = HeetoxDarkGray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(HeetoxLightGray)
                                    .padding(10.dp)
                            ){
                                Text(
                                    subCategory.replace("_"," "),
                                    fontSize = 16.sp,
                                    color = HeetoxDarkGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }

                }


                // Loading state for subcategory product list
                if (loadingSubcategoriesList || errorSubcategoriesList.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(top = 200.dp)
                                .fillMaxSize()
                                .background(HeetoxWhite),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (loadingSubcategoriesList) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(30.dp),
                                    color = HeetoxBrightGreen
                                )
                            } else {
                                Text(
                                    text = "Oops! Couldn't Load Products",
                                    fontSize = 15.sp,
                                    color = HeetoxDarkGray
                                )
                            }
                        }
                    }
                } else {
                    // If dataList is empty, show a message
                    if (dataList?.isNotEmpty() == true) {

                        if (dataList.isNullOrEmpty()) {
                            item {
                                Text(
                                    text = "No products available",
                                    fontSize = 16.sp,
                                    color = HeetoxDarkGray,
                                    modifier = Modifier.padding(top = 300.dp)
                                )
                            }
                        } else {
                            // Display product cards
                            items(
                                dataList!!.toList(),
                                key = { data -> data.product_barcode }) { data ->
                                var isLiked by remember { mutableStateOf(data.isliked) }
                                var likeCount by remember { mutableIntStateOf(data.likesCount) }

                                ProductCard(
                                    data = data,
//                                    ProductVM = ProductVM,
                                    userData = userData,
                                    context = context,
                                    navController = navController,
                                    isLiked = isLiked,
                                    likeCount = likeCount,
                                    onLikeCountChange = { likeCount = it },
                                    onLikeChange = { isLiked = it }
                                )
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "No products available",
                                fontSize = 16.sp,
                                color = HeetoxDarkGray,
                                modifier = Modifier.padding(top = 250.dp)
                            )
                        }
                    }
                }
            }


        }

    }

    LaunchedEffect(key1 = subCategory) {
        productListVM.getAlternativeProducts(subCategory,token)
    }

    // Handle product list loading and errors
    LaunchedEffect(key1 = allProducts.value) {
        when (allProducts.value) {
            is Resource.Error -> {
                errorSubcategoriesList = "Oops! Couldn't Load Products :("
                loadingSubcategoriesList = false
                dataList = null
            }
            is Resource.Loading -> {
                loadingSubcategoriesList = true
                dataList = null
            }
            is Resource.Success -> {
                loadingSubcategoriesList = false
                dataList = if (allProducts.value.data.isNullOrEmpty()) {
                    null
                } else {
                    allProducts.value.data
                }
            }
            else -> {

            }
        }
    }

    }

