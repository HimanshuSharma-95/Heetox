package com.heetox.app.ViewModel.Authentication

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
import com.heetox.app.Model.Authentication.UpdateUserProfileImageSend
import com.heetox.app.Model.Authentication.VerifyEmailSend
import com.heetox.app.Repository.AuthenticationRepository.AuthenticationRepository
import com.heetox.app.Repository.AuthenticationRepository.SharedPreferenceRepository
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.Resource
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ViewModel.UiEventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepo: AuthenticationRepository,
    private val tokenRepo: SharedPreferenceRepository
) : UiEventViewModel() {


    private val _registerUserData = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())
    val registerUserData = _registerUserData.asStateFlow()


    private val _loginUserData = MutableStateFlow<Resource<LoginData>>(Resource.Nothing())
    val loginUserData = _loginUserData.asStateFlow()


    private val _getCurrentUserData = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())
    val getCurrentUserData = _getCurrentUserData.asStateFlow()


    private val _localData = MutableStateFlow<LocalStoredData?>(null)
    val localData = _localData.asStateFlow()


    private val _logoutUser = MutableStateFlow<Resource<ApiResponse<Nothing>>>(Resource.Nothing())
    val logoutUser = _logoutUser.asStateFlow()


    private val _updateUserData =
        MutableStateFlow<Resource<UpdateProfleResponse>>(Resource.Nothing())
    val updateUserData = _updateUserData.asStateFlow()


    private val _uploadProfileImageData = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())
    val uploadProfileImageData = _uploadProfileImageData.asStateFlow()


    private val _removeProfileImage = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())
    val removeProfileImage = _removeProfileImage.asStateFlow()


    private val _sendOtpData = MutableStateFlow<Resource<String>>(Resource.Nothing())
    val sendOtpData = _sendOtpData.asStateFlow()


    private val _verifyOtpData = MutableStateFlow<Resource<AuthUser>>(Resource.Nothing())
    val verifyOtpData = _verifyOtpData.asStateFlow()


    private val _changePasswordData = MutableStateFlow<Resource<String>>(Resource.Nothing())
    val changePasswordData = _changePasswordData.asStateFlow()


    private val _forgotPasswordData = MutableStateFlow<Resource<String>>(Resource.Nothing())
    val forgotPasswordData = _forgotPasswordData.asStateFlow()


    init {
        checkIfUserAlreadyLoggedIn()
    }

    private fun checkIfUserAlreadyLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            val storedData = tokenRepo.getToken()

            if (storedData?.Token != null) {
                _localData.value = storedData
                getCurrentUser(storedData.Token)
            } else {
                _localData.value = null
            }
        }
    }

    fun registerUser(userData: RegisterSend) {

        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.Register))

            val response = authRepo.registerUser(userData)

            _registerUserData.value = response

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.Register))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Couldn't Register",
                           Action.Register
                        )
                    )
                }

                else -> {}
            }
        }

    }

    fun loginUser(userData: LoginSend) {

        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.Login))

            val response = authRepo.loginUser(userData)

            _loginUserData.value = response

            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        _localData.value = tokenRepo.saveToken(
                            LocalStoredData(
                                Status = true,
                                Token = it.accesstoken,
                                Name = it.user.fullname,
                                Email = it.user.email,
                                EmailStatus = it.user.is_email_verified,
                                Age = it.user.age,
                                Phone = it.user.phone_number,
                                Dob = it.user.DOB,
                                avatar = it.user.avatar,
                                gender = it.user.gender
                            )
                        )
                        getCurrentUser(it.accesstoken)
                    }
                    emitUiEvent(UiEvent.Success(Action.Login))
                }

                is Resource.Error -> {
                    tokenRepo.removeToken()
                    _localData.value = null
                    emitUiEvent(UiEvent.Error(response.error ?: "Couldn't Login", Action.Login))
                }

                else -> {}
            }
        }
    }

    private fun getCurrentUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {

            _getCurrentUserData.value = Resource.Loading()

            val response = authRepo.getCurrentUser(token)

            _getCurrentUserData.value = response

            if (response.data == null && tokenRepo.getToken() == null) {
                tokenRepo.removeToken()
                _localData.value = null
            }

        }
    }

    fun logoutUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.Logout))

            val response = authRepo.logoutUser(token)
            _logoutUser.value = response

            if (response is Resource.Success) {
                _localData.value = null
                tokenRepo.removeToken()
                emitUiEvent(UiEvent.Success(Action.Logout))

            } else if (response is Resource.Error) {
                emitUiEvent(UiEvent.Error(response.error ?: "Logout failed", Action.Logout))
            }

        }
    }

    fun updateUser(token: String, userdata: UpdateProfileSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.UpdateUserData))

            val response = authRepo.updateProfile(token, userdata)
            _updateUserData.value = response

            if (response is Resource.Success) {
                val updatedUserData = response.data
                val currentLocalData = tokenRepo.getToken()

                if (updatedUserData != null && currentLocalData != null) {
                    _localData.value = currentLocalData.copy(
                        Name = updatedUserData.fullname,
                        Email = updatedUserData.email,
                        Dob = updatedUserData.DOB,
                        gender = updatedUserData.gender,
                        Phone = updatedUserData.phone_number
                    )
                    tokenRepo.saveToken(_localData.value!!)
                }

                emitUiEvent(UiEvent.Success(Action.UpdateUserData))

            } else if (response is Resource.Error) {
                emitUiEvent(
                    UiEvent.Error(
                        response.error ?: "Couldn't Update User",
                        Action.UpdateUserData
                    )
                )
            }
        }
    }



    fun updateProfileImage(data:UpdateUserProfileImageSend) {
        viewModelScope.launch(Dispatchers.IO) {


            emitUiEvent(UiEvent.Loading(Action.UpdateUserImage))

            val response = authRepo.uploadProfileImage(data)
            _uploadProfileImageData.value = response

            if (response is Resource.Success) {
                val updatedData = response.data
                val currentLocalData = tokenRepo.getToken()

                if (updatedData != null && currentLocalData != null) {
                    _localData.value = currentLocalData.copy(
                        avatar = updatedData.avatar
                    )
                    _localData.value?.let { tokenRepo.saveToken(it) }
                }

                emitUiEvent(UiEvent.Success(Action.UpdateUserImage))

            } else if (response is Resource.Error) {
                emitUiEvent(
                    UiEvent.Error(
                        response.error ?: "Failed to upload image",
                        Action.UpdateUserImage
                    )
                )
            }
        }
    }

    fun removeProfileImage(token: String) {

        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.RemoveUserProfile))

            val response = authRepo.removeProfileImage(token)
            _removeProfileImage.value = response

            if (response is Resource.Success) {
                val localData = tokenRepo.getToken()

                if (localData != null) {
                    _localData.value = localData.copy(avatar = "")
                    _localData.value?.let { tokenRepo.saveToken(it) }
                }

                emitUiEvent(UiEvent.Success(Action.RemoveUserProfile))

            } else if (response is Resource.Error) {
                emitUiEvent(
                    UiEvent.Error(
                        response.error ?: "Failed to remove profile image",
                        Action.RemoveUserProfile
                    )
                )
            }
        }
    }

    fun sendOtp(email: SendOtpSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.SendOTP))

            val response = authRepo.sendOtp(email)

            if (response is Resource.Success) {
                emitUiEvent(UiEvent.Success(Action.SendOTP))
            } else if (response is Resource.Error) {
                emitUiEvent(UiEvent.Error(response.error ?: "Failed to send OTP", Action.SendOTP))
            }
        }
    }

    fun verifyOtp(token: String, data: VerifyEmailSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.VerifyOTP))

            val response = authRepo.verifyOtp(token, data)

            if (response is Resource.Success) {
                val localData = tokenRepo.getToken()
                if (localData != null) {
                    _localData.value = localData.copy(EmailStatus = true)
                    _localData.value?.let { tokenRepo.saveToken(it) }
                }
                emitUiEvent(UiEvent.Success(Action.VerifyOTP))
            } else if (response is Resource.Error) {
                emitUiEvent(
                    UiEvent.Error(
                        response.error ?: "Failed to verify OTP",
                        Action.VerifyOTP
                    )
                )
            }
        }
    }

    fun changePassword(token: String, data: ChangePasswordSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.ChangePassword))

            val response = authRepo.changePassword(token, data)

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.ChangePassword))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Failed to change password",
                            Action.ChangePassword
                        )
                    )
                }

                else -> {}
            }
        }
    }

    fun forgotPassword(email: String) {

        viewModelScope.launch(Dispatchers.IO) {
            emitUiEvent(UiEvent.Loading(Action.ForgotPassword))

            val response = authRepo.forgetPassword(email)

            _forgotPasswordData.value = response

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.ForgotPassword))
                }

                is Resource.Error -> {
                    emitUiEvent(UiEvent.Error(response.error.toString(), Action.ForgotPassword))
                }

                else -> {}
            }
        }
    }

}
