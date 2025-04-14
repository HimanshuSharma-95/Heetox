package com.heetox.app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.heetox.app.Screens.NavBottomAndNavGraph
import com.heetox.app.ui.theme.HeetoxAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


//    private  val ProductsVM : ProductsViewModel  by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()
        installSplashScreen()


        setContent {

//            val viewModel : AuthenticationViewModel = hiltViewModel()
//            val UserData = viewModel.localData.collectAsState()
//            val context = LocalContext.current
//            val status = viewModel.FetchUserStatus.collectAsState()
//
//
//            val currentUserData = viewModel.getCurrentUserData.collectAsState()
//            val error = currentUserData.value.error
//
//            if(!status.value){
//
//                LaunchedEffect(key1 = Unit) {
//                    val token = UserData.value?.Token
//
//                    if(token!=null){
//                        viewModel.getCurrentUser(token)
//                        Toast.makeText(context,"Welcome!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
////                Log.d("-->", "UserProfileCard: ${status.value} ")
//
//            }
//
//            when(currentUserData.value){
//                is Resource.Error -> {
//                    if(error == "invalid access token"){
//                        viewModel.removetoken()
//                    }
//                }
//                is Resource.Loading -> {}
//                is Resource.Nothing -> {}
//                is Resource.Success -> {}
//            }
//
//
//            viewModel.gettoken()


            HeetoxAppTheme {

                App()


            }




        }
    }
}



@Composable
fun App(){

    NavBottomAndNavGraph()


}













