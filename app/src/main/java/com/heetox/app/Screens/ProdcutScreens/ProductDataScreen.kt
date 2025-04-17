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
import com.heetox.app.Composables.ProductCompose.ProductByBarcodeDetails
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ViewModel.ProductsVM.ConsumeViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxBrightGreen
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun ProductDataScreen(barcode : String,userData:LocalStoredData?, navController: NavHostController){


    val productVM :ProductsViewModel = hiltViewModel()

    val productDetails = productVM.productByBarcodeData.collectAsState()

    val consumeVM : ConsumeViewModel = hiltViewModel()
    val consumeProductData = consumeVM.consumeProductData.collectAsState().value

    val token = userData?.Token
    val receivedBarcode = barcode

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var loading by rememberSaveable {
        mutableStateOf(true)
    }


//    var isApiCalled by rememberSaveable { mutableStateOf(false) }


    if(loading || error.isNotEmpty()){


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HeetoxWhite),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ){


            if (loading){
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = HeetoxBrightGreen
                )
            }else{

                Text(text = error,
                    fontSize = 14.sp,
                    color = HeetoxDarkGray,
                )

            }


        }


    }else{

        productDetails.value.data?.let {
            ProductByBarcodeDetails(details = it,
                userData = userData,
                navController = navController,
                likeUnlike = { barcode, token ->
                    productVM.likeUnlikeProduct(barcode, token)
                },
                consumeProduct = {token,barcode,size ->
                    consumeVM.consumeProduct(token,barcode,size)
                },
                consumeProductData = consumeProductData,
                uiEvents = consumeVM.uiEvent
            ) }



    }



    LaunchedEffect(key1 = receivedBarcode) {

              productVM.getProductByBarcode(barcode, token?: "")


    }


    LaunchedEffect(Unit) {

        productVM.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    if (event.action == Action.ProductByBarcode) {
                        loading = true
                        error = "Just a second ;)"
                    }
                }

                is UiEvent.Success -> {
                    if (event.action == Action.ProductByBarcode) {
                        loading = false
                        error = ""
                    }
                }

                is UiEvent.Error -> {
                    if (event.action == Action.ProductByBarcode) {
                        loading = false
                        error = event.message
                    }
                }

                UiEvent.Idle -> Unit
            }
        }
    }


}