package com.heetox.app.Repository.AuthenticationRepository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.google.gson.Gson
import com.heetox.app.ApiInterface.AuthenticationInterface
import com.heetox.app.Model.ApiResponse
import com.heetox.app.Model.Authentication.AuthUser
import com.heetox.app.Model.Authentication.ChangePasswordSend
import com.heetox.app.Model.Authentication.ErrorResponse
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Authentication.LoginData
import com.heetox.app.Model.Authentication.LoginSend
import com.heetox.app.Model.Authentication.RegisterSend
import com.heetox.app.Model.Authentication.SendOtpSend
import com.heetox.app.Model.Authentication.UpdateProfileSend
import com.heetox.app.Model.Authentication.UpdateProfleResponse
import com.heetox.app.Model.Authentication.VerifyEmailSend
import com.heetox.app.Utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject


class AuthenticationRepository @Inject constructor(val Api : AuthenticationInterface
,
    val TokenRepo : SharedPreferenceRepository

){

   //register
    private var RegisterUser = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())

    val accessRegisterUser : StateFlow<Resource<AuthUser>>
        get() = RegisterUser


    //login
    private  var LoginUser = MutableStateFlow<Resource<LoginData>>(Resource.Nothing())

    val accessLoginUser : StateFlow<Resource<LoginData>>
        get() = LoginUser


    //logout
    private val LogoutUser = MutableStateFlow<Resource<ApiResponse<Nothing>>>(Resource.Nothing())

    val accessLogoutUser : StateFlow<Resource<ApiResponse<Nothing>>>
        get() = LogoutUser


    //current User
    private var CurrentUser = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())

    val accessCurrentUser : StateFlow<Resource<AuthUser>>
        get() = CurrentUser


    //LocalData
    val Localdata : StateFlow<LocalStoredData?>
        get() = TokenRepo.accessLocalData


    //UpdateUser
    private var UpdateUser = MutableStateFlow<Resource<UpdateProfleResponse>>(Resource.Nothing())

    val accessUpdatetUser : StateFlow<Resource<UpdateProfleResponse>>
        get() = UpdateUser



    //one time fetch
    private var FetchUser = MutableStateFlow<Boolean>(false)

    val accessFetchUser: StateFlow<Boolean>
        get() = FetchUser


    //upload Image
    private  var UplaodProfileImage = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())

    val accessUplaodprofileImage :StateFlow<Resource<AuthUser>>
        get() = UplaodProfileImage



    //remove Image
    private  var RemoveProfileImage = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())

    val accessRemoveprofileImage :StateFlow<Resource<AuthUser>>
        get() = RemoveProfileImage



    //send otp
    private var SendOtpData = MutableStateFlow<Resource<String>>(Resource.Nothing())

    val accessSendOtpData : StateFlow<Resource<String>>
        get() = SendOtpData



    //verify otp
    private  var VerifyOtpData = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())

    val accessVerifyOtpData : StateFlow<Resource<AuthUser>>
        get() = VerifyOtpData



    //Change password
    private var ChangePasswordData = MutableStateFlow<Resource<String>>(Resource.Nothing())

    val accessChangePasswordData : StateFlow<Resource<String>>
        get() = ChangePasswordData



    //Forgot Password
    private  var ForgotPasswordData = MutableStateFlow<Resource<String>>(Resource.Nothing())

    val accessForgotPasswordData : StateFlow<Resource<String>>
        get() = ForgotPasswordData






    suspend fun Registeruser(userData: RegisterSend){

    RegisterUser.emit(Resource.Loading())

   try {
       val response = Api.registerUser(userData)

       if(response.body() != null && response.body()!!.success){

           RegisterUser.emit(Resource.Success<AuthUser>(response.body()?.data))


       }else{

           val errorBody = response.errorBody()?.string()
           val errorResponse = errorBody?.let {
               Gson().fromJson(it, ErrorResponse::class.java)
           }

           RegisterUser.emit(Resource.Error(errorResponse!!.message))

       }
    }catch(e:Exception){
        RegisterUser.emit(Resource.Error("Couldn't Register Try again"))
       Log.d("auth repo ->", "Registeruser: $e")
    }

}



    suspend fun Loginuser(userData : LoginSend){

        LoginUser.emit(Resource.Loading())

        try{
            val response = Api.loginUser(userData)

            if(response.body() != null && response.body()!!.success){

                LoginUser.emit(Resource.Success(response.body()!!.data))

                LoginUser.value.data?.let {
                    GetCurrentUser(it.accesstoken)
                }

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                LoginUser.emit(Resource.Error(errorResponse!!.message))

            }
        }catch (e:Exception){

            LoginUser.emit(Resource.Error("Couldn't Login Try again"))
            Log.d("auth repo ->", "Loginuser: $e")

        }


    }




    suspend fun logoutUser(Token : String){

        LogoutUser.emit(Resource.Loading())

        try{

            val response = Api.logoutuser(Token)

            if(response.body()!!.success && response.body() != null ){

                LogoutUser.emit(Resource.Success(response.body()))

                TokenRepo.removeToken()

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                LogoutUser.emit(Resource.Error(errorResponse!!.message))

            }

        }catch(e : Exception){

            LogoutUser.emit(Resource.Error("Couldn't Logout Try again"))
            Log.d("auht repo ->", "logoutUser: $e")

        }


    }




    suspend fun GetCurrentUser(Token: String){

        CurrentUser.emit(Resource.Loading())

        try {
            val response = Api.getCurrentUser(Token)

            if(response.body() != null && response.body()!!.success){

                CurrentUser.emit(Resource.Success(response.body()?.data))


                if(Localdata.value == null) {

                TokenRepo.saveToken(LocalStoredData(
                    true,
                    accessLoginUser.value.data!!.accesstoken,
                    accessLoginUser.value.data!!.user.fullname,
                    accessLoginUser.value.data!!.user.email,
                    accessLoginUser.value.data!!.user.is_email_verified,
                    accessLoginUser.value.data!!.user.age,
                    accessLoginUser.value.data!!.user.phone_number,
                    accessLoginUser.value.data!!.user.DOB,
                    accessLoginUser.value.data!!.user.avatar,
                    accessLoginUser.value.data!!.user.gender
                ))

                }else{

                    TokenRepo.saveToken(LocalStoredData(
                        true,
                        Localdata.value!!.Token,
                        accessCurrentUser.value.data!!.fullname,
                        accessCurrentUser.value.data!!.email,
                        accessCurrentUser.value.data!!.is_email_verified,
                        accessCurrentUser.value.data!!.age,
                        accessCurrentUser.value.data!!.phone_number,
                        accessCurrentUser.value.data!!.DOB,
                        accessCurrentUser.value.data!!.avatar,
                        accessCurrentUser.value.data!!.gender
                    ))

                    FetchUser.emit(true)

                }

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                CurrentUser.emit(Resource.Error(errorResponse!!.message))

            }
        }catch(e:Exception){
            RegisterUser.emit(Resource.Error("Couldn't get Current User Try again"))
            Log.d("auth repo ->", "GetCurentUser: $e")
        }

    }




    suspend fun UpdateProfile(Token: String, UserData : UpdateProfileSend){


        UpdateUser.emit(Resource.Loading())

        try {

            val response = Api.updateProfile(Token,UserData)

            if(response.body() != null && response.body()!!.success){

                UpdateUser.emit(Resource.Success(response.body()?.data))

                TokenRepo.saveToken(LocalStoredData(
                    true,
                    Localdata.value!!.Token,
                    accessUpdatetUser.value.data!!.fullname,
                    accessUpdatetUser.value.data!!.email,
                    accessUpdatetUser.value.data!!.is_email_verified,
                    accessUpdatetUser.value.data!!.age,
                    accessUpdatetUser.value.data!!.phone_number,
                    accessUpdatetUser.value.data!!.DOB,
                    accessUpdatetUser.value.data!!.avatar,
                    accessUpdatetUser.value.data!!.gender

                ))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                UpdateUser.emit(Resource.Error(errorResponse!!.message))

            }
        }catch(e:Exception){

            UpdateUser.emit(Resource.Error("Couldn't get Current User Try again"))
            Log.d("catch ->", "GetUpdate: $e")

        }

    }




// to get file image file type (e.g., "image/png", "image/jpeg")
    private fun getMimeType(context: Context,uri: Uri): String? {
        val contentResolver = context.contentResolver
        return contentResolver.getType(uri)
    }

//to get file path from the device
    private fun getFileName(context: Context, uri: Uri): String {
        var filename = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            filename = it.getString(columnIndex)
        }
        return filename
    }


    // To create a temporary file from input stream
    private fun createTempFileFromInputStream(context: Context,inputStream: InputStream?, fileName: String): File {
        val tempFile = File(context.cacheDir, fileName)
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }


    // to upload image
    @SuppressLint("Recycle")
    suspend fun UplaodProfileImage(context: Context, imageUri : Uri, token : String){

        UplaodProfileImage.emit(Resource.Loading())

        try {

            val mimeType = getMimeType(context, imageUri)
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val tempFile = createTempFileFromInputStream(context,inputStream, getFileName(context, imageUri))
            val requestFile = tempFile.asRequestBody(mimeType?.toMediaTypeOrNull())

            val body = MultipartBody.Part.createFormData("avatar", tempFile.name, requestFile)
            val response = Api.uploadProfileImage(token,body)

            if(response.body()!= null && response.body()!!.success){

                UplaodProfileImage.emit(Resource.Success(response.body()!!.data))

                TokenRepo.saveToken(
                    LocalStoredData(
                        true,
                        Localdata.value!!.Token,
                        UplaodProfileImage.value.data!!.fullname,
                        UplaodProfileImage.value.data!!.email,
                        UplaodProfileImage.value.data!!.is_email_verified,
                        UplaodProfileImage.value.data!!.age,
                        UplaodProfileImage.value.data!!.phone_number,
                        UplaodProfileImage.value.data!!.DOB,
                        UplaodProfileImage.value.data!!.avatar,
                        UplaodProfileImage.value.data!!.gender
                    )
                )


            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                UplaodProfileImage.emit(Resource.Error(errorResponse!!.message))

            }


        }catch (e:Exception){

            UplaodProfileImage.emit(Resource.Error("couldn't Upload Image Try Again"))
            Log.d("Auth repo", "UploadProfileImage: $e")

        }

    }




    suspend fun RemoveProfileImage(token: String){

        RemoveProfileImage.emit(Resource.Loading())

        try {

            val response = Api.removeProfileImage(token)

            if(response.body()!= null && response.body()!!.success){

                RemoveProfileImage.emit(Resource.Success(response.body()!!.data))

                TokenRepo.saveToken(
                    LocalStoredData(
                        true,
                        Localdata.value?.Token,
                        RemoveProfileImage.value.data!!.fullname,
                        RemoveProfileImage.value.data!!.email,
                        RemoveProfileImage.value.data!!.is_email_verified,
                        RemoveProfileImage.value.data!!.age,
                        RemoveProfileImage.value.data!!.phone_number,
                        RemoveProfileImage.value.data!!.DOB,
                        RemoveProfileImage.value.data!!.avatar,
                        RemoveProfileImage.value.data!!.gender
                    )
                )


            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }
                RemoveProfileImage.emit(Resource.Error(errorResponse!!.message))

            }

        }catch (e:Exception){

            RemoveProfileImage.emit(Resource.Error("Couldn't Remove Image Try Again"))
            Log.d("Auth repo", "RemoveProfileImage: $e")

        }




    }






    suspend fun SendOtp(email : SendOtpSend){

        SendOtpData.emit(Resource.Loading())

        try {

            val response = Api.sendOtp(email)


            if(response.body() != null && response.body()!!.success){

                SendOtpData.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                SendOtpData.emit(Resource.Error(errorResponse!!.message))

//

            }

        }catch(e:Exception){

            SendOtpData.emit(Resource.Error("Couldn't Send Otp Try Again"))
            Log.d("auth repo ", "SendOtp: $e")

        }


    }




    suspend fun VerifyOtp(token:String , data : VerifyEmailSend){

        VerifyOtpData.emit(Resource.Loading())

        try{

            val response = Api.verifyOtp(token,data)

            Log.d("auth -> repo", "VerifyOtp: ${response.body()}")

            if(response.body()!= null && response.body()!!.success){

                VerifyOtpData.emit(Resource.Success(response.body()!!.data))


            }else{



                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }


                VerifyOtpData.emit(Resource.Error(errorResponse!!.message))

            }


        }catch (e:Exception){

            Log.d("auth repo", "VerifyOtp: $e")
            VerifyOtpData.emit(Resource.Error("Couldn't Verify Otp Try Again"))

        }


    }








    suspend fun changePassword(token: String,data:ChangePasswordSend){

        ChangePasswordData.emit(Resource.Loading())

        try {

            val response = Api.changePassword(token,data)

            if(response.body() != null && response.body()!!.success){

                ChangePasswordData.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                ChangePasswordData.emit(Resource.Error(errorResponse!!.message))

            }



        }catch (e : Exception){

            ChangePasswordData.emit(Resource.Error("Couldn't Change password try again"))
            Log.d("auth repo", "changePassword: $e")

        }


    }






    suspend fun forgetPassword(email: String){

        ForgotPasswordData.emit(Resource.Loading())

        try{

            val response = Api.forgotPassword(SendOtpSend(
                email
            ))

            if(response.body()!= null && response.body()!!.success){

                ForgotPasswordData.emit(Resource.Success(response.body()!!.data))

            }else{


                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody?.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                    ForgotPasswordData.emit(Resource.Error(errorResponse!!.message))


            }

        }catch (e:Exception){

            ForgotPasswordData.emit(Resource.Error("Couldn't send Try Again"))
            Log.d("Auth repo", "forgetPassword: $e")

        }


    }








}