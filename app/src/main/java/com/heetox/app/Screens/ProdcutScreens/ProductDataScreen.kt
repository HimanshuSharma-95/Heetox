package com.heetox.app.Screens.ProdcutScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.heetox.app.Composables.ProductCompose.ProductByBarcodeDetails
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.R
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ViewModel.ProductsVM.ConsumeViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay


@Composable
fun ProductDataScreen(barcode : String,userData:LocalStoredData?, navController: NavHostController){


    val productVM :ProductsViewModel = hiltViewModel()

    val productDetails = productVM.productByBarcodeData.collectAsState()

    val consumeVM : ConsumeViewModel = hiltViewModel()
    val consumeProductData = consumeVM.consumeProductData.collectAsState().value

    val token by rememberSaveable {
        mutableStateOf(userData?.Token)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var loading by rememberSaveable {
        mutableStateOf(true)
    }

    val receivedBarcode by rememberSaveable {
        mutableStateOf(barcode)
    }


    var isApiCalled by rememberSaveable { mutableStateOf(false) }


    if(loading || error.isNotEmpty()){

        var degree by rememberSaveable { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HeetoxWhite),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ){


            if (loading){
                Image(painter = painterResource(id = R.drawable.loadingcircle), contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
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

          if(token != null){
              productVM.getProductByBarcode(barcode, token!!)
          }else{
              productVM.getProductByBarcode(barcode, "")
          }

            isApiCalled = true

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