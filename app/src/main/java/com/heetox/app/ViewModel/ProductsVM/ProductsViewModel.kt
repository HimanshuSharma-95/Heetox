package com.heetox.app.ViewModel.ProductsVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heetox.app.Model.Product.AlternateResponseItem
import com.heetox.app.Model.Product.CategoriesResponse
import com.heetox.app.Model.Product.CheckBarcodeResponse
import com.heetox.app.Model.Product.ConsumeProductResponse
import com.heetox.app.Model.Product.ConsumedDayDataResponse
import com.heetox.app.Model.Product.ConsumedMonthDataResponse
import com.heetox.app.Model.Product.ConsumedWeekDataResponse
import com.heetox.app.Model.Product.LikeUnlikeResponse
import com.heetox.app.Model.Product.LikedProductRepsonse
import com.heetox.app.Model.Product.MostScannedResponse
import com.heetox.app.Model.Product.ProductbyBarcodeResponse
import com.heetox.app.Model.Product.SubCategoriesResponse
import com.heetox.app.Repository.ProductRepository.productRepository
import com.heetox.app.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductsViewModel @Inject constructor(private val ProductRepo : productRepository ) : ViewModel(){


    val MostScannedProductsData : StateFlow<Resource<List<MostScannedResponse>>>
        get() = ProductRepo.accessMostScannedProductData


    val CategoriesData : StateFlow<Resource<CategoriesResponse>>
        get() = ProductRepo.accessCategoriesData


    val ProductByBarcodeData : StateFlow<Resource<ProductbyBarcodeResponse>>
        get() = ProductRepo.accessProductByBarcodeData

    val LikeUnlikeProductData : StateFlow<Resource<LikeUnlikeResponse>>
        get() = ProductRepo.accessLikedProductData

    val AlternativeProductData : StateFlow<Resource<ArrayList<AlternateResponseItem>>>
        get() = ProductRepo.accessAlternateProduct


    val SearchData : StateFlow<Resource<ArrayList<AlternateResponseItem>>>
        get() = ProductRepo.accessSearchData


    val checkbarcode : StateFlow<Resource<CheckBarcodeResponse>>
        get() = ProductRepo.accessCheckBarcodeData


    val LikedProductsData : StateFlow<Resource<LikedProductRepsonse>>
        get() = ProductRepo.accessGetLikedProductData

    val ConsumeProductData : StateFlow<Resource<ConsumeProductResponse>>
        get() = ProductRepo.accessConsumeProductData

    val ConsumedWeekData : StateFlow<Resource<ConsumedWeekDataResponse>>
        get() = ProductRepo.accessConsumedWeekData

    val ConsumedDayData : StateFlow<Resource<ConsumedDayDataResponse>>
        get() = ProductRepo.accessConsumedDayData


    val DeleteConsumedProductData : StateFlow<Resource<ConsumeProductResponse>>
        get() = ProductRepo.accessDeleteConsumedProduct


    val ConsumedMonthData : StateFlow<Resource<ConsumedMonthDataResponse>>
        get() = ProductRepo.accessConsumedMonthData

    val SubCategoriesData : StateFlow<Resource<SubCategoriesResponse>>
        get() = ProductRepo.accessSubCategoryData


    private val _mainCategory = MutableStateFlow<String>("")
    val mainCategory: StateFlow<String> = _mainCategory


    fun setMainCategory(newSubCategory: String) {
        _mainCategory.value = newSubCategory

    }



    private val _subcategory = MutableStateFlow<String>("")
    val subcategory: StateFlow<String> = _subcategory


    fun setSubcategory(newSubCategory: String) {
            _subcategory.value = newSubCategory

    }



    fun getmostscannedproducts(){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.getMostScannedProducts()

        }

    }

    fun getcategories(){

        viewModelScope.launch (Dispatchers.IO){

            ProductRepo.getCategories()

        }

    }

    fun getproductbybarcode(barcode : String,Authorization: String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.getProductByBarcode(barcode,Authorization)

        }

    }

    fun likeunlikeproduct(barcode: String,Authorization : String){

        viewModelScope.launch (Dispatchers.IO){

            ProductRepo.LikeUnlikeProduct(barcode,Authorization)

        }

    }


    fun getalternativeproducts(category : String,Authorization: String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.getAlternativeProducts(category,Authorization)

        }

    }


    fun searchproducts(query : String,Authorization: String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.getSearch(query,Authorization)

        }

    }



    fun checkbarcode(barcode : String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.CheckBarcode(barcode)

        }

    }


  fun getlikedproducts(authorization : String){

      viewModelScope.launch(Dispatchers.IO){

          ProductRepo.getLikedProducts(authorization)

        }

    }


   fun consumeproduct(token : String,barcode:String,size:Float){

     viewModelScope.launch(Dispatchers.IO){

         ProductRepo.ConsumeProduct(barcode,size,token)

     }


    }


    fun getConsumedWeekData(token:String,week:String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.GetConsumedWeekData(week,token)

        }

    }




    fun getConsumedDayData(token:String,day:String){

        viewModelScope.launch(Dispatchers.IO) {


            ProductRepo.GetConsumedDayData(token,day)
        }


        }



    fun deleteConsumedProduct(token:String,barcode:String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.DeleteConsumedProduct(token,barcode)
        }


    }



    fun getConsumedMonthData(token:String,month:String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.GetMonthData(token,month)

        }

    }


    fun getSubCategory(category : String){

        viewModelScope.launch(Dispatchers.IO){

            ProductRepo.GetSubCategory(category)

        }

    }


    fun clearAlternativeProductList(){
        viewModelScope.launch {
            ProductRepo.clearAlternativeProductList()
        }
    }



}