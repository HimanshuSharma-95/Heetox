package com.heetox.app.ViewModel.ProductsVM

import androidx.lifecycle.viewModelScope
import com.heetox.app.Model.Product.AlternateResponseItem
import com.heetox.app.Model.Product.MostScannedResponse
import com.heetox.app.Model.Product.SubCategoriesResponse
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
class ProductListViewModel @Inject constructor(private val productRepo : productRepository):UiEventViewModel(){


    private val _subCategoriesData = MutableStateFlow<Resource<SubCategoriesResponse>>(Resource.Nothing())
    val subCategoriesData = _subCategoriesData.asStateFlow()


    private val _alternativeProductData = MutableStateFlow<Resource<ArrayList<AlternateResponseItem>>>(Resource.Nothing())
    val alternativeProductData = _alternativeProductData.asStateFlow()


    private val _searchData = MutableStateFlow<Resource<ArrayList<AlternateResponseItem>>>(Resource.Nothing())
    val searchData = _searchData.asStateFlow()


    private val _mostScannedProductsData = MutableStateFlow<Resource<List<MostScannedResponse>>>(Resource.Nothing())
    val mostScannedProductsData = _mostScannedProductsData.asStateFlow()



    fun getSubCategory(category : String){

        viewModelScope.launch(Dispatchers.IO){

            emitUiEvent(UiEvent.Loading(Action.SubCategories))

            val response = productRepo.getSubCategory(category)
            _subCategoriesData.value = response

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.SubCategories))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Couldn't Load Sub Categories",
                            Action.SubCategories
                        )
                    )
                }
                else -> Unit
            }

        }

    }

    fun getAlternativeProducts(category : String, authorization: String){

        viewModelScope.launch(Dispatchers.IO){

           emitUiEvent(UiEvent.Loading(Action.AlternativeProducts))

            val response = productRepo.getAlternativeProducts(category,authorization)

            _alternativeProductData.value = response

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.AlternativeProducts))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Couldn't Load Products",
                            Action.AlternativeProducts
                        )
                    )
                }
                else -> Unit
            }

        }

    }


    fun searchProducts(query : String, authorization: String){

        viewModelScope.launch(Dispatchers.IO){

            emitUiEvent(UiEvent.Loading(Action.SearchProduct))

            val response = productRepo.getSearch(query,authorization)

            _searchData.value = response

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.SearchProduct))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Couldn't Load Products",
                            Action.SearchProduct
                        )
                    )
                }
                else -> Unit
            }

        }

    }


    fun getMostScannedProducts(){

        viewModelScope.launch(Dispatchers.IO){

            emitUiEvent(UiEvent.Loading(Action.AllMostScanned))

            val response = productRepo.getMostScannedProducts()

            _mostScannedProductsData.value = response

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.AllMostScanned))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Couldn't Load Products",
                            Action.AllMostScanned
                        )
                    )
                }
                else -> Unit
            }

        }

    }

}