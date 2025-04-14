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
import com.heetox.app.Utils.extractErrorMessage
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class productRepository @Inject constructor(private val productApi: ProductInterface) {

//
//    private var MostScannedProductData = MutableStateFlow<Resource<List<MostScannedResponse>>>(Resource.Nothing())
//
//    val accessMostScannedProductData : StateFlow<Resource<List<MostScannedResponse>>>
//        get() = MostScannedProductData

//
//    private  var CategoriesData = MutableStateFlow<Resource<CategoriesResponse>>(Resource.Nothing())
//
//    val accessCategoriesData : StateFlow<Resource<CategoriesResponse>>
//        get() = CategoriesData

//    private var ProductbyBarcodeData = MutableStateFlow<Resource<ProductbyBarcodeResponse>>(Resource.Nothing())
//
//    val accessProductByBarcodeData : StateFlow<Resource<ProductbyBarcodeResponse>>
//        get() = ProductbyBarcodeData


//    private  var LikeUnlikeProductData = MutableStateFlow<Resource<LikeUnlikeResponse>>(Resource.Nothing())
//
//    val accessLikedProductData : StateFlow<Resource<LikeUnlikeResponse>>
//        get() = LikeUnlikeProductData


//    private var AlternateProductData = MutableStateFlow<Resource<ArrayList<AlternateResponseItem>>>(Resource.Nothing())
//
//    val accessAlternateProduct : StateFlow<Resource<ArrayList<AlternateResponseItem>>>
//        get() = AlternateProductData


//    private var SearchData = MutableStateFlow<Resource<ArrayList<AlternateResponseItem>>>(Resource.Nothing())
//
//    val accessSearchData : StateFlow<Resource<ArrayList<AlternateResponseItem>>>
//        get() = SearchData


//    private var CheckBarcodeData = MutableStateFlow<Resource<CheckBarcodeResponse>>(Resource.Nothing())
//
//    val accessCheckBarcodeData: StateFlow<Resource<CheckBarcodeResponse>>
//        get() = CheckBarcodeData


//    private var getLikedProductData = MutableStateFlow<Resource<LikedProductRepsonse>>(Resource.Nothing())
//
//    val accessGetLikedProductData: StateFlow<Resource<LikedProductRepsonse>>
//        get() = getLikedProductData


//    private var SubCategoryData = MutableStateFlow<Resource<SubCategoriesResponse>>(Resource.Nothing())
//
//    val accessSubCategoryData: StateFlow<Resource<SubCategoriesResponse>>
//        get() = SubCategoryData



//    private var ConsumeProduct = MutableStateFlow<Resource<ConsumeProductResponse>>(Resource.Nothing())
//
//    val accessConsumeProductData: StateFlow<Resource<ConsumeProductResponse>>
//        get() = ConsumeProduct


//    private var ConsumedWeekData =
//        MutableStateFlow<Resource<ConsumedWeekDataResponse>>(Resource.Nothing())
//
//    val accessConsumedWeekData: StateFlow<Resource<ConsumedWeekDataResponse>>
//        get() = ConsumedWeekData


//    private var ConsumedDayData = MutableStateFlow<Resource<ConsumedDayDataResponse>>(Resource.Nothing())
//
//    val accessConsumedDayData: StateFlow<Resource<ConsumedDayDataResponse>>
//        get() = ConsumedDayData


//    private var DeleteConsumedProduct =
//        MutableStateFlow<Resource<ConsumeProductResponse>>(Resource.Nothing())

//    val accessDeleteConsumedProduct: StateFlow<Resource<ConsumeProductResponse>>
//        get() = DeleteConsumedProduct


//    private var ConsumedMonthData =
//        MutableStateFlow<Resource<ConsumedMonthDataResponse>>(Resource.Nothing())
//
//    val accessConsumedMonthData: StateFlow<Resource<ConsumedMonthDataResponse>>
//        get() = ConsumedMonthData
//




    suspend fun getMostScannedProducts(): Resource<List<MostScannedResponse>> {

        try {
            val response = productApi.getMostScanned()

            return if (response.body() != null && response.body()!!.success) {
                Resource.Success(response.body()!!.data)
            } else {
                Resource.Error(extractErrorMessage(response))
            }

        } catch (e: Exception) {
            return Resource.Error("Couldn't load Products")
//            Log.d("product repo", "getMostScannedProducts: $e ")
        }


    }


    suspend fun getCategories(): Resource<CategoriesResponse> {

        try {

            val response = productApi.getCategories()

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

            return Resource.Error("Couldn't Load Categories")
//            Log.d("product repo ", "getCategories: $e ")
        }

    }


    suspend fun getProductByBarcode(
        barcode: String,
        authorization: String
    ): Resource<ProductbyBarcodeResponse> {

        try {

            val response = productApi.getProductByBarcode(barcode, authorization)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

                //                Log.d("product repo", "barcode ${errorResponse.message}")

            }

        } catch (e: Exception) {

            return Resource.Error("Sorry! couldn't load this product")
//            Log.d("product repo ", "getProductByBarcode: $e")

        }


    }


    suspend fun likeUnlikeProduct(
        barcode: String,
        authorization: String
    ): Resource<LikeUnlikeResponse> {


        try {

            val response = productApi.likeUnlikeProduct(barcode, authorization)

            return if (response.body() != null && response.body()!!.success) {

                (Resource.Success(response.body()!!.data))

            } else {

                Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {


            return Resource.Error("Couldn't Like Product")

//            Log.d("Product repo ", "LikeUnlikeProduct: $e")

        }


    }


    suspend fun getAlternativeProducts(
        category: String,
        authorization: String
    ): Resource<ArrayList<AlternateResponseItem>> {

        try {


            val response = productApi.getAlternateProducts(category, authorization)

//            Log.d("product repo", "getAlternativeProducts: fof ${response.body().toString()}")

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }


        } catch (e: Exception) {

            return Resource.Error("Couldn't Load Products oops")
//            Log.d("product repo", "getAlternativeProducts: $e")

        }


    }


    suspend fun getSearch(
        query: String,
        authorization: String
    ): Resource<ArrayList<AlternateResponseItem>> {

        try {

            val response = productApi.search(query, authorization)

            return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

//    Log.d("product repo", "getSearch: $e")

            return Resource.Error("Couldn't Load Products oops")

        }


    }


    suspend fun checkBarcode(barcode: String):Resource<CheckBarcodeResponse>{


        try {
            val response = productApi.checkBarcode(barcode)


           return if (response.body() != null && response.body()!!.success) {

                Resource.Success(response.body()!!.data)

            } else {

                return Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

           return Resource.Error("Couldn't Load Products oops")
//            Log.d("product repo", "Checkbarcode: $e")

        }


    }


    suspend fun getLikedProducts(authorization: String):Resource<LikedProductRepsonse>{


        try {

            val response = productApi.getLikedProducts(authorization)

           return if (response.body() != null && response.body()!!.success) {

              Resource.Success(response.body()!!.data)

            } else {

                return Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

            return Resource.Error("Couldn't Load Products oops!")

//            Log.d("product repo", "getLikedProducts: $e")

        }


    }




    suspend fun getSubCategory(category: String):Resource<SubCategoriesResponse>{


        try {

            val response = productApi.getSubCategories(category)

           return if (response.body()!!.success && response.body() != null) {

                 Resource.Success(response.body()!!.data)
            } else {

                Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception) {

            return Resource.Error("Oops! Try Again")
//            Log.d("product repo", "sub categories: $e")
        }


    }



    suspend fun consumeProduct(barcode: String, size: Float, token: String):Resource<ConsumeProductResponse>{

        try {

            val response = productApi.consumeProduct(token, barcode, ConsumeProductSend(size))

            return if (response.body()!!.success && response.body() != null) {

                Resource.Success(response.body()!!.data)

            } else{

                Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception){

            return Resource.Error("Oops! Try Again")
//            Log.d("product repo", "Consume Product: $e")
        }

    }


    suspend fun getConsumedWeekData(week: String, token: String):Resource<ConsumedWeekDataResponse>{


        try {

            val response = productApi.getWeekData(token, week)

            return if (response.body()!!.success && response.body() != null) {

                 Resource.Success(response.body()!!.data)

            } else {

                val errorBody = response.errorBody()?.string()
                val errorResponse = errorBody.let {
                    Gson().fromJson(it, ErrorResponse::class.java)
                }

                return Resource.Error(errorResponse.message)

            }

        } catch (e: Exception) {


            return Resource.Error("Oops! Try Again")

//            Log.d("product repo", "Consume Week Data Product: $e")


        }


    }



    suspend fun getConsumedDayData(token: String, day: String):Resource<ConsumedDayDataResponse>{


        try {

            val response = productApi.getDayData(token, day)

            return if (response.body()!!.success && response.body() != null) {

                Resource.Success(response.body()!!.data)

            } else {

                Resource.Error(extractErrorMessage(response))

            }

        }catch (e: Exception) {

           return Resource.Error("Oops! Try Again")
//            Log.d("product repo", "Consume Product: $e")

        }


    }


    suspend fun deleteConsumedProduct(token: String, barcode: String):Resource<ConsumeProductResponse>{

        try {

            val response = productApi.DeleteConsumeProduct(token, barcode)

            return if (response.body()!!.success && response.body() != null) {

                Resource.Success(response.body()!!.data)


            } else {

                Resource.Error(extractErrorMessage(response))

            }
        } catch (e: Exception) {

            return Resource.Error("Oops! Try Again")
//            Log.d("product repo", "Delete Product: $e")

        }


    }


    suspend fun getConsumeMonthData(token: String, month: String):Resource<ConsumedMonthDataResponse>{


        try {

            val response = productApi.getMonthData(token, month)

            return if (response.body()!!.success && response.body() != null) {

                Resource.Success(response.body()!!.data)


            } else {

                return Resource.Error(extractErrorMessage(response))

            }

        } catch (e: Exception) {

            return Resource.Error("Oops! Try Again")
//            Log.d("product repo", "Month Data: $e")
        }


    }



}