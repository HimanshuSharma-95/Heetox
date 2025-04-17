package com.heetox.app.Utils

sealed class UiEvent {
    data class Loading(val action: Action) : UiEvent()
    data class Success(val action: Action) : UiEvent()
    data class Error(val message: String, val action: Action) : UiEvent()
    data object Idle : UiEvent()
}

enum class Action {

    //authentication
    Register, Login, Logout, UpdateUserData, UpdateUserImage, RemoveUserProfile, SendOTP, VerifyOTP, ChangePassword
    , ForgotPassword,


    //products
    MostScanned, MainCategories , SubCategories , AlternativeProducts, SearchProduct , AllMostScanned,
    ProductByBarcode, LikeUnlike, LikedProducts,

    //consume
    ConsumeProduct,GetConsumedWeekData, GetConsumedDayData, DeleteConsumedProduct, GetConsumedMonthData

}

