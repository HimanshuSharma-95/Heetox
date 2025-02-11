package com.heetox.app.Composables.ProductCompose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Product.AlternateResponseItem
import com.heetox.app.R
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen


@Composable
fun ProductCard(
    data: AlternateResponseItem, ProductVM:ProductsViewModel, UserData: State<LocalStoredData?>,
    context: Context,
    navController: NavController,
    isLiked: Boolean,
    onLikeChange: (Boolean) -> Unit,
    likeCount: Int,
    onLikeCountChange: (Int) -> Unit,
){


//ingredients
        val ing: List<String> by remember {

            mutableStateOf(if (data.ingredients.size > 2) data.ingredients.take(2) else data.ingredients.take(
                data.ingredients.size
            ))

        }





Row(
    modifier = Modifier
        .padding(horizontal = 10.dp, vertical = 5.dp)
        .width(450.dp)
        .height(170.dp)
        .shadow(3.dp, RoundedCornerShape(15.dp), clip = false, HeetoxDarkGray, HeetoxDarkGray)
        .clip(RoundedCornerShape(15.dp))
        .background(Color.White)
        .padding(10.dp)
        .clickable {

            navController.navigate("productdetails/${data.product_barcode}")

        }

){




    //image section
    Column(
        modifier = Modifier
            .weight(1f)

    ){

ProductCardImage(Image = data.product_front_image, name = data.product_name )

    }







    Spacer(modifier = Modifier
        .width(10.dp)
    )







    // Whole Detail Column
    Column(
        modifier = Modifier
            .weight(2.3f)
            .fillMaxHeight()
,

        verticalArrangement = Arrangement.Center
    ){





// rank and rating
      ProductsCardRankAndRaing(nutriscore = data.product_nutriscore, rank = data.rank )





     Spacer(modifier = Modifier
    .height(10.dp)
     )




        //ingredients
          ProductCardIngredeints(ingredients = ing)






            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )




        //price and like

        ProductCardPriceAndLike(
            price = data.price,
            isLiked = isLiked,
            UserData = UserData,
            context = context,
            onLikeChange = onLikeChange,
            likeCount = likeCount,
            onLikeCountChange = onLikeCountChange,
            ProductVM = ProductVM,
            barcodes = data.product_barcode
        )





    }

}




}










@Composable
fun ProductCardImage(Image : String, name : String){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        AsyncImage(
            model = Image,
            contentDescription = "Product Image",
            modifier = Modifier
                .size(100.dp)
        )

        Text(
            text = name.ifEmpty { "NA" },
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

    }


}









@Composable
fun ProductsCardRankAndRaing(nutriscore : String,rank : Int ){



    Row(

        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ){

        Text(text = "#${rank.toString().ifEmpty { "NA" }}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = HeetoxDarkGreen,
        )

        when(nutriscore){

            "A" -> AsyncImage(model = R.drawable.arating, contentDescription = "Rating Image", modifier = Modifier
                .width(130.dp)
                .padding(end = 20.dp) )
            "B" -> AsyncImage(model = R.drawable.brating, contentDescription = "Rating Image" , modifier = Modifier
                .width(130.dp)
                .padding(end = 20.dp))
            "C" -> AsyncImage(model = R.drawable.crating, contentDescription = "Rating Image",modifier = Modifier
                .width(130.dp)
                .padding(end = 20.dp))
            "D" -> AsyncImage(model = R.drawable.drating, contentDescription = "Rating Image",modifier = Modifier
                .width(130.dp)
                .padding(end = 20.dp))
            "E" -> AsyncImage(model = R.drawable.erating, contentDescription = "Rating Image",modifier = Modifier
                .width(130.dp)
                .padding(end = 20.dp))
            else -> Text(text = "Rating NA")
        }

    }

}




inline fun <T> List<T>.takeWhileIndexed(predicate: (index: Int, T) -> Boolean): List<T> {
    val list = mutableListOf<T>()
    for (i in indices) {
        if (!predicate(i, this[i])) break
        list.add(this[i])
    }
    return list
}





@Composable
fun ProductCardIngredeints(ingredients : List<String>){

    var showMore by remember { mutableStateOf(false) }
    var isTextOverflowing by remember { mutableStateOf(false) }

    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Column(
        modifier = Modifier
//            .heightIn(min = 60.dp) // Adjust height as needed
//            .padding(8.dp)
    ) {
        com.google.accompanist.flowlayout.FlowRow {
            Text(
                text = "Ingredients: ",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 18.sp // Adjust the line height to reduce spacing between lines
            )

            val text = ingredients.joinToString(", ")

            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = if (showMore) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp, // Adjust the line height to reduce spacing between lines
                onTextLayout = { layoutResult ->
                    textLayoutResult.value = layoutResult
                    isTextOverflowing = layoutResult.hasVisualOverflow
                },
                modifier = Modifier
                    .fillMaxWidth()
//                    .padding(end = 4.dp)
            )
        }

//        if (isTextOverflowing && !showMore) {
            Text(
                text = "Learn More",
                fontSize = 14.sp,
                color = HeetoxDarkGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
//                    .clickable {
//                        showMore = true
//                    }
//                    .padding(top = 8.dp)
            )
//        }
    }
}







@Composable
fun ProductCardPriceAndLike( price : Int,
                             isLiked: Boolean,UserData : State<LocalStoredData?>,
                             context : Context,
                             onLikeChange: (Boolean) -> Unit,likeCount: Int,
                             onLikeCountChange: (Int) -> Unit,
                             ProductVM: ProductsViewModel,
                             barcodes : String
                             ){

    Row(

        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ){

        Text(text = if(price != 0) "₹ ${price}" else "Price NA",
            fontSize = 18.sp,
            color = HeetoxDarkGray,
            fontWeight = FontWeight.Bold
        )




        Row(

            modifier = Modifier
                .padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically

        ){

//            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.filledlike) , contentDescription = "",
//
//                tint = if (isLiked) Color.Red else HeetoxLightGray,
//                modifier = Modifier
//                    .clickable(
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() }
//                    ) {
//
//                        if (UserData.value != null) {
//
//                            onLikeChange(!isLiked)
//
//                            UserData.value?.Token?.let {
//                                ProductVM.likeunlikeproduct(
//                                    barcodes,
//                                    it
//                                )
//                            }
//
//                            val newlikeCount = if (isLiked) {
//
//                                likeCount - 1
//
//                            } else {
//
//                                likeCount + 1
//
//                            }
//
//                            onLikeCountChange(newlikeCount)
//
//                        } else {
//
//                            Toast
//                                .makeText(context, "Login Required", Toast.LENGTH_SHORT)
//                                .show()
//
//
//                        }
//
//                    }
//                    .size(30.dp)
//
//            )

            Text(text = "Likes ",
                modifier = Modifier
                    .padding(horizontal = 0.dp),
                color = HeetoxDarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )

            Text(text = likeCount.toString().ifEmpty { " 0" },
                modifier = Modifier
                    .padding(horizontal = 3.dp),
                color = HeetoxDarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )



        }



    }


}