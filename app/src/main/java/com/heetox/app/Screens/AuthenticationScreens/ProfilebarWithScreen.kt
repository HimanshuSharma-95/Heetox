package com.heetox.app.Screens.AuthenticationScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.heetox.app.Composables.AuthCompose.ProfileInputs
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Authentication.NavDrawer
import com.heetox.app.R
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBarWithScreen(navController: NavHostController,
                         userData:LocalStoredData?,
                         logoutUser:(String) -> Unit,
                         uiEvent : Flow<UiEvent>
                         ){



    val context = LocalContext.current
    val loginItems : List<NavDrawer>

    val productVM : ProductsViewModel = hiltViewModel()
    val likedProductsData = productVM.likedProductsData.collectAsState().value
    val token = userData?.Token ?: ""

    if(userData != null){

        if(userData.EmailStatus){

            loginItems = listOf(
                NavDrawer(
                    title = "Update Profile",
                    SelectedIcon = Icons.Filled.Edit,
                    UnselectedIcon = Icons.Outlined.Edit,
                    route = "updateprofile"
                ),
                NavDrawer(
                    title = "Change Password",
                    SelectedIcon = Icons.Filled.Lock,
                    UnselectedIcon = Icons.Outlined.Lock,
                    route = "changepassword"
                ),
                NavDrawer(
                    title = "Privacy Policy",
                    SelectedIcon = Icons.Filled.PrivacyTip,
                    UnselectedIcon = Icons.Outlined.PrivacyTip,
                    route = "privacypolicy"
                ),
                NavDrawer(
                    title = "Logout",
                    SelectedIcon = Icons.Filled.Logout,
                    UnselectedIcon = Icons.Outlined.Logout,
                    route = "logout"
                )

            )
        }else{

            loginItems = listOf(
                NavDrawer(
                    title = "Update Profile",
                    SelectedIcon = Icons.Filled.Edit,
                    UnselectedIcon = Icons.Outlined.Edit,
                    route = "updateprofile"
                ),
                NavDrawer(
                    title = "Change Password",
                    SelectedIcon = Icons.Filled.Lock,
                    UnselectedIcon = Icons.Outlined.Lock,
                    route = "changepassword"
                ),
                NavDrawer(
                    title = "Verify Email",
                    SelectedIcon = Icons.Filled.MarkEmailRead,
                    UnselectedIcon = Icons.Outlined.MarkEmailRead,
                    route = "verifyemail"
                ),
                NavDrawer(
                    title = "Privacy Policy",
                    SelectedIcon = Icons.Filled.PrivacyTip,
                    UnselectedIcon = Icons.Outlined.PrivacyTip,
                    route = "privacypolicy"
                ),
                NavDrawer(
                    title = "Logout",
                    SelectedIcon = Icons.Filled.Logout,
                    UnselectedIcon = Icons.Outlined.Logout,
                    route = "logout"
                )


            )

        }

    }else{

        loginItems = listOf(
            NavDrawer(
                title = "Update Profile",
                SelectedIcon = Icons.Filled.ModeEdit,
                UnselectedIcon = Icons.Outlined.ModeEdit,
                route = "updateprofile"
            ),
            NavDrawer(
                title = "Change Password",
                SelectedIcon = Icons.Filled.Lock,
                UnselectedIcon = Icons.Outlined.Lock,
                route = "changepassword"
            ),
            NavDrawer(
                title = "Privacy Policy",
                SelectedIcon = Icons.Filled.PrivacyTip,
                UnselectedIcon = Icons.Outlined.PrivacyTip,
                route = "privacypolicy"
            ),
            NavDrawer(
                title = "Logout",
                SelectedIcon = Icons.Filled.Logout,
                UnselectedIcon = Icons.Outlined.Logout,
                route = "logout"
            )

        )

    }



    val logoutitems = listOf(

        NavDrawer(
            title = "Register",
            SelectedIcon = Icons.Filled.SupervisorAccount,
            UnselectedIcon = Icons.Outlined.SupervisorAccount,
            route = "register"
        ),
        NavDrawer(
            title = "Login",
            SelectedIcon = Icons.Filled.Login,
            UnselectedIcon = Icons.Outlined.Login,
            route = "login"
        ),
        NavDrawer(
            title = "Privacy Policy",
            SelectedIcon = Icons.Filled.PrivacyTip,
            UnselectedIcon = Icons.Outlined.PrivacyTip,
            route = "privacypolicy"
        ),
    )




    Surface(
        modifier = Modifier


    ) {

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedIndex by rememberSaveable {
            mutableStateOf(0)
        }

        ModalNavigationDrawer(
            drawerContent = {

ModalDrawerSheet(

    modifier = Modifier
        .clip(RoundedCornerShape(0.dp))


){

   Column(

       modifier = Modifier
           .fillMaxHeight()
           .width(300.dp)
           .background(Color.White)
           .padding(30.dp)

       ,
       horizontalAlignment = Alignment.CenterHorizontally

   ){

       if(userData == null){

           logoutitems.forEachIndexed { index, item ->

               NavigationDrawerItem(
                   modifier = Modifier
                       .padding(vertical = 10.dp)

                   ,label = {
                       Text(text = item.title)
                   },
                   selected = index == selectedIndex ,

                   onClick = {
                       selectedIndex = index

                       navController.navigate(item.route.toString())

                       scope.launch {
                           drawerState.close()
                       }

                   },
                   icon = {
                       Icon(
                           if(index == selectedIndex){
                               item.SelectedIcon
                           }else{
                               item.UnselectedIcon
                           },
                           contentDescription = item.title
                       )
                   },
                   colors = NavigationDrawerItemDefaults.colors(
                       selectedContainerColor = Color.Transparent,
                       unselectedContainerColor = Color.Transparent,
                       selectedTextColor = HeetoxDarkGray,
                       unselectedTextColor = HeetoxDarkGray,
                       selectedIconColor = HeetoxDarkGray,
                       unselectedIconColor = HeetoxDarkGray
                   )

               )

               Box(modifier = Modifier
                   .height(2.dp)
                   .fillMaxWidth(.9f)
                   .background(HeetoxLightGray)

               )

           }

       }else{
           loginItems.forEachIndexed { index, item ->

               NavigationDrawerItem(
                   modifier = Modifier
                       .padding(vertical = 10.dp)

                   ,label = {
                       Text(text = item.title)
                   },
                   selected = index == selectedIndex ,

                   onClick = {
                       selectedIndex = index

                       if(item.route == "logout"){

                           logoutUser(token)

                       }else{
                           navController.navigate(item.route.toString())
                       }

                       scope.launch {
                           drawerState.close()
                       }

                   },
                   icon = {
                       Icon(
                           if(index == selectedIndex){
                               item.SelectedIcon
                           }else{
                               item.UnselectedIcon
                           },
                           contentDescription = item.title
                       )
                   },
                   colors = NavigationDrawerItemDefaults.colors(
                       selectedContainerColor = Color.Transparent,
                       unselectedContainerColor = Color.Transparent,
                       selectedTextColor = HeetoxDarkGray,
                       unselectedTextColor = HeetoxDarkGray,
                       selectedIconColor = HeetoxDarkGray,
                       unselectedIconColor = HeetoxDarkGray
                   )

               )

               Box(modifier = Modifier
                   .height(2.dp)
                   .fillMaxWidth(.9f)
                   .background(HeetoxLightGray)

               )

           }
       }


   }

}

        } ,drawerState = drawerState ) {
            Scaffold(

              topBar = {
                  TopAppBar(
                      title = { 
                              Image(painter = painterResource(id = R.drawable.heetoxlogobg), contentDescription ="logo",
                                  modifier = Modifier
                                      .size(100.dp)
                                  )
                      },
                      navigationIcon = {
                          IconButton(onClick = {
                             scope.launch {
                                 drawerState.open()
                             }
                          }) {
                              Icon(imageVector =
                              Icons.Default.Menu
                                  , contentDescription = "" )

                          }
                      },
                              colors = TopAppBarDefaults.topAppBarColors(
                              containerColor = HeetoxGreen
                              )

                  )
              },
                containerColor = Color.Transparent,
                modifier = Modifier
                    .background(Color.Transparent)
                    .zIndex(1f)
            ){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(HeetoxGreen)
                    .padding(it)
                ){


               Column {
                   ProfileInputs(
                       navController = navController, userData,
                       getLikedProducts = {
                           productVM.getLikedProducts(it)
                       },
                       uiEvent = productVM.uiEvent,
                       likedProductsData = likedProductsData
                   )
                   Box(modifier = Modifier
                       .fillMaxSize()
                       .background(HeetoxWhite)
                   ){


                   }
               }

                }
            }



        }


    }



    LaunchedEffect(Unit){
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> { }

                is UiEvent.Success -> {
                    when (event.action) {
                        Action.Logout -> {
//                  navController.navigate("profile")
                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()

                        }
                        else->{}
                    }
                }

                is UiEvent.Error -> {
                    when (event.action) {
                        Action.Logout -> {
                            Toast.makeText(context, "Couldn't log out. Try again.", Toast.LENGTH_SHORT).show()
                        }
                        else-> Unit
                    }
                }
                UiEvent.Idle -> Unit
            }
        }
    }


}









