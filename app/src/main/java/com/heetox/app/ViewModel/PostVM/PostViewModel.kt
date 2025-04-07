package com.heetox.app.ViewModel.PostVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.heetox.app.Model.Post.Post
import com.heetox.app.Repository.AuthenticationRepository.AuthenticationRepository
import com.heetox.app.Repository.PostRepository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    authRepository: AuthenticationRepository
) : ViewModel() {


    private val tokenFlow = authRepository.Localdata.map { it?.Token ?: "" }
    private var currentToken: String = ""


    init {
        // Observe token changes
        //if same token keeps the
        viewModelScope.launch {
            tokenFlow.collect { newToken ->
                if (currentToken != newToken) {
                    currentToken = newToken
                    getNewPostToken(newToken)
                }else{
                    getPosts(currentToken)
                }
            }
        }
    }


        var currentResult: Flow<PagingData<Post>>? = null

        var scrollIndex = 0
        var scrollOffset = 0

        // State management for each post
        private val _postState = mutableMapOf<String, PostState>()
        val postState: Map<String, PostState> = _postState


    fun getPosts(authToken: String): Flow<PagingData<Post>> {


        // If we have a cached result, return it directly
        val lastResult = currentResult
        if (lastResult != null) {

            return lastResult.map { pagingData ->
                pagingData.map { post ->
                    _postState[post._id] = PostState(
                        isLiked = post.isliked,
                        isBookmarked = post.isbookmarked,
                        likeCount = post.likesCount
                    )
                    post
                }
            }.also { currentResult = it }
            // Return cached result if token is available and state has already been populated
        }


        // Fetch new data and populate PostState with data from the API
       return getNewPostToken(authToken)

    }




 fun getNewPostToken(authToken : String): Flow<PagingData<Post>> {

        // Fetch new data and populate PostState with data from the API
        val newResult: Flow<PagingData<Post>> = postRepository.getPosts(authToken)
            .map { pagingData ->
                pagingData.map { post ->
                    // Populate PostState with initial data from the API
                    _postState[post._id] = PostState(
                        isLiked = post.isliked,
                        isBookmarked = post.isbookmarked,
                        likeCount = post.likesCount
                    )
                    post
                }
            }
            .cachedIn(viewModelScope)


     saveScrollPosition(0,0)
        currentResult = newResult
        return newResult


    }






    fun saveScrollPosition(index: Int, offset: Int) {
        scrollIndex = index
        scrollOffset = offset

    }

    fun updatePostState(postId: String, isLiked: Boolean, isBookmarked: Boolean, likeCount: Int) {
        _postState[postId] = PostState(isLiked, isBookmarked, likeCount)
    }

    fun getPostState(postId: String): PostState {
        return _postState[postId] ?: PostState()
    }


    fun likedislikepost(authToken: String, postId: String){

       viewModelScope.launch(Dispatchers.IO){
           postRepository.postLikeDislike(postId,authToken)
       }

    }


    fun bookmarkpost(postid:String,Authorization : String){

        viewModelScope.launch(Dispatchers.IO){
            postRepository.bookmarkPost(postid,Authorization)
        }

    }



}

data class PostState(
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val likeCount: Int = 0
)
