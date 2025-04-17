package com.heetox.app.ViewModel.Authentication

import androidx.lifecycle.viewModelScope
import com.heetox.app.Model.Authentication.ChangePasswordSend
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Authentication.LoginSend
import com.heetox.app.Model.Authentication.RegisterSend
import com.heetox.app.Model.Authentication.SendOtpSend
import com.heetox.app.Model.Authentication.UpdateProfileSend
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



    private val _localData = MutableStateFlow<LocalStoredData?>(null)
    val localData = _localData.asStateFlow()


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

            when (val response = authRepo.registerUser(userData)) {
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

                else -> Unit
            }
        }

    }

    fun loginUser(userData: LoginSend) {

        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.Login))

            when (val response = authRepo.loginUser(userData)) {
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

            val response = authRepo.getCurrentUser(token)

            if (response.data == null && tokenRepo.getToken() == null) {
                tokenRepo.removeToken()
                _localData.value = null
            }

        }
    }

    fun logoutUser(token: String) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.Logout))

            when(val response = authRepo.logoutUser(token)){
                is Resource.Error ->{
                    emitUiEvent(UiEvent.Error(response.error ?: "Logout failed", Action.Logout))
                }
                is Resource.Success -> {
                    _localData.value = null
                    tokenRepo.removeToken()
                    emitUiEvent(UiEvent.Success(Action.Logout))
                }
                else -> Unit
            }

        }
    }

    fun updateUser(token: String, userdata: UpdateProfileSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.UpdateUserData))

            when(val response = authRepo.updateProfile(token, userdata)){
                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Couldn't Update User",
                            Action.UpdateUserData
                        )
                    )
                }
                is Resource.Success -> {
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

                }
                else -> Unit
            }
        }
    }

    fun updateProfileImage(data:UpdateUserProfileImageSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.UpdateUserImage))

            when(val response = authRepo.uploadProfileImage(data)){
                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Failed to upload image",
                            Action.UpdateUserImage
                        )
                    )
                }
                is Resource.Success -> {
                    val updatedData = response.data
                    val currentLocalData = tokenRepo.getToken()

                    if (updatedData != null && currentLocalData != null) {
                        _localData.value = currentLocalData.copy(
                            avatar = updatedData.avatar
                        )
                        _localData.value?.let { tokenRepo.saveToken(it) }
                    }

                    emitUiEvent(UiEvent.Success(Action.UpdateUserImage))

                }
                else -> Unit
            }

        }
    }

    fun removeProfileImage(token: String) {

        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.RemoveUserProfile))

            when(val response = authRepo.removeProfileImage(token)){
                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Failed to remove profile image",
                            Action.RemoveUserProfile
                        )
                    )
                }
                is Resource.Success -> {
                    val localData = tokenRepo.getToken()

                    if (localData != null) {
                        _localData.value = localData.copy(avatar = "")
                        _localData.value?.let { tokenRepo.saveToken(it) }
                    }

                    emitUiEvent(UiEvent.Success(Action.RemoveUserProfile))

                }
                else -> Unit
            }

        }
    }

    fun sendOtp(email: SendOtpSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.SendOTP))

            when(val response = authRepo.sendOtp(email)){
                is Resource.Error -> {
                    emitUiEvent(UiEvent.Error(response.error ?: "Failed to send OTP", Action.SendOTP))
                }
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.SendOTP))
                }
                else -> Unit
            }

        }
    }

    fun verifyOtp(token: String, data: VerifyEmailSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.VerifyOTP))

            when(val response = authRepo.verifyOtp(token, data)){
                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Failed to verify OTP",
                            Action.VerifyOTP
                        )
                    )
                }
                is Resource.Success -> {
                    val localData = tokenRepo.getToken()
                    if (localData != null) {
                        _localData.value = localData.copy(EmailStatus = true)
                        _localData.value?.let { tokenRepo.saveToken(it) }
                    }
                    emitUiEvent(UiEvent.Success(Action.VerifyOTP))
                }
                else -> Unit
            }

        }
    }

    fun changePassword(token: String, data: ChangePasswordSend) {
        viewModelScope.launch(Dispatchers.IO) {

            emitUiEvent(UiEvent.Loading(Action.ChangePassword))

            when (val response = authRepo.changePassword(token, data)) {
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

            when (val response = authRepo.forgetPassword(email)) {
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
