package com.heetox.app.Repository.AuthenticationRepository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.heetox.app.ApiInterface.AuthenticationInterface
import com.heetox.app.Model.ApiResponse
import com.heetox.app.Model.Authentication.AuthUser
import com.heetox.app.Model.Authentication.ChangePasswordSend
import com.heetox.app.Model.Authentication.LoginData
import com.heetox.app.Model.Authentication.LoginSend
import com.heetox.app.Model.Authentication.RegisterSend
import com.heetox.app.Model.Authentication.SendOtpSend
import com.heetox.app.Model.Authentication.UpdateProfileFunctionSend
import com.heetox.app.Model.Authentication.UpdateProfileSend
import com.heetox.app.Model.Authentication.UpdateProfleResponse
import com.heetox.app.Model.Authentication.UpdateUserProfileImageSend
import com.heetox.app.Model.Authentication.VerifyEmailSend
import com.heetox.app.Utils.Resource
import com.heetox.app.Utils.extractErrorMessage
import kotlinx.coroutines.CancellationException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject


class AuthenticationRepository @Inject constructor(
    private val authApi: AuthenticationInterface,
) {


    suspend fun registerUser(userData: RegisterSend): Resource<AuthUser> {

        try {
            val response = authApi.registerUser(userData)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()?.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception) {


            if (e is CancellationException) {
                throw e
            }

            return Resource.Error("Couldn't Register Try again")
//            Log.d("auth repo ->", "Registeruser: $e")
        }
    }


    suspend fun loginUser(userData: LoginSend): Resource<LoginData> {

        try {
            val response = authApi.loginUser(userData)

            if (response.body() != null && response.body()!!.success) {

                response.body()?.data?.let { getCurrentUser(it.accesstoken) }

                return Resource.Success(response.body()?.data)

            } else {

                return Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception) {

            return Resource.Error("Couldn't Register Try again")
//            Log.d("auth repo ->", "Loginuser: $e")

        }

    }


    suspend fun getCurrentUser(token: String): Resource<AuthUser> {

        try {

            val response = authApi.getCurrentUser(token)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()?.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception) {

            return Resource.Error("Couldn't get Current User Try again")

//            Log.d("auth repo ->", "GetCurentUser: $e")
        }

    }


    suspend fun logoutUser(token: String): Resource<ApiResponse<Nothing>> {

        try {

            val response = authApi.logoutuser(token)

            return if (response.body()!!.success && response.body() != null) {

                Resource.Success(response.body())

            } else {

                Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

            return Resource.Error("Couldn't Logout Try again")
//            Log.d("auht repo ->", "logoutUser: $e")

        }


    }


    suspend fun updateProfile(
        token: String,
        userData: UpdateProfileSend
    ): Resource<UpdateProfleResponse> {

        try {

            val response = authApi.updateProfile(token, userData)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()?.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception) {

            return Resource.Error("Couldn't get Current User Try again")
//            Log.d("catch ->", "GetUpdate: $e")

        }

    }


    // to get file image file type (e.g., "image/png", "image/jpeg")
    private fun getMimeType(context: Context, uri: Uri): String? {
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
    private fun createTempFileFromInputStream(
        context: Context,
        inputStream: InputStream?,
        fileName: String
    ): File {
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
    suspend fun uploadProfileImage(
        data : UpdateUserProfileImageSend
    ): Resource<AuthUser> {

        try {

            val mimeType = getMimeType(data.context,data.uri)
            val inputStream = data.context.contentResolver.openInputStream(data.uri)
            val tempFile =
                createTempFileFromInputStream(data.context, inputStream, getFileName(data.context,data.uri))
            val requestFile = tempFile.asRequestBody(mimeType?.toMediaTypeOrNull())

            val body = MultipartBody.Part.createFormData("avatar", tempFile.name, requestFile)
            val response = authApi.uploadProfileImage(data.token, body)

            if (response.body() != null && response.body()!!.success) {

                return Resource.Success(response.body()!!.data)


            } else {
                return Resource.Error(extractErrorMessage(response))
            }

        } catch (e: Exception) {

            return Resource.Error("couldn't Upload Image Try Again")
//            Log.d("Auth repo", "UploadProfileImage: $e")

        }

    }


    suspend fun removeProfileImage(token: String): Resource<AuthUser> {

        try {
            val response = authApi.removeProfileImage(token)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()?.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception) {

            return Resource.Error("Couldn't Remove Image Try Again")
//            Log.d("Auth repo", "RemoveProfileImage: $e")

        }
    }


    suspend fun sendOtp(email: SendOtpSend): Resource<String> {

        try {
            val response = authApi.sendOtp(email)


            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

            return Resource.Error("Couldn't Send Otp Try Again")
//            Log.d("auth repo ", "SendOtp: $e")
        }

    }


    suspend fun verifyOtp(token: String, data: VerifyEmailSend): Resource<AuthUser> {

        try {
            val response = authApi.verifyOtp(token, data)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

//            Log.d("auth repo", "VerifyOtp: $e")
            return Resource.Error("Couldn't Verify Otp Try Again")

        }

    }


    suspend fun changePassword(token: String, data: ChangePasswordSend): Resource<String> {

        try {

            val response = authApi.changePassword(token, data)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }


        } catch (e: Exception) {

            return Resource.Error("Couldn't Change password try again")
//            Log.d("auth repo", "changePassword: $e")

        }


    }


    suspend fun forgetPassword(email: String): Resource<String> {

        try {
            val response = authApi.forgotPassword(SendOtpSend(email))

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))
            }

        } catch (e: Exception) {

            return Resource.Error("Couldn't send Try Again")
//            Log.d("Auth repo", "forgetPassword: $e")

        }

    }


}