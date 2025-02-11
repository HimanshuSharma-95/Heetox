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
import androidx.compose.runtime.rememberUpdatedState
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
import com.heetox.app.Composables.GeneralCompose.SubCategoriesItem
import com.heetox.app.Composables.ProductCompose.ProductCard
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@SuppressLint("MutableCollectionMutableState")
@Composable
fun ProductListScreen(
    category: String,
    AuthVM: AuthenticationViewModel,
    ProductVM: ProductsViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current

    //only set by product bardcode details clicked better aletrnative
    val CurrentSubCategory = ProductVM.subcategory.collectAsState().value

    val subCategories = ProductVM.SubCategoriesData.collectAsState()
    var subCategoriesList by rememberSaveable {
        mutableStateOf(emptyList<String>())
    }
    var mainCategory by rememberSaveable { mutableStateOf(subCategories.value.data?.main_category ?: "") }

    var AllProducts = ProductVM.AlternativeProductData.collectAsState()
    var dataList by rememberSaveable { mutableStateOf(AllProducts.value.data) }

    val UserData = AuthVM.Localdata.collectAsState()

    var loadingsubcategories by rememberSaveable { mutableStateOf(false) }
    var errorsubcategories by rememberSaveable { mutableStateOf("") }
    var loadingsubcategorieslist by rememberSaveable { mutableStateOf(true) }
    var errorsubcategorieslist by rememberSaveable { mutableStateOf("") }

    var isApiCalled by rememberSaveable { mutableStateOf(false) }
    var currentsubCategory by rememberSaveable {

        mutableStateOf(CurrentSubCategory.ifEmpty {
            subCategoriesList.getOrNull(0) ?: ""
        })

    }

    val lazyListState = rememberLazyListState()

    val currentToken = rememberUpdatedState(UserData.value?.Token)

    // getting sub categories
    LaunchedEffect(key1 = category) {
        if (!isApiCalled) {
            isApiCalled = true
            ProductVM.getSubCategory(category)
        }
    }



    // getting sub categories list
    LaunchedEffect(key1 = currentsubCategory) {
        val token = currentToken.value ?: ""

        if (currentsubCategory.isNotEmpty()) {
            ProductVM.getalternativeproducts(currentsubCategory.replace(" ", "_"), token)
        }

        val index = subCategoriesList.indexOf(currentsubCategory.replace("_", " "))

        if (index != -1) {
            lazyListState.animateScrollToItem(index) // Scroll to the selected item
        }

    }




    // Swipe to refresh functionality
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        ProductVM.getSubCategory(category) // Trigger refresh
        ProductVM.getalternativeproducts(currentsubCategory, currentToken.value ?: "")

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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = mainCategory.ifEmpty { "" },
                                color = HeetoxDarkGray,
                            )
                        }

                        if (loadingsubcategories || errorsubcategories.isNotEmpty()) {
                            if (loadingsubcategories) {
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
                        } else {
                            if (subCategoriesList.isEmpty()) {
                                Text(
                                    text = "No Subcategories Available",
                                    fontSize = 15.sp,
                                    color = HeetoxDarkGray
                                )
                            } else {
                                LazyRow(
                                    state = lazyListState
                                ){
                                    items(subCategoriesList.toList()) { item ->
                                        SubCategoriesItem(
                                            item = item,
                                            isSelected = currentsubCategory.replace("_"," ") == item,
                                            onClick = { selectedCategory ->
                                                if (currentsubCategory != selectedCategory) {
                                                    currentsubCategory = selectedCategory
                                                }
                                                ProductVM.setSubcategory(selectedCategory.replace(" ", "_"))
                                            }

                                        )

                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }

                // Loading state for subcategory product list
                if (loadingsubcategorieslist || errorsubcategorieslist.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(top = 200.dp)
                                .fillMaxSize()
                                .background(HeetoxWhite),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (loadingsubcategorieslist) {
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
                  if(subCategoriesList.isNotEmpty()){

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
                          items(dataList!!.toList(), key = { data -> data.product_barcode }) { data ->
                              var isLiked by remember { mutableStateOf(data.isliked) }
                              var likeCount by remember { mutableStateOf(data.likesCount) }

                              ProductCard(
                                  data = data,
                                  ProductVM = ProductVM,
                                  UserData = UserData,
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

    LaunchedEffect(key1 = subCategories.value) {
        when (subCategories.value) {
            is Resource.Error -> {
                errorsubcategories = "Oops! Couldn't Load :("
                loadingsubcategories = false
            }
            is Resource.Loading -> {
                loadingsubcategories = true
                mainCategory = ""
                dataList = null
            }
            is Resource.Success -> {
                loadingsubcategories = false
                subCategoriesList = subCategories.value.data?.subcategories ?: emptyList()

                // Set the initial subcategory only if currentsubCategory is empty
                if (currentsubCategory.isEmpty() && subCategoriesList.isNotEmpty()) {
                    currentsubCategory = subCategoriesList[0]
                }

                mainCategory = subCategories.value.data?.main_category ?: ""
            }
            else -> {}
        }
    }


    // Handle product list loading and errors
    LaunchedEffect(key1 = AllProducts.value) {
        when (AllProducts.value) {
            is Resource.Error -> {
                errorsubcategorieslist = "Oops! Couldn't Load Products :("
                loadingsubcategorieslist = false
                dataList = null
            }
            is Resource.Loading -> {
                loadingsubcategorieslist = true
                dataList = null
            }
            is Resource.Success -> {
                loadingsubcategorieslist = false
                if (AllProducts.value.data.isNullOrEmpty()) {
                    dataList = null
                } else {
                    dataList = AllProducts.value.data
                }
            }
            else -> {

            }
        }
    }
}
