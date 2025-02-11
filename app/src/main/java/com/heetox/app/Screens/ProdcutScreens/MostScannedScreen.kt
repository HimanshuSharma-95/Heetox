package com.heetox.app.Screens.ProdcutScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.heetox.app.Composables.ProductCompose.MostScannedProductsAll
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun MostScannedScreen(Productviewmodel : ProductsViewModel,navController : NavHostController){

    val mostScannedProductsData = Productviewmodel.MostScannedProductsData.collectAsState()



   Column(

       modifier = androidx.compose.ui.Modifier
           .fillMaxSize()
           .background(HeetoxWhite)

   ){

       mostScannedProductsData.value.data?.let { MostScannedProductsAll(data = it,navController,) }

   }




}