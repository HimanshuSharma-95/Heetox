package com.heetox.app.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.heetox.app.Model.General.BottomNavData
import com.heetox.app.R
import com.heetox.app.Screens.AuthenticationScreens.ChangePasswordScreen
import com.heetox.app.Screens.AuthenticationScreens.ConsumedProductScreen
import com.heetox.app.Screens.AuthenticationScreens.Forgotpasswordscreen
import com.heetox.app.Screens.AuthenticationScreens.LoginScreen
import com.heetox.app.Screens.AuthenticationScreens.PrivacyPolicy
import com.heetox.app.Screens.AuthenticationScreens.ProfileBarwithScreen
import com.heetox.app.Screens.AuthenticationScreens.RegisterScreen
import com.heetox.app.Screens.AuthenticationScreens.updateprofilescreen
import com.heetox.app.Screens.AuthenticationScreens.verifyemailscreen
import com.heetox.app.Screens.PostsScreen.Postscreen
import com.heetox.app.Screens.ProdcutScreens.BetterAlternativesScreen
import com.heetox.app.Screens.ProdcutScreens.MostScannedScreen
import com.heetox.app.Screens.ProdcutScreens.ProductDataScreen
import com.heetox.app.Screens.ProdcutScreens.ProductListScreen
import com.heetox.app.Screens.ProdcutScreens.Scanscreen
import com.heetox.app.Screens.ProdcutScreens.SearchProductListScreen
import com.heetox.app.Screens.ProdcutScreens.Searchscreen
import com.heetox.app.Utils.rememberImeState
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.PostVM.PostsViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavbottomandNavGraph(){


    val items = listOf(
        BottomNavData(
            title = "Home",
            SelectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home,
            hasNews = false
        ),
        BottomNavData(
            title = "Search",
            SelectedIcon = Icons.Filled.Search,
            unSelectedIcon = Icons.Outlined.Search,
            hasNews = false
        ),
        BottomNavData(
            title = "Scan",
            SelectedIcon = ImageVector.vectorResource(id = R.drawable.barcodebottom),
            unSelectedIcon =  ImageVector.vectorResource(id = R.drawable.barcodebottom),
            hasNews = false
        ),
        BottomNavData(
            title = "posts",
            SelectedIcon = ImageVector.vectorResource(id = R.drawable.blogfilled),
            unSelectedIcon =  ImageVector.vectorResource(id = R.drawable.blogunfilled),
            hasNews = false
        ),
        BottomNavData(
            title = "Profile",
            SelectedIcon = Icons.Filled.AccountCircle,
            unSelectedIcon = Icons.Outlined.AccountCircle,
            hasNews = false
        ),
    )



    val imeState = rememberImeState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var selectedOption by rememberSaveable { mutableIntStateOf(0) }

    val routes = listOf("home", "search", "scan", "posts", "profile")

    selectedOption = when (currentDestination?.route){
        "home" -> 0
        "search" -> 1
        "scan" -> 2
        "like" -> 3
        "profile" -> 4
        else -> selectedOption
    }

    Scaffold( bottomBar = {

        //if to not show bottom navbar on these screens
        if(currentDestination?.route == "login" ||
            currentDestination?.route == "register" ||
            currentDestination?.route == "updateprofile" ||
            currentDestination?.route == "forgotpassword" ||
            currentDestination?.route == "changepassword"||
            currentDestination?.route == "verifyemail"
            ){

        }else{

            Box(

                modifier = Modifier

            ){


                NavigationBar(

                    modifier = Modifier
                        .height(
                            if (imeState.value) {
                                0.dp
                            } else {
                                75.dp
                            }
                        ),
                    containerColor = Color.White
                )
                {
                    items.forEachIndexed { index, item ->


                        //for scan icon different design
                        if (index == 2) {


                            NavigationBarItem(selected = selectedOption == index,
                                onClick = {
                                    selectedOption = index

                                    navController.navigate(routes[index]) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }

                                },
                                colors = NavigationBarItemDefaults
                                    .colors(
                                        indicatorColor = Color.White
                                    ),
                                icon = {

                                    Box(
                                        modifier = Modifier
                                            .zIndex(1f)
                                            .background(Color.White, CircleShape)
                                            .border(2.dp, HeetoxDarkGreen, CircleShape)
                                            .padding(15.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (selectedOption == index) {
                                                item.SelectedIcon
                                            } else {
                                                item.unSelectedIcon
                                            },
                                            contentDescription = item.title,
                                            tint = if (selectedOption == index) HeetoxDarkGreen else HeetoxDarkGray,
                                            modifier = Modifier
                                                .size(30.dp)
                                        )
                                    }
                                }

                            )
                        } else {


                            NavigationBarItem(selected = selectedOption == index,
                                onClick = {
                                    selectedOption = index

                                    navController.navigate(routes[index]) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }

                                },
                                colors = NavigationBarItemDefaults
                                    .colors(
                                        indicatorColor = Color.White
                                    ),
                                label = {
                                    Text(
                                        text = item.title,
                                        color = if (index == selectedOption) {
                                            HeetoxDarkGreen
                                        } else {
                                            HeetoxDarkGray
                                        }
                                    )
                                },
                                icon = {

                                    Box(
                                        modifier = Modifier
                                    ) {
                                        Icon(
                                            imageVector = if (selectedOption == index) {
                                                item.SelectedIcon
                                            } else {
                                                item.unSelectedIcon
                                            },
                                            contentDescription = item.title,
                                            tint = if (selectedOption == index) HeetoxDarkGreen else HeetoxDarkGray,
                                        )
                                    }
                                }

                            )


                        }

                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(HeetoxLightGray)
                )

            }
        }
    } ){


        NavGraph(navController,it)




    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues){



    val ProductViewModel : ProductsViewModel = hiltViewModel()

    val AuthenticationViewModel : AuthenticationViewModel = hiltViewModel()

    val PostViewModel : PostsViewModel = hiltViewModel()



    Box(modifier = Modifier.padding(paddingValues)){



     NavHost(navController = navController, startDestination = "home",

         enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn(animationSpec = tween(700)) },
         exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut(animationSpec = tween(700)) },
         popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn(animationSpec = tween(700)) },
         popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut(animationSpec = tween(700)) }

        ) {

            composable("home") {
                Homescreen(navController,ProductViewModel)

            }



            navigation(startDestination = "search", route = "searchscreen") {


                composable("search") {
                    Searchscreen(navController = navController)

                }


                composable("searchproductlist/{search}",
                    arguments = listOf(navArgument("search") { type = NavType.StringType })
                ) { backStackEntry ->
                    val search = backStackEntry.arguments?.getString("search") ?: ""
                    SearchProductListScreen(search = search, AuthVM = AuthenticationViewModel, ProductVM = ProductViewModel, navController = navController)
                }
            }








            composable("scan") {
                Scanscreen(navController = navController)
            }

         composable("consumed") {
             ConsumedProductScreen(navController = navController,ProductViewModel,AuthenticationViewModel)
         }



            composable("posts") {
                Postscreen(PostViewModel,AuthenticationViewModel)
            }

            composable("mostscanned"){
                MostScannedScreen(ProductViewModel,navController)
            }


         composable("aboutus"){
             AboutUs()
         }






//             Nested Navigation Graph for Product Details
//            navigation(startDestination = "productlist/{category}/{subcategory?}", route = "categorylist") {

                composable("productdetails/{barcode}",
                    arguments = listOf(navArgument("barcode") { type = NavType.StringType })

                ) { backStackEntry ->
                    val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                    ProductDataScreen(barcode, ProductViewModel, AuthenticationViewModel, navController)

                }




         // Define the categorylist nested graph
         navigation(startDestination = "productlist/{category}/{source}", route = "categorylist") {
             composable(
                 "productlist/{category}/{source}",
                 arguments = listOf(
                     navArgument("category") { type = NavType.StringType },
                     navArgument("source") { type = NavType.StringType }
                 )
             ) { backStackEntry ->
                 val category = backStackEntry.arguments?.getString("category") ?: ""
                 val source = backStackEntry.arguments?.getString("source") ?: "HOME" // Default to "HOME"

                 ProductListScreen(
                     category = category,
                     AuthVM = AuthenticationViewModel,
                     ProductVM = ProductViewModel,
                     navController = navController,
                     source = source
                 )
             }
         }



         composable("betteralternative/{category}/{subCategory}",
             arguments = listOf(
                 navArgument("category") { type = NavType.StringType },
                 navArgument("subCategory") { type = NavType.StringType }
             )
             ){

                 backStackEntry ->
             val category = backStackEntry.arguments?.getString("category") ?: ""
             val subCategory = backStackEntry.arguments?.getString("subCategory") ?: ""

             BetterAlternativesScreen(
                 category = category,
                 subCategory = subCategory,
                 AuthVM = AuthenticationViewModel,
                 ProductVM = ProductViewModel,
                 navController = navController,
             )
         }





            //profile
            composable("forgotpassword"){
                Forgotpasswordscreen(navController = navController)
            }





            navigation(startDestination = "userprofile", route = "profile"){

                 composable("userprofile"){

                    ProfileBarwithScreen(navController = navController)

                }

                composable("register"){
                    RegisterScreen(navController)
                }

                composable("login"){
                    LoginScreen(navController)
                }

                composable("updateprofile"){
                  updateprofilescreen(navController)
                }
                composable("verifyemail"){
                   verifyemailscreen(navController = navController)
                }
                composable("changepassword"){
                   ChangePasswordScreen(navContorller = navController)
                }

                composable("privacypolicy"){
                    PrivacyPolicy()
                }

            }

        }
    }


}

