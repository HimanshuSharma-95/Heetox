package com.heetox.app.ApiInterface

import com.heetox.app.Model.ApiResponse
import com.heetox.app.Model.Authentication.AuthUser
import com.heetox.app.Model.Authentication.ChangePasswordSend
import com.heetox.app.Model.Authentication.LoginData
import com.heetox.app.Model.Authentication.LoginSend
import com.heetox.app.Model.Authentication.RegisterSend
import com.heetox.app.Model.Authentication.SendOtpSend
import com.heetox.app.Model.Authentication.UpdateProfileSend
import com.heetox.app.Model.Authentication.UpdateProfleResponse
import com.heetox.app.Model.Authentication.VerifyEmailSend
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthenticationInterface {


    @POST("users/register")
    suspend fun registerUser(@Body userData: RegisterSend) : Response<ApiResponse<AuthUser>>

    @POST("users/login")
    suspend fun loginUser(@Body userData : LoginSend) : Response<ApiResponse<LoginData>>

    @POST("users/logout")
    suspend fun logoutuser(@Header("Authorization") Authorization : String) : Response<ApiResponse<Nothing>>

    @GET("users/current-user")
    suspend fun getCurrentUser(@Header("Authorization") Authorization : String) : Response<ApiResponse<AuthUser>>

    @PATCH("users/update-account")
    suspend fun updateProfile(@Header("Authorization") Authorization: String , @Body userData : UpdateProfileSend ) : Response<ApiResponse<UpdateProfleResponse>>

    @Multipart
    @PATCH("users/avatar")
    suspend fun uploadProfileImage(@Header("Authorization") Authorization: String ,@Part avatar: MultipartBody.Part ) : Response<ApiResponse<AuthUser>>

    @PATCH("users/removeavatar")
    suspend fun removeProfileImage(@Header("Authorization") Authorization: String ) : Response<ApiResponse<AuthUser>>

    @POST("users/sendotp")
    suspend fun sendOtp(@Body email : SendOtpSend) : Response<ApiResponse<String>>

    @POST("users/verifyemail")
    suspend fun verifyOtp(@Header("Authorization") Authorization: String, @Body Data : VerifyEmailSend ) : Response<ApiResponse<AuthUser>>

    @POST("users/change-password")
    suspend fun changePassword(@Header("Authorization") Authorization: String , @Body data : ChangePasswordSend) : Response<ApiResponse<String>>

    @POST("users/forgotpassword")
    suspend fun forgotPassword(@Body email : SendOtpSend) : Response<ApiResponse<String>>


}