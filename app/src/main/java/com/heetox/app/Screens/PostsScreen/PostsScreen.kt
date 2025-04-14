package com.heetox.app.Screens.PostsScreen


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Post.Post
import com.heetox.app.ViewModel.PostVM.PostsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxWhite
import kotlinx.coroutines.delay


@Composable
fun PostScreen(postVM: PostsViewModel,userData:LocalStoredData?) {

    val lazyPagingItems = postVM.currentResult?.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    val token = userData?.Token ?: ""

    val context = LocalContext.current


//    val allAndBook = listOf("All Posts","BookMarked")
//
//    var allAndBookSelectedIndex by rememberSaveable {
//
//        mutableIntStateOf(0)
//
//    }

    //save token in viewModel
    LaunchedEffect(Unit){

    }

    LaunchedEffect(lazyPagingItems?.itemCount) {
        listState.scrollToItem(postVM.scrollIndex, postVM.scrollOffset)
//        Log.d("==>", "Restored scroll position: Index=${PostVM.scrollIndex}, Offset=${PostVM.scrollOffset}")
    }


    LaunchedEffect(listState) {
        delay(200L)
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                if (index != 0 || offset != 0) {
                    postVM.saveScrollPosition(index, offset)
//                    Log.d("==>", "Saved scroll position: Index=$index, Offset=$offset")
                }
            }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)




    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        lazyPagingItems?.refresh()
        swipeRefreshState.isRefreshing = true

        postVM.saveScrollPosition(0,0)
    }) {



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(HeetoxWhite),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


//            item {
//
//                Spacer(modifier = Modifier.height(10.dp))
//
//                SegmentedControl(
//                    options = AllandBook,
//                    selectedIndex = AllandBookSelectedIndex,
//                    onOptionSelected = {
//                                       AllandBookSelectedIndex = it
//                    },
//                    bgcolor = Color.White ,
//                    selectedColor = HeetoxWhite,
//                    selectedTextcolour = HeetoxDarkGreen
//                )
//
//            }


            item{
                Spacer(modifier = Modifier.height(20.dp))
            }


            lazyPagingItems?.let { items ->
                items(items, key = { it._id }){ post ->
                    post?.let {



                        var postState = postVM.getPostState(post._id)
                        var isLiked by remember { mutableStateOf( if(token.isEmpty()) false else postState.isLiked ) }
                        var likeCount by remember { mutableIntStateOf(if(token.isEmpty()) postState.likeCount else postState.likeCount) }
                        var isBookmarked by remember { mutableStateOf(if(token.isEmpty()) false else postState.isBookmarked  ) }




                        PostItem(
                            post = it,
                            onLike = { newLikeState ->

                                     if(token.isEmpty()){

                                         Toast.makeText(context,"Login Required",Toast.LENGTH_SHORT).show()

                                     }else{
                                         likeCount = if (newLikeState) likeCount + 1 else likeCount - 1
                                         isLiked = newLikeState
                                         postVM.updatePostState(post._id, isLiked, isBookmarked, likeCount)
                                         postVM.likeDislikePost(token, post._id)
                                         postState = postVM.getPostState(it._id)
                                     }

                            },
                            onBookmark = {

                                if(token.isEmpty()){

                                    Toast.makeText(context,"Login Required",Toast.LENGTH_SHORT).show()

                                }else {
                                    isBookmarked = !isBookmarked
                                    postVM.updatePostState(
                                        post._id,
                                        isLiked,
                                        isBookmarked,
                                        likeCount
                                    )
                                    postVM.bookMarkPost(post._id,token)
                                    postState = postVM.getPostState(post._id)
                                }
                            },
                            isLiked = isLiked,
                            isBookmarked = isBookmarked,
                            likeCount = likeCount
                        )

                    }
                }
            }





            // Handle loading and error states
            lazyPagingItems?.let { items ->
                when {
                    // Loading more items (pagination loading)
                    items.loadState.append is LoadState.Loading -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = HeetoxDarkGreen
                                )
                            }
                        }
                    }

                    // Error when loading more items
                    items.loadState.append is LoadState.Error -> {
                        item {
                            Text(
                                text = "Failed to load more posts",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center,
                                color = HeetoxDarkGray
                            )
                        }
                        swipeRefreshState.isRefreshing = false
                    }

                    // Error during initial load
                    items.loadState.refresh is LoadState.Error -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Failed to load posts",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = HeetoxDarkGray
                                )
                            }
                        }
                        swipeRefreshState.isRefreshing = false
                    }



                    // Loading initial data (refresh loading)
                    items.loadState.refresh is LoadState.Loading -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    color = HeetoxDarkGreen
                                )
                            }
                        }
//                        swiperefreshstate.isRefreshing = true
                    }

                    // Initial load finished, and no errors
                    items.loadState.refresh is LoadState.NotLoading && items.itemCount > 0 -> {
                        swipeRefreshState.isRefreshing = false
                    }

                    // No items available
                    items.itemCount == 0 && items.loadState.refresh is LoadState.NotLoading -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No posts available",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        swipeRefreshState.isRefreshing = false
                    }
                }

            }
        }
    }
}












@Composable
fun PostItem(
    post: Post,
    onLike: (Boolean) -> Unit,
    onBookmark: (String) -> Unit,
    isLiked: Boolean,
    isBookmarked: Boolean,
    likeCount: Int
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 9.dp)
            .width(500.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 10.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(.5f)
                    .fillMaxHeight()
                    .background(Color.White),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "by ${post.author.ifEmpty { "Unknown" }}",
                    color = HeetoxDarkGray
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = post.created_At_date.ifEmpty { "" },
                    color = HeetoxDarkGray
                )
            }
        }

        AsyncImage(
            model = post.featured_image,
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop // Apply the fillCrop effect
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ){

            Text(
                text = post.title ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HeetoxDarkGray
            )

            Text(
                text = post.content ?: "",
                modifier = Modifier.padding(vertical = 10.dp),
                color = Color.Black
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    // Toggle the like state and update the count
                    onLike(!isLiked)

                }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else HeetoxDarkGray,
                        modifier = Modifier.size(27.dp)
                    )
                }

                Text(
                    text = likeCount.toString(),
                    color = HeetoxDarkGray,
                    fontSize = 16.sp
                )
            }

//            IconButton(onClick = { onBookmark(post._id) }) {
//                Icon(
//                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
//                    contentDescription = "Bookmark",
//                    tint = HeetoxDarkGray,
//                    modifier = Modifier.size(27.dp)
//                )
//            }
        }
    }
}
