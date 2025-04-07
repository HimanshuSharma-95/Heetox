package com.heetox.app.Repository.ProductRepository

import com.google.gson.Gson
import com.heetox.app.ApiInterface.ProductInterface
import com.heetox.app.Model.Authentication.ErrorResponse
import com.heetox.app.Model.Product.AlternateResponseItem
import com.heetox.app.Model.Product.CategoriesResponse
import com.heetox.app.Model.Product.CheckBarcodeResponse
import com.heetox.app.Model.Product.ConsumeProductResponse
import com.heetox.app.Model.Product.ConsumeProductSend
import com.heetox.app.Model.Product.ConsumedDayDataResponse
import com.heetox.app.Model.Product.ConsumedMonthDataResponse
import com.heetox.app.Model.Product.ConsumedWeekDataResponse
import com.heetox.app.Model.Product.LikeUnlikeResponse
import com.heetox.app.Model.Product.LikedProductRepsonse
import com.heetox.app.Model.Product.MostScannedResponse
import com.heetox.app.Model.Product.ProductbyBarcodeResponse
import com.heetox.app.Model.Product.SubCategoriesResponse
import com.heetox.app.Utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class productRepository @Inject constructor(private val ProductApi : ProductInterface) {


    private var MostScannedProductData = MutableStateFlow<Resource<List<MostScannedResponse>>>(Resource.Nothing())

    val accessMostScannedProductData : StateFlow<Resource<List<MostScannedResponse>>>
        get() = MostScannedProductData


    private  var CategoriesData = MutableStateFlow<Resource<CategoriesResponse>>(Resource.Nothing())

    val accessCategoriesData : StateFlow<Resource<CategoriesResponse>>
        get() = CategoriesData

    private var ProductbyBarcodeData = MutableStateFlow<Resource<ProductbyBarcodeResponse>>(Resource.Nothing())

    val accessProductByBarcodeData : StateFlow<Resource<ProductbyBarcodeResponse>>
        get() = ProductbyBarcodeData


    private  var LikeUnlikeProductData = MutableStateFlow<Resource<LikeUnlikeResponse>>(Resource.Nothing())

    val accessLikedProductData : StateFlow<Resource<LikeUnlikeResponse>>
        get() = LikeUnlikeProductData


    private var AlternateProductData = MutableStateFlow<Resource<ArrayList<AlternateResponseItem>>>(Resource.Nothing())

    val accessAlternateProduct : StateFlow<Resource<ArrayList<AlternateResponseItem>>>
        get() = AlternateProductData




    private var SearchData = MutableStateFlow<Resource<ArrayList<AlternateResponseItem>>>(Resource.Nothing())

    val accessSearchData : StateFlow<Resource<ArrayList<AlternateResponseItem>>>
        get() = SearchData




    private var CheckBarcodeData = MutableStateFlow<Resource<CheckBarcodeResponse>>(Resource.Nothing())

    val accessCheckBarcodeData : StateFlow<Resource<CheckBarcodeResponse>>
        get() = CheckBarcodeData




    private var getLikedProductData = MutableStateFlow<Resource<LikedProductRepsonse>>(Resource.Nothing())

    val accessGetLikedProductData : StateFlow<Resource<LikedProductRepsonse>>
        get() = getLikedProductData



    private var ConsumeProduct = MutableStateFlow<Resource<ConsumeProductResponse>>(Resource.Nothing())

    val accessConsumeProductData : StateFlow<Resource<ConsumeProductResponse>>
        get() = ConsumeProduct




    private var ConsumedWeekData = MutableStateFlow<Resource<ConsumedWeekDataResponse>>(Resource.Nothing())

    val accessConsumedWeekData : StateFlow<Resource<ConsumedWeekDataResponse>>
        get() = ConsumedWeekData




    private var ConsumedDayData = MutableStateFlow<Resource<ConsumedDayDataResponse>>(Resource.Nothing())

    val accessConsumedDayData : StateFlow<Resource<ConsumedDayDataResponse>>
        get() = ConsumedDayData



    private var DeleteConsumedProduct = MutableStateFlow<Resource<ConsumeProductResponse>>(Resource.Nothing())

    val accessDeleteConsumedProduct : StateFlow<Resource<ConsumeProductResponse>>
        get() = DeleteConsumedProduct



    private var ConsumedMonthData = MutableStateFlow<Resource<ConsumedMonthDataResponse>>(Resource.Nothing())

    val accessConsumedMonthData : StateFlow<Resource<ConsumedMonthDataResponse>>
        get() = ConsumedMonthData



    private var SubCategoryData = MutableStateFlow<Resource<SubCategoriesResponse>>(Resource.Nothing())

    val accessSubCategoryData : StateFlow<Resource<SubCategoriesResponse>>
        get() = SubCategoryData





    suspend fun getMostScannedProducts(){


        MostScannedProductData.emit(Resource.Loading())

        try {

            val response = ProductApi.getMostScanned()

            if (response.body()!= null && response.body()!!.success){

                MostScannedProductData.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                MostScannedProductData.emit(Resource.Error(errorResponse.message))

            }

        }catch(e:Exception){

            MostScannedProductData.emit(Resource.Error("Couldn't load Products"))
//            Log.d("product repo", "getMostScannedProducts: $e ")


        }


    }





    suspend fun getCategories(){


        CategoriesData.emit(Resource.Loading())

        try {

            val response = ProductApi.getCategories()

            if(response.body() != null && response.body()!!.success){

                CategoriesData.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                CategoriesData.emit(Resource.Error(errorResponse.message))

            }

        }catch (e : Exception){

            CategoriesData.emit(Resource.Error("Couldn't Load Categories"))
//            Log.d("product repo ", "getCategories: $e ")

        }


    }




    suspend fun getProductByBarcode(barcode : String, Authorization: String){

        ProductbyBarcodeData.emit(Resource.Loading())

        try {

            val response = ProductApi.getProductByBarcode(barcode, Authorization)

//            Log.d("product repo", "barco ${response.body().toString()}")

            if(response.body()!= null && response.body()!!.success){

                ProductbyBarcodeData.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                   ProductbyBarcodeData.emit(Resource.Error(errorResponse.message))

//                Log.d("product repo", "barco ${errorResponse.message}")


            }

        }catch (e:Exception){

            ProductbyBarcodeData.emit(Resource.Error("Sorry! couldn't load this product"))
//            Log.d("product repo ", "getProductByBarcode: $e")


        }



    }






    suspend fun LikeUnlikeProduct(barcode : String, Authorization : String){

        LikeUnlikeProductData.emit(Resource.Loading())

        try{

            val response = ProductApi.likeUnlikeProduct(barcode,Authorization)

            if(response.body()!= null && response.body()!!.success){

                LikeUnlikeProductData.emit((Resource.Success(response.body()!!.data)))

            }else{


                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                LikeUnlikeProductData.emit(Resource.Error(errorResponse.message))

            }

        }catch (e : Exception){


            LikeUnlikeProductData.emit(Resource.Error("Couldn't Like Product"))

//            Log.d("Product repo ", "LikeUnlikeProduct: $e")

        }



    }









    suspend fun getAlternativeProducts(category : String,Authorization: String){

        AlternateProductData.emit(Resource.Loading())


        try{

//            Log.d("product repo", "getAlternativeProducts: Heya ")

            val response = ProductApi.getAlternateProducts(category,Authorization)

//            Log.d("product repo", "getAlternativeProducts: fof ${response.body().toString()}")

            if(response.body()!= null && response.body()!!.success){

                AlternateProductData.emit(Resource.Success(response.body()!!.data))

            }else{


                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                AlternateProductData.emit(Resource.Error(errorResponse.message))

            }


        }catch (e : Exception){

            AlternateProductData.emit(Resource.Error("Couldn't Load Products oops"))
//            Log.d("product repo", "getAlternativeProducts: $e")

        }



    }







    suspend fun getSearch(query : String,Authorization: String){

        SearchData.emit(Resource.Loading())

try {

    val response = ProductApi.search(query,Authorization)

    if(response.body()!= null && response.body()!!.success){

        SearchData.emit(Resource.Success(response.body()!!.data))

}else{

    val errorBody = response.errorBody()?.string()
    val errorResponse = errorBody.let {
        Gson().fromJson(it,ErrorResponse::class.java)
    }

        SearchData.emit(Resource.Error(errorResponse.message))

}

}catch (e : Exception){

//    Log.d("product repo", "getSearch: $e")

    SearchData.emit(Resource.Error("Couldn't Load Products oops"))

    }


    }









    suspend fun CheckBarcode(barcode: String){

        CheckBarcodeData.emit(Resource.Loading())

        try {
            val response = ProductApi.checkBarcode(barcode)


            if(response.body()!= null && response.body()!!.success){

                CheckBarcodeData.emit(Resource.Success(response.body()!!.data))


            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                CheckBarcodeData.emit(Resource.Error(errorResponse.message))

            }

        }catch (e : Exception){

            CheckBarcodeData.emit(Resource.Error("Couldn't Load Products oops"))
//            Log.d("product repo", "Checkbarcode: $e")

        }



    }






    suspend fun getLikedProducts(Authorization: String){

        getLikedProductData.emit(Resource.Loading())

        try {

            val response = ProductApi.getLikedProducts(Authorization)

            if(response.body()!= null && response.body()!!.success) {

                getLikedProductData.emit(Resource.Success(response.body()!!.data))

            }else{
                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                getLikedProductData.emit(Resource.Error(errorResponse.message))

            }

        }catch (e:Exception){

            getLikedProductData.emit(Resource.Error("Couldn't Load Products oops!"))

//            Log.d("product repo", "getLikedProducts: $e")

        }



    }








    suspend fun ConsumeProduct(barcode:String,size:Float,Token : String){

        ConsumeProduct.emit(Resource.Loading())

        try{

            val response = ProductApi.consumeProduct(Token,barcode, ConsumeProductSend(size))

            if(response.body()!!.success && response.body() != null ){

                ConsumeProduct.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                ConsumeProduct.emit(Resource.Error(errorResponse.message))

                delay(100)
                ConsumeProduct.emit(Resource.Nothing())

            }
        }catch(e : Exception){

            ConsumeProduct.emit(Resource.Error("Oops! Try Again"))

//            Log.d("product repo", "Consume Product: $e")
        }



    }




    suspend fun GetConsumedWeekData(week: String,token:String){

        ConsumedWeekData.emit(Resource.Loading())

        try{

            val response = ProductApi.getWeekData(token,week)

            if(response.body()!!.success && response.body() != null){

                ConsumedWeekData.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                ConsumedWeekData.emit(Resource.Error(errorResponse.message))

            }


        }catch (e:Exception){


            ConsumedWeekData.emit(Resource.Error("Oops! Try Again"))

//            Log.d("product repo", "Consume Week Data Product: $e")


        }



    }








    suspend fun GetConsumedDayData(token: String,day:String){

        ConsumedDayData.emit(Resource.Loading())

        try{

            val response = ProductApi.getDayData(token,day)

            if(response.body()!!.success && response.body() != null) {

                ConsumedDayData.emit(Resource.Success(response.body()!!.data))

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                ConsumedDayData.emit(Resource.Error(errorResponse.message))


            }

        }catch (e : Exception){

            ConsumedDayData.emit(Resource.Error("Oops! Try Again"))
//            Log.d("product repo", "Consume Product: $e")

        }


    }




    suspend fun DeleteConsumedProduct(token: String,barcode: String){

        DeleteConsumedProduct.emit(Resource.Loading())

        try {

            val response = ProductApi.DeleteConsumeProduct(token, barcode)

            if(response.body()!!.success && response.body() != null) {

                DeleteConsumedProduct.emit(Resource.Success(response.body()!!.data))

                delay(500)

                DeleteConsumedProduct.emit(Resource.Nothing())

            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                DeleteConsumedProduct.emit(Resource.Error(errorResponse.message))

            }
        }catch (e:Exception){

            DeleteConsumedProduct.emit(Resource.Error("Oops! Try Again"))
//            Log.d("product repo", "Delete Product: $e")

        }


    }




    suspend fun GetMonthData(token: String,month:String){


        ConsumedMonthData.emit(Resource.Loading())

        try {

            val response = ProductApi.getMonthData(token,month)

            if(response.body()!!.success && response.body() != null){

                ConsumedMonthData.emit(Resource.Success(response.body()!!.data))


            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                ConsumedMonthData.emit(Resource.Error(errorResponse.message))

            }

        }catch (e:Exception){

            ConsumedMonthData.emit(Resource.Error("Oops! Try Again"))
//            Log.d("product repo", "Month Data: $e")

        }


    }





    suspend fun GetSubCategory(category: String,){

        SubCategoryData.emit(Resource.Loading())

        try {

            val response = ProductApi.getSubCategories(category)

            if(response.body()!!.success && response.body() != null) {

                SubCategoryData.emit(Resource.Success(response.body()!!.data))
            }else{

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it,ErrorResponse::class.java)
                }

                SubCategoryData.emit(Resource.Error(errorResponse.message))

            }
        }catch (e:Exception){

            SubCategoryData.emit(Resource.Error("Oops! Try Again"))
//            Log.d("product repo", "sub categories: $e")

        }


    }



    suspend fun clearAlternativeProductList(){
        AlternateProductData.emit(Resource.Nothing())
    }










}