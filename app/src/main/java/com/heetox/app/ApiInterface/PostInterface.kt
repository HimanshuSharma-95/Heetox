package com.heetox.app.ApiInterface

import com.heetox.app.Model.ApiResponse
import com.heetox.app.Model.Post.allpostresponse
import com.heetox.app.Model.Post.bookmarkpostresponse
import com.heetox.app.Model.Post.postlikedislikeresponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostInterface {


    @GET("post/all_posts")
    suspend fun getAllPosts(@Query("page") page : Int,
                         @Query("limit") limit : Int = 10,
                            @Header("Authorization") Authorization : String
                            ): Response<ApiResponse<allpostresponse>>



    @POST("post/likeDislikePost/{postid}")
    suspend fun likeDislikePost(
      @Path("postid") postid : String,
      @Header("Authorization") Authorization: String
      ) : Response<ApiResponse<postlikedislikeresponse>>



    @POST("post/bookmarkPost/{postid}")
    suspend fun bookmarkPost(
        @Path("postid") postid : String,
        @Header("Authorization") Authorization: String
    ) : Response<ApiResponse<bookmarkpostresponse>>


}