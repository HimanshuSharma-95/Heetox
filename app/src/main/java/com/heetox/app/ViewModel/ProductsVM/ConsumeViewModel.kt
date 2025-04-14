package com.heetox.app.ViewModel.ProductsVM

import androidx.lifecycle.viewModelScope
import com.heetox.app.Model.Product.ConsumeProductResponse
import com.heetox.app.Model.Product.ConsumedDayDataResponse
import com.heetox.app.Model.Product.ConsumedMonthDataResponse
import com.heetox.app.Model.Product.ConsumedWeekDataResponse
import com.heetox.app.Repository.ProductRepository.productRepository
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
class ConsumeViewModel @Inject constructor(private val productRepo : productRepository):UiEventViewModel(){


    private val _consumeProductData = MutableStateFlow<Resource<ConsumeProductResponse>>(Resource.Nothing())
    val consumeProductData = _consumeProductData.asStateFlow()


    private val _consumedWeekData = MutableStateFlow<Resource<ConsumedWeekDataResponse>>(Resource.Nothing())
        val consumedWeekData = _consumedWeekData.asStateFlow()

    private val _consumedDayData = MutableStateFlow<Resource<ConsumedDayDataResponse>>(Resource.Nothing())
        val consumedDayData = _consumedDayData.asStateFlow()


    private val _deleteConsumedProductData = MutableStateFlow<Resource<ConsumeProductResponse>>(Resource.Nothing())
        val deleteConsumedProductData = _deleteConsumedProductData.asStateFlow()


    private val _consumedMonthData = MutableStateFlow<Resource<ConsumedMonthDataResponse>>(Resource.Nothing())
        val consumedMonthData = _consumedMonthData.asStateFlow()




    fun consumeProduct(token: String, barcode: String, size: Float) {

        viewModelScope.launch(Dispatchers.IO) {
            emitUiEvent(UiEvent.Loading(Action.ConsumeProduct))

            val response = productRepo.consumeProduct(barcode, size, token)

            _consumeProductData.value = response

            when (response) {
                is Resource.Success -> emitUiEvent(UiEvent.Success(Action.ConsumeProduct))
                is Resource.Error -> emitUiEvent(
                    UiEvent.Error(response.error ?: "Something went wrong", Action.ConsumeProduct)
                )
                else -> Unit
            }
        }
    }




    fun getConsumedWeekData(token: String, week: String) {
        viewModelScope.launch(Dispatchers.IO) {
            emitUiEvent(UiEvent.Loading(Action.GetConsumedWeekData))

            val response = productRepo.getConsumedWeekData(week, token)

            _consumedWeekData.value = response

            when (response) {
                is Resource.Success -> emitUiEvent(UiEvent.Success(Action.GetConsumedWeekData))
                is Resource.Error -> emitUiEvent(
                    UiEvent.Error(response.error ?: "Couldn't fetch weekly data", Action.GetConsumedWeekData)
                )
                else -> Unit
            }
        }
    }



    fun getConsumedDayData(token: String, day: String) {
        viewModelScope.launch(Dispatchers.IO) {
            emitUiEvent(UiEvent.Loading(Action.GetConsumedDayData))

            val response = productRepo.getConsumedDayData(token, day)

            _consumedDayData.value = response

            when (response) {
                is Resource.Success -> emitUiEvent(UiEvent.Success(Action.GetConsumedDayData))
                is Resource.Error -> emitUiEvent(
                    UiEvent.Error(response.error ?: "Couldn't fetch daily data", Action.GetConsumedDayData)
                )
                else -> Unit
            }
        }
    }



    fun deleteConsumedProduct(token: String, barcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            emitUiEvent(UiEvent.Loading(Action.DeleteConsumedProduct))

            val response = productRepo.deleteConsumedProduct(token, barcode)

            _deleteConsumedProductData.value = response

            when (response) {
                is Resource.Success -> emitUiEvent(UiEvent.Success(Action.DeleteConsumedProduct))
                is Resource.Error -> emitUiEvent(
                    UiEvent.Error(response.error ?: "Couldn't delete product", Action.DeleteConsumedProduct)
                )
                else -> Unit
            }
        }
    }



    fun getConsumedMonthData(token: String, month: String){

        viewModelScope.launch(Dispatchers.IO) {
            emitUiEvent(UiEvent.Loading(Action.GetConsumedMonthData))

            val response = productRepo.getConsumeMonthData(token, month)

            _consumedMonthData.value = response

            when (response) {
                is Resource.Success -> emitUiEvent(UiEvent.Success(Action.GetConsumedMonthData))
                is Resource.Error -> emitUiEvent(
                    UiEvent.Error(response.error ?: "Couldn't fetch monthly data", Action.GetConsumedMonthData)
                )
                else -> Unit
            }
        }
    }




}