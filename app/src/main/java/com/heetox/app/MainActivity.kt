package com.heetox.app


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.heetox.app.Screens.NavbottomandNavGraph
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
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


            val viewModel : AuthenticationViewModel = hiltViewModel()
            val UserData = viewModel.Localdata.collectAsState()
            val context = LocalContext.current
            val status = viewModel.FetchUserStatus.collectAsState()


            val currentUserData = viewModel.GetCurrentUserData.collectAsState()
            val error = currentUserData.value.error

            if(!status.value){

                LaunchedEffect(key1 = Unit) {
                    val token = UserData.value?.Token

                    if(token!=null){
                        viewModel.getcurrentuser(token)
                        Toast.makeText(context,"Welcome!", Toast.LENGTH_SHORT).show()
                    }
                }

//                Log.d("-->", "UserProfileCard: ${status.value} ")

            }

            when(currentUserData.value){
                is Resource.Error -> {
                    if(error == "invalid access token"){
                        viewModel.removetoken()
                    }
                }
                is Resource.Loading -> {}
                is Resource.Nothing -> {}
                is Resource.Success -> {}
            }


            viewModel.gettoken()


            HeetoxAppTheme {

                App()


            }




        }
    }
}




@Composable
fun App(){

    NavbottomandNavGraph()


}













