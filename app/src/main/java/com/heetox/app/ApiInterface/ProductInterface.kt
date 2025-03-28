package com.heetox.app.ApiInterface

import com.heetox.app.Model.ApiResponse
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query



interface ProductInterface{

@GET("product/most_scanned")
suspend fun getMostScanned() : Response<ApiResponse<List<MostScannedResponse>>>

@GET("product/categories")
suspend fun getCategories() : Response<ApiResponse<CategoriesResponse>>

@GET("product/showproduct/{barcode}")
suspend fun getProductByBarcode(@Path("barcode") barcode: String,@Header("Authorization") Authorization : String ) : Response<ApiResponse<ProductbyBarcodeResponse>>

@POST("product/like-product/{barcode}")
suspend fun likeUnlikeProduct(@Path("barcode") barcode: String,@Header("Authorization") Authorization : String ) : Response<ApiResponse<LikeUnlikeResponse>>

@GET("product/alternateproducts/{category}")
suspend fun getAlternateProducts(@Path("category") category : String, @Header("Authorization") Authorization : String ) : Response<ApiResponse<ArrayList<AlternateResponseItem>>>

@GET("product/searchproduct")
suspend fun search(@Query("search") searchname: String, @Header("Authorization") Authorization : String ) : Response<ApiResponse<ArrayList<AlternateResponseItem>>>

@GET("product/checkbarcode/{barcode}")
suspend fun checkBarcode(@Path("barcode") barcode: String) : Response<ApiResponse<CheckBarcodeResponse>>

@GET("product/liked_product")
suspend fun getLikedProducts(@Header("Authorization") Authorization : String ) : Response<ApiResponse<LikedProductRepsonse>>

@GET("product/subcategories/{categories}")
suspend fun getSubCategories(@Path("categories") categories : String) : Response<ApiResponse<SubCategoriesResponse>>




//consumption

@POST("product/ConsumeProduct/{barcode}")
suspend fun consumeProduct(@Header("Authorization") Authorization : String,@Path("barcode") barcode: String,@Body serving : ConsumeProductSend ) : Response<ApiResponse<ConsumeProductResponse>>

@GET("users/consumedproducts/{week}")
suspend fun getWeekData(@Header("Authorization") Authorization : String,@Path("week") week: String) : Response<ApiResponse<ConsumedWeekDataResponse>>

@GET("users/consumed_products_day/")
suspend fun getDayData(@Header("Authorization") Authorization : String,@Query("date") date: String) : Response<ApiResponse<ConsumedDayDataResponse>>

@GET("users/getMonthlyReport/{month}")
suspend fun getMonthData(@Header("Authorization") Authorization : String,@Path("month") month: String) : Response<ApiResponse<ConsumedMonthDataResponse>>

@POST("product/RemoveConsumeProduct/{id}")
suspend fun DeleteConsumeProduct(@Header("Authorization") Authorization : String,@Path("id") id: String) : Response<ApiResponse<ConsumeProductResponse>>


}