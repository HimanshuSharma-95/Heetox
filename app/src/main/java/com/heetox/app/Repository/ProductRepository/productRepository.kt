package com.heetox.app.Repository.ProductRepository

import com.heetox.app.ApiInterface.ProductInterface
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
                // Log.d("product repo", "barcode ${errorResponse.message}")

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

                return Resource.Error(extractErrorMessage(response))
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