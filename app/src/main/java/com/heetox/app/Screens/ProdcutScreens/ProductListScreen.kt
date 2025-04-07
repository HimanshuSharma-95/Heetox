package com.heetox.app.Screens.ProdcutScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.heetox.app.Composables.GeneralCompose.SubCategoriesItem
import com.heetox.app.Composables.ProductCompose.ProductCard
import com.heetox.app.Model.Product.AlternateResponseItem
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite


//screen of home screen categories products
@SuppressLint("MutableCollectionMutableState")
@Composable
fun ProductListScreen(
    category: String,
    AuthVM: AuthenticationViewModel,
    ProductVM: ProductsViewModel,
    navController: NavHostController,
    source :String? = null
) {

    val context = LocalContext.current

    val userData = AuthVM.Localdata.collectAsState()
    val token = userData.value?.Token ?: ""

    val subCategories = ProductVM.SubCategoriesData.collectAsState()
    val subCategoriesList = subCategories.value.data?.subcategories


    var currentSubCategory by rememberSaveable{
        mutableStateOf("")
    }

    //sub categories states
    var subCategoryError by rememberSaveable {
        mutableStateOf("")
    }
    var subCategoryLoading by rememberSaveable {
        mutableStateOf(true)
    }


    val allProduct = ProductVM.AlternativeProductData.collectAsState()
    var dataList : ArrayList<AlternateResponseItem>? = allProduct.value.data

    //data list states
    var dataListError by rememberSaveable {
        mutableStateOf("")
    }
    var dataListLoading by rememberSaveable {
        mutableStateOf(true)
    }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(subCategories.value) {

        when(subCategories.value){
            is Resource.Error -> {
                subCategoryError = "Oops! Couldn't Load :("
                subCategoryLoading = false
            }
            is Resource.Loading -> {
                subCategoryLoading = true
                subCategoryError = ""
            }
            is Resource.Nothing -> {}
            is Resource.Success ->{

                subCategoryError = ""
                subCategoryLoading = false

                if (currentSubCategory.isEmpty() && subCategoriesList?.isNotEmpty() == true) {
                    currentSubCategory = subCategoriesList[0]

                }
//                Log.e("1 --->", "ProductListScreen: ${currentSubCategory} ${subCategoriesList} ", )


            }

        }
    }



    LaunchedEffect(currentSubCategory) {
        if (currentSubCategory.isNotEmpty() && subCategoriesList?.isNotEmpty() == true) {
            ProductVM.getalternativeproducts(currentSubCategory, token)

            val index = subCategoriesList.indexOf(currentSubCategory.replace("_", " ")) // Find the index
            if (index != -1) {
                lazyListState.animateScrollToItem(index) // Scroll to the selected item
            }
        }
    }


    LaunchedEffect(allProduct.value){
        when(allProduct.value){
            is Resource.Error ->{
                dataListLoading = false
                dataListError = "No Products Available"
            }
            is Resource.Loading ->{
                dataListLoading = true
                dataListError = ""
//
            }
            is Resource.Nothing -> {}
            is Resource.Success -> {
                dataListError = ""
                dataList = allProduct.value.data
                dataListLoading = false
//                Log.e("2 --->", "ProductListScreen: ${dataList} ", )
            }
        }
    }


    // Swipe to refresh functionality
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        ProductVM.getSubCategory(category) // Trigger refresh
        ProductVM.getalternativeproducts(currentSubCategory,token)

    }) {

        val listState = rememberLazyListState()


        Column(
            modifier = Modifier
                .padding(bottom = 0.dp)
                .fillMaxSize()
                .background(HeetoxWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                item {

                    Spacer(modifier = Modifier.height(15.dp))

                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = category.ifEmpty { "" },
                                color = HeetoxDarkGray,
                            )
                        }

                        if (subCategoryLoading || subCategoryError.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                if (subCategoryLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(30.dp),
                                        color = HeetoxBrightGreen
                                    )
                                } else {
                                    swipeRefreshState.isRefreshing = false
                                    Text(
                                        text = "Oops! Couldn't Load Products",
                                        fontSize = 15.sp,
                                        color = HeetoxDarkGray
                                    )
                                }

                            }
                        } else {

                            if (subCategoriesList?.isEmpty() == true) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No Subcategories Available",
                                        fontSize = 15.sp,
                                        color = HeetoxDarkGray
                                    )
                                }
                            } else {
                                    LazyRow(
                                        state = lazyListState,
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(subCategoriesList.orEmpty()) { item ->
                                            SubCategoriesItem(
                                                item = item,
                                                isSelected = currentSubCategory.replace("_", " ") == item,
                                                onClick = { selectedCategory ->
                                                    if (currentSubCategory != selectedCategory) {
                                                        currentSubCategory = selectedCategory
                                                    }
                                                    ProductVM.setSubcategory(selectedCategory.replace(" ", "_"))
                                                }
                                            )
                                        }
                                    }

                            }
                        }


                    }


                }

                if (dataListLoading || dataListError.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(top = 200.dp)
                                .fillMaxSize()
                                .background(HeetoxWhite),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (dataListLoading) {
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
                    if (subCategoriesList != null) {

                        if(subCategoriesList.isNotEmpty()){

                            if (!dataList.isNullOrEmpty()) {

                                // Display product cards
                                items(dataList!!.toList(), key = { data -> data.product_barcode }) { data ->
                                    var isLiked by remember { mutableStateOf(data.isliked) }
                                    var likeCount by remember { mutableStateOf(data.likesCount) }

                                    ProductCard(
                                        data = data,
                                        ProductVM = ProductVM,
                                        UserData = userData,
                                        context = context,
                                        navController = navController,
                                        isLiked = isLiked,
                                        likeCount = likeCount,
                                        onLikeCountChange = { likeCount = it },
                                        onLikeChange = { isLiked = it }
                                    )
                                }
                            }
                        }else{
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

            }

        }





















