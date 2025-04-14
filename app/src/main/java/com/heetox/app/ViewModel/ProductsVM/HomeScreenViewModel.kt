package com.heetox.app.ViewModel.ProductsVM

import androidx.lifecycle.viewModelScope
import com.heetox.app.Model.Product.CategoriesResponse
import com.heetox.app.Model.Product.MostScannedResponse
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
class HomeScreenViewModel @Inject constructor(private val productRepo : productRepository):UiEventViewModel(){


    private val _mostScannedProductsData = MutableStateFlow<Resource<List<MostScannedResponse>>>(Resource.Nothing())
    val mostScannedProductsData = _mostScannedProductsData.asStateFlow()


    private val _categoriesData = MutableStateFlow<Resource<CategoriesResponse>>(Resource.Nothing())
    val categoriesData = _categoriesData.asStateFlow()

    init {
        getCategories()
        getMostScannedProducts()
    }

    fun getMostScannedProducts(){

        viewModelScope.launch(Dispatchers.IO){

            emitUiEvent(UiEvent.Loading(Action.MostScanned))

            val response = productRepo.getMostScannedProducts()

            _mostScannedProductsData.value = response

            when(response){
                is Resource.Error -> {
                    emitUiEvent(UiEvent.Error(
                        response.error ?: "Couldn't Load Products",
                        Action.MostScanned
                        ))
                }
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.MostScanned))
                }
                else -> Unit
            }


        }

    }

    fun getCategories(){

        viewModelScope.launch (Dispatchers.IO){

            emitUiEvent(UiEvent.Loading(Action.MainCategories))

            val response = productRepo.getCategories()
            _categoriesData.value = response

            when(response){

                is Resource.Error -> {
                    emitUiEvent(UiEvent.Error(
                        response.error ?: "Couldn't Load Categories",
                        Action.MainCategories
                    ))
                }

                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.MainCategories))
                }

                else -> Unit
            }

        }

    }


}