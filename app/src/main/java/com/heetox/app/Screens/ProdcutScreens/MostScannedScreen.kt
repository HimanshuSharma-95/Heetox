package com.heetox.app.Screens.ProdcutScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.heetox.app.Composables.ProductCompose.MostScannedProductsAll
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ViewModel.ProductsVM.ProductListViewModel
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun MostScannedScreen(navController : NavHostController) {


    val productListVM: ProductListViewModel = hiltViewModel()
    val mostScannedProductsData = productListVM.mostScannedProductsData.collectAsState().value

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        productListVM.getMostScannedProducts()
    }


    Column(

        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(HeetoxWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (loading || error.isNotEmpty()) {
            if(loading){
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = HeetoxBrightGreen
                )
            }else{
                Text(
                    text = "Oops! Couldn't Load Products",
                    fontSize = 15.sp,
                    color = HeetoxDarkGray
                )
            }

        } else {

            mostScannedProductsData.data?.let { MostScannedProductsAll(data = it, navController,) }

        }
    }


    LaunchedEffect(Unit) {
        productListVM.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    if (event.action == Action.AllMostScanned) {
                        loading = true
                        error = "Just a second ;)"
                    }
                }

                is UiEvent.Success -> {
                    if (event.action == Action.AllMostScanned) {
                        loading = false
                        error = ""
                    }
                }

                is UiEvent.Error -> {
                    if (event.action == Action.AllMostScanned) {
                        loading = false
                        error = event.message
                    }
                }

                UiEvent.Idle -> Unit
            }
        }
    }


}