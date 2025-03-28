package com.heetox.app.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.heetox.app.Composables.GeneralCompose.AboutusHome
import com.heetox.app.Composables.GeneralCompose.CategoriesHome
import com.heetox.app.Composables.GeneralCompose.LogoAndSearchBar
import com.heetox.app.Composables.GeneralCompose.TextAndScanHome
import com.heetox.app.Composables.ProductCompose.MostScannedProductsSix
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun Homescreen(navController: NavHostController,Productviewmodel: ProductsViewModel){

    val Authviewmodel : AuthenticationViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    val UserData = Authviewmodel.Localdata.collectAsState()

    val name = UserData.value?.Name

    // Remember the swipe refresh state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    // Handle refresh action
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            // Trigger the refresh logic here
            // e.g., reloading the data from your ViewModel
            Productviewmodel.getcategories()
            Productviewmodel.getmostscannedproducts()

            // Example function, replace with actual implementation
            swipeRefreshState.isRefreshing = false // Stop refreshing after the logic is complete
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(HeetoxWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            //top bar
            LogoAndSearchBar(navController, null)

            Spacer(modifier = Modifier.height(10.dp))

            //categories
            CategoriesHome(Productviewmodel, navController)


            //Text and Scan
            TextAndScanHome(navController,name)


            //Most Scanned Products 6
            MostScannedProductsSix(navController, Productviewmodel)


            //about us
            AboutusHome(navController)


            Spacer(modifier = Modifier.height(10.dp))


        }

    }


}