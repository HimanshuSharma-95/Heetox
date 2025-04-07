package com.heetox.app.ViewModel.Authentication

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heetox.app.Model.ApiResponse
import com.heetox.app.Model.Authentication.AuthUser
import com.heetox.app.Model.Authentication.ChangePasswordSend
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Authentication.LoginData
import com.heetox.app.Model.Authentication.LoginSend
import com.heetox.app.Model.Authentication.RegisterSend
import com.heetox.app.Model.Authentication.SendOtpSend
import com.heetox.app.Model.Authentication.UpdateProfileSend
import com.heetox.app.Model.Authentication.UpdateProfleResponse
import com.heetox.app.Model.Authentication.VerifyEmailSend
import com.heetox.app.Repository.AuthenticationRepository.AuthenticationRepository
import com.heetox.app.Repository.AuthenticationRepository.SharedPreferenceRepository
import com.heetox.app.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel @Inject constructor( val AuthRepo : AuthenticationRepository,private val TokenRepo : SharedPreferenceRepository) : ViewModel() {

//    val Registeruserdata : StateFlow<Resource<AuthUser>>
//        get() = AuthRepo.accessRegisterUser

    val _registerUserdata = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())
//        get() = AuthRepo.accessRegisterUser

    val registerUserData = _registerUserdata.asStateFlow()

    val Loginuserdata : StateFlow<Resource<LoginData>>
        get() = AuthRepo.accessLoginUser

    val Logoutuser : StateFlow<Resource<ApiResponse<Nothing>>>
        get() = AuthRepo.accessLogoutUser

    val Localdata : StateFlow<LocalStoredData?>
        get() = TokenRepo.accessLocalData

    val GetCurrentUserData : StateFlow<Resource<AuthUser>>
        get() = AuthRepo.accessCurrentUser

    val FetchUserStatus : StateFlow<Boolean>
        get() = AuthRepo.accessFetchUser

    val UpdateUserData : StateFlow<Resource<UpdateProfleResponse>>
        get() = AuthRepo.accessUpdatetUser

    val UploadProfileImageData : StateFlow<Resource<AuthUser>>
        get() = AuthRepo.accessUplaodprofileImage

    val RemoveProfileImage : StateFlow<Resource<AuthUser>>
        get() = AuthRepo.accessRemoveprofileImage

    val SendOtpData : StateFlow<Resource<String>>
        get() = AuthRepo.accessSendOtpData

    val VerifyOtpData : StateFlow<Resource<AuthUser>>
        get() = AuthRepo.accessVerifyOtpData


    val ChangePasswordData : StateFlow<Resource<String>>
        get() = AuthRepo.accessChangePasswordData


    val ForgotPasswordData : StateFlow<Resource<String>>
        get() = AuthRepo.accessForgotPasswordData


    //changed
    fun registerUser(userData: RegisterSend){

        viewModelScope.launch(){

            _registerUserdata.value = Resource.Loading()

           val response = AuthRepo.registerUser(userData)

            _registerUserdata.value = response
        }

    }

    fun loginUser(userData: LoginSend) {

        viewModelScope.launch(Dispatchers.IO) {

            AuthRepo.Loginuser(userData)

        }

    }

        fun savetoken(data : LocalStoredData){

          viewModelScope.launch(Dispatchers.IO)  {
              TokenRepo.saveToken(data)
          }
            gettoken()

        }

        fun gettoken(){

       viewModelScope.launch(Dispatchers.IO)  {
           TokenRepo.getToken()
       }

        }

    fun removetoken(){

        viewModelScope.launch(Dispatchers.IO)  {
            TokenRepo.removeToken()
        }
        gettoken()

    }


    fun logoutuser(token : String){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.logoutUser(token)

        }


    }


    fun getcurrentuser(token: String){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.GetCurrentUser(token)

        }

    }


    fun updateuser(token: String,userdata : UpdateProfileSend){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.UpdateProfile(token,userdata)

        }

    }


    fun updateprofileimage(context: Context,token: String,imageUri : Uri){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.UplaodProfileImage(context,imageUri,token)

        }

    }


    fun removeprofileimage(token: String){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.RemoveProfileImage(token)

        }

    }


    fun sendotp(email: SendOtpSend){
        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.SendOtp(email)

        }
    }


    fun verifyotp(token: String,data : VerifyEmailSend){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.VerifyOtp(token,data)

        }

    }



    fun changepassword(token: String,data:ChangePasswordSend){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.changePassword(token,data)

        }


    }


    fun forgotpassword(email:String){

        viewModelScope.launch(Dispatchers.IO)  {

            AuthRepo.forgetPassword(email)

        }

    }




}