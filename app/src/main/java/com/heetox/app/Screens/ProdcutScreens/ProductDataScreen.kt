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
import androidx.navigation.NavHostController
import com.heetox.app.Composables.ProductCompose.ProductByBarcodeDetails
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay


@Composable
fun ProductDataScreen(barcode : String ,ProductVM : ProductsViewModel, AuthVM : AuthenticationViewModel,navController: NavHostController){


    val ProductDetails = ProductVM.ProductByBarcodeData.collectAsState()
    val UserData = AuthVM.Localdata.collectAsState()

    val token by rememberSaveable {
        mutableStateOf(UserData.value?.Token)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var Loading by rememberSaveable {
        mutableStateOf(true)
    }

    var Barcode by rememberSaveable {
        mutableStateOf(barcode)
    }




    var isApiCalled by rememberSaveable { mutableStateOf(false) }



    if(Loading || error.isNotEmpty()){

        var degree by rememberSaveable { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(HeetoxWhite),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ){


            if (Loading){
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

        ProductDetails.value.data?.let { ProductByBarcodeDetails(Details = it,AuthVM,ProductVM,navController) }



    }



    LaunchedEffect(key1 = Barcode) {

//        if (!isApiCalled) {

          if(token != null){
              ProductVM.getproductbybarcode(barcode, token!!)
          }else{
              ProductVM.getproductbybarcode(barcode, "")
          }

            isApiCalled = true
//        }

    }



LaunchedEffect(key1 = ProductDetails.value) {

    when(ProductDetails.value){

        is Resource.Error -> {
            error = "oops! Couldn't Load Product :("
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