package com.heetox.app.ViewModel.ProductsVM

import androidx.lifecycle.viewModelScope
import com.heetox.app.Model.Product.CheckBarcodeResponse
import com.heetox.app.Model.Product.LikedProductRepsonse
import com.heetox.app.Model.Product.ProductbyBarcodeResponse
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
class ProductsViewModel @Inject constructor(private val productRepo : productRepository ) : UiEventViewModel(){


    private val _productByBarcodeData = MutableStateFlow<Resource<ProductbyBarcodeResponse>>(Resource.Nothing())
       val productByBarcodeData = _productByBarcodeData.asStateFlow()


    private val _checkBarcode = MutableStateFlow<Resource<CheckBarcodeResponse>>(Resource.Nothing())
        val checkBarcode = _checkBarcode.asStateFlow()


    private val _likedProductsData = MutableStateFlow<Resource<LikedProductRepsonse>>(Resource.Nothing())
        val likedProductsData = _likedProductsData.asStateFlow()



    fun getProductByBarcode(barcode : String, authorization: String){

        viewModelScope.launch(Dispatchers.IO){

            emitUiEvent(UiEvent.Loading(Action.ProductByBarcode))

            val response = productRepo.getProductByBarcode(barcode,authorization)

            _productByBarcodeData.value = response

            when (response) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.ProductByBarcode))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Couldn't Load Product",
                            Action.ProductByBarcode
                        )
                    )
                }

                else -> Unit
            }

        }

    }


    fun likeUnlikeProduct(barcode: String, authorization : String){

        viewModelScope.launch (Dispatchers.IO){

            emitUiEvent(UiEvent.Loading(Action.LikeUnlike))

            when (val response = productRepo.likeUnlikeProduct(barcode,authorization)) {
                is Resource.Success -> {
                    emitUiEvent(UiEvent.Success(Action.LikeUnlike))
                }

                is Resource.Error -> {
                    emitUiEvent(
                        UiEvent.Error(
                            response.error ?: "Oops! Try Again",
                            Action.LikeUnlike
                        )
                    )
                }

                else -> Unit
            }

        }

    }


    fun checkBarcode(barcode : String){

        viewModelScope.launch(Dispatchers.IO){

//            emitUiEvent(UiEvent.Loading(Action.CheckBarcode))

            _checkBarcode.value = Resource.Loading()

            val response = productRepo.checkBarcode(barcode)

            _checkBarcode.value = response

//            when (response) {
//                is Resource.Success -> {
//                    emitUiEvent(UiEvent.Success(Action.CheckBarcode))
//                }
//
//                is Resource.Error -> {
//                    emitUiEvent(
//                        UiEvent.Error(
//                            response.error ?: "Couldn't Load Product",
//                            Action.CheckBarcode
//                        )
//                    )
//                }
//
//                else -> Unit
//            }

        }

    }



    fun getLikedProducts(authorization : String){

      viewModelScope.launch(Dispatchers.IO){

         emitUiEvent(UiEvent.Loading(Action.LikedProducts))

          val response = productRepo.getLikedProducts(authorization)

          _likedProductsData.value = response

          when (response) {
              is Resource.Success -> {
                  emitUiEvent(UiEvent.Success(Action.LikedProducts))
              }

              is Resource.Error -> {
                  emitUiEvent(
                      UiEvent.Error(
                          response.error ?: "Oops! Try Again",
                          Action.LikedProducts
                      )
                  )
              }

              else -> Unit
          }

        }

    }

}