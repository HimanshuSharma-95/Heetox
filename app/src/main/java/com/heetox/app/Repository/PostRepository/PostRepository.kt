package com.heetox.app.Repository.PostRepository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.heetox.app.ApiInterface.PostInterface
import com.heetox.app.Model.Post.Post
import com.heetox.app.Model.Post.bookmarkpostresponse
import com.heetox.app.Model.Post.postlikedislikeresponse
import com.heetox.app.Utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class PostRepository @Inject constructor(
    private val apiService: PostInterface
) {

    
    private val postlikedislikeresponse = MutableStateFlow<Resource<postlikedislikeresponse>>(Resource.Nothing())
    
    val accessPostLikeDislikeResponse : StateFlow<Resource<postlikedislikeresponse>>
        get() = postlikedislikeresponse

    private val boommarkresponse = MutableStateFlow<Resource<bookmarkpostresponse>>(Resource.Nothing())

    val accessbookmarkrepsonse : StateFlow<Resource<bookmarkpostresponse>>
        get() = boommarkresponse


    fun getPosts(authToken: String): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2,enablePlaceholders = false,initialLoadSize = 10),
            pagingSourceFactory = { PostsPagingSource(apiService, authToken) }
        ).flow
    }





    
    suspend fun postLikeDislike(postid: String,Authorization:String){
        
        postlikedislikeresponse.emit(Resource.Loading())

        try {
            val response = apiService.likeDislikePost(postid, Authorization)

            if (response.isSuccessful && response.body() != null) {
                postlikedislikeresponse.emit(Resource.Success(response.body()!!.data))
            } else {

                postlikedislikeresponse.emit(Resource.Error("Something went wrong"))

            }

        }catch (e:Exception){
            
            postlikedislikeresponse.emit(Resource.Error("Something went wrong"))
            Log.d("post repo", "postLikeDislike: $e")
            
        }
        
        
    }





    suspend fun bookmarkPost( PostId:String , Authorization:String ){


        boommarkresponse.emit(Resource.Loading())

        try {
            val response = apiService.bookmarkPost(PostId, Authorization)

            if (response.isSuccessful && response.body() != null) {
                boommarkresponse.emit(Resource.Success(response.body()!!.data))
            } else {

                boommarkresponse.emit(Resource.Error("Something went wrong"))

            }

        }catch (e:Exception){

            boommarkresponse.emit(Resource.Error("Something went wrong"))
            Log.d("post repo", "bookmark post : $e")

        }



    }
    
    
    



}




class PostsPagingSource(
    private val apiService: PostInterface,
    private val authToken: String
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {

            val page = params.key ?: 1
            val response = apiService.getAllPosts(page, params.loadSize, authToken)
            val prevkey = if (response.body()?.data?.hasPrevPage != true) null else page - 1
            val nextkey = if (response.body()?.data?.hasNextPage != true) null else page + 1



            if (response.isSuccessful) {
                val posts = response.body()?.data?.posts ?: emptyList()
                LoadResult.Page(
                    data = posts,
                    prevKey = prevkey,
                    nextKey = nextkey
                )
            } else {
                LoadResult.Error(Exception("Error fetching posts"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}
