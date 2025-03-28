package com.heetox.app.Composables.ProductCompose

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Product.Ingredient
import com.heetox.app.Model.Product.NutritionalValue
import com.heetox.app.Model.Product.ProductbyBarcodeResponse
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite
import java.text.DecimalFormat


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductByBarcodeDetails(Details : ProductbyBarcodeResponse , AuthVM : AuthenticationViewModel , ProductVM : ProductsViewModel,navController: NavHostController){


    val scrollState = rememberScrollState()
    val UserData = AuthVM.Localdata.collectAsState()
    val context = LocalContext.current



    var imageList by rememberSaveable {

        mutableStateOf(listOf<String>())

    }


    val pagerState = rememberPagerState(0){
        imageList.size
    }

    var imageIndex by rememberSaveable {
        mutableStateOf(0)
    }

    var hashtag by rememberSaveable {
        mutableStateOf("#${Details.product_data.rank}")
    }

    var category by rememberSaveable {
        mutableStateOf(Details.product_data.product_sub_category)
    }

    var isLiked by rememberSaveable {

        mutableStateOf(Details.product_data.isliked)

    }

    var likeCount by rememberSaveable {

        mutableStateOf(Details.product_data.likesCount)

    }

    var RatingImageId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    if(Details.product_data.ratings.product_nutriscore == "A"){
        RatingImageId = R.drawable.arating
    }else if(Details.product_data.ratings.product_nutriscore == "B"){
        RatingImageId = R.drawable.brating
    }else if(Details.product_data.ratings.product_nutriscore == "C"){
        RatingImageId = R.drawable.crating
    }else if(Details.product_data.ratings.product_nutriscore == "D"){
        RatingImageId = R.drawable.drating
    }else if(Details.product_data.ratings.product_nutriscore == "E"){
        RatingImageId = R.drawable.erating
    }





    //for button bar slider
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val options = listOf("Nutritional value","Ingredients")



//    for nutrition list
    val nutritionList = remember {

        mutableStateOf(Details.product_data.nutritional_value)

    }



    val ingredientList by remember {
        mutableStateOf(listOf(Details.product_data.ingredients))
    }



    var addingToConsumption by rememberSaveable {
        mutableStateOf(false)
    }









//image slider
Box(
){

     if(addingToConsumption){


         Column(
             Modifier
                 .fillMaxSize()
                 .zIndex(1F)
                 .background(Color(0xB24D4D4D))
                 .verticalScroll(rememberScrollState()),
             verticalArrangement = Arrangement.Center,


         ){

             Column(
                 modifier = Modifier
                     .padding(horizontal = 10.dp)
                     .clip(RoundedCornerShape(50.dp))
                     .background(Color.White)
                     .clickable {
                         addingToConsumption = !addingToConsumption
                     }
                     .padding(10.dp),
                 horizontalAlignment = Alignment.End,
                 verticalArrangement = Arrangement.Center

             ){


                 Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Add button",
                     tint = HeetoxDarkGreen
                 )
             }

             ConsumeProductCard(Details,ProductVM,UserData.value?.Token,navController)
         }
     }



     Column(
         modifier = androidx.compose.ui.Modifier
             .fillMaxSize()
             .verticalScroll(scrollState)
             .background(HeetoxWhite)
         ,
         horizontalAlignment = Alignment.CenterHorizontally
     ) {


         //image
         Box(

             modifier = Modifier
                 .size(250.dp)


         ) {
             HorizontalPager(
                 beyondBoundsPageCount = imageList.size,
                 state = pagerState,
                 key = { it },
             ) { index ->

                 AsyncImage(
                     model = imageList[index], contentDescription = "Product Image",
                     modifier = androidx.compose.ui.Modifier
                         .size(250.dp)
                 )

             }
         }

         Row(
             modifier = androidx.compose.ui.Modifier
//                .offset(y = (-15).dp)
         ) {
             imageList.forEachIndexed { index, _ ->
                 Box(
                     modifier = androidx.compose.ui.Modifier
                         .padding(horizontal = 3.dp)
                         .width(40.dp)
                         .height(7.dp)
                         .clip(RoundedCornerShape(20.dp))
                         .background(if (index == imageIndex) HeetoxDarkGray else HeetoxLightGray)
                         .clickable {
                             imageIndex = index
                         }
                 )
             }

         }







        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            //hashtag
            if(category.isNotEmpty() || hashtag.isNotEmpty()){

                Box(
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 15.dp)
                ){
                    Row(

                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(HeetoxLightGray)
                            .padding(10.dp)

                        ,
                        horizontalArrangement = Arrangement.Center

                    ){

                        Text(
                            text = if (hashtag.isNotEmpty()) "${hashtag} in the " else "",
                            fontWeight = FontWeight.Bold,
                            color = HeetoxDarkGreen,
                            fontSize = 14.sp,
                        )

                        Text(text = "Category ( ${if(category.isNotEmpty()) category else "not available" } )",

                            fontWeight = FontWeight.Bold,
                            color = HeetoxDarkGray,
                            fontSize = 14.sp,

                            )

                    }
                }

            }

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(HeetoxDarkGreen)
                    .clickable {
                        if(UserData.value?.Token?.isEmpty() == true || UserData.value?.Token == null ){
                            Toast.makeText(context,"Login Required",Toast.LENGTH_SHORT).show()
                        }else{
                            addingToConsumption = true
                        }
                    }
                    .padding(5.dp)

            ){


                Icon(imageVector = Icons.Default.Add, contentDescription = "Add button",
                    tint = Color.White
                    )
            }

        }






         //details

         Column(

             modifier = Modifier
                 .fillMaxSize()
                 .shadow(
                     20.dp,
                     RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                     ambientColor = HeetoxDarkGreen,
                     spotColor = HeetoxDarkGreen
                 )
                 .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                 .background(Color.White)
                 .padding(bottom = 30.dp)
             ,
             horizontalAlignment = Alignment.CenterHorizontally

         ){





             //name and like and mrp
             Column(
                 modifier = androidx.compose.ui.Modifier
                     .width(510.dp)
                     .padding(horizontal = 30.dp, vertical = 10.dp),

                 ){


                 //name and like
                 Row(
                     verticalAlignment = Alignment.Top,
                     modifier = Modifier


                 ){


                     //name
                     Text(text = Details.product_data.product_name.ifEmpty { "not available" }  ,

                         fontWeight = FontWeight.Bold,
                         fontSize = 18.sp,
                         color = HeetoxDarkGray
                         ,
                         modifier = Modifier
                             .padding(0.dp, 0.dp, 10.dp, 10.dp)
                             .weight(.1f)
                     )


                     //like
                     Row(
                         modifier = androidx.compose.ui.Modifier
//                        .fillMaxWidth()
                         ,
                         horizontalArrangement = Arrangement.End ,
                         verticalAlignment = Alignment.CenterVertically
                     ){

                         Icon(imageVector = ImageVector.vectorResource(id = R.drawable.filledlike) , contentDescription = "",

                             tint = if (isLiked) Color.Red else HeetoxLightGray,
                             modifier = androidx.compose.ui.Modifier
                                 .clickable(
                                     indication = null,
                                     interactionSource = remember { MutableInteractionSource() }
                                 ) {

                                     if (UserData.value != null) {
                                         isLiked = !isLiked

                                         UserData.value?.Token?.let {
                                             ProductVM.likeunlikeproduct(
                                                 Details.product_data.product_barcode,
                                                 it
                                             )
                                         }

                                         likeCount = if (isLiked) {

                                             likeCount + 1

                                         } else {

                                             likeCount - 1

                                         }
                                     } else {

                                         Toast
                                             .makeText(
                                                 context,
                                                 "Login Required",
                                                 Toast.LENGTH_SHORT
                                             )
                                             .show()


                                     }

                                 }
                                 .size(30.dp)

                         )

                         Text(text = likeCount.toString().ifEmpty { " 0" },
                             modifier = androidx.compose.ui.Modifier
                                 .padding(horizontal = 3.dp),
                             color = HeetoxDarkGray,
                             fontWeight = FontWeight.Bold,
                             fontSize = 16.sp
                         )
                     }

                 }



                 Text(text = "MRP ₹${if(Details.product_data.price.toString().isNotEmpty()) Details.product_data.price.toString() else "not available"  }",
                     fontSize = 14.sp,
                     color = HeetoxDarkGray,
                     modifier = androidx.compose.ui.Modifier
                         .padding(top = 5.dp)
                 )



             }




             Box(modifier = androidx.compose.ui.Modifier
                 .fillMaxWidth(.9f)
                 .padding(vertical = 5.dp)
                 .height(1.dp)
                 .background(HeetoxLightGray)
             )









//nutritional score and button

             Row(
                 modifier = androidx.compose.ui.Modifier
                     .fillMaxWidth(1f)
                 ,
                 verticalAlignment = Alignment.Bottom,
                 horizontalArrangement = Arrangement.SpaceEvenly
             ){

                 Column {

                     Text(text = "Nutritional Score",
                         fontWeight = FontWeight.Bold,
                         fontSize = 15.sp,
                         color = HeetoxDarkGray,
                         modifier = androidx.compose.ui.Modifier
                             .padding(vertical = 10.dp, horizontal = 10.dp)
                     )


                     if(RatingImageId != null){
                         Image(painter = painterResource(id = RatingImageId!!), contentDescription = "Rating",
                             modifier = androidx.compose.ui.Modifier
                                 .width(150.dp)
                         )
                     }else{
                         Text(text = "Rating Not Available",
                             fontWeight = FontWeight.Bold,
                             fontSize = 15.sp,
                             color = HeetoxDarkGray,
                             modifier = androidx.compose.ui.Modifier
                                 .padding(vertical = 15.dp, horizontal = 10.dp)
                         )
                     }
                 }


                 Button(onClick = {

                     val maincategory = Details.product_data.product_category.replace(" ","_")
                     val subcategory = Details.product_data.product_sub_category.replace(" ","_")

                     navController.navigate("betteralternative/${maincategory}/${subcategory}")

                 },
                     colors = ButtonDefaults.buttonColors(
                         contentColor = Color.White,
                         containerColor = HeetoxDarkGreen
                     ),
                     shape = RoundedCornerShape(5.dp),
                     contentPadding = PaddingValues(15.dp,2.dp),
                     modifier = Modifier
                         .padding(bottom = 3.dp)
                         .height(33.dp)

                 ) {

                     Text(text = "Better Alternatives",
                         fontSize = 12.sp
                     )
                     Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Arrow")

                 }



             }






             //Nutritional values and ingredients

             Spacer(modifier = androidx.compose.ui.Modifier
                 .height(30.dp))

             SegmentedControl(
                 options = options,
                 selectedIndex = selectedIndex,
                 onOptionSelected = { selectedIndex = it },
                 width = 357,
                 bgcolor = HeetoxWhite,
                 selectedColor = Color.White,
                 selectedTextcolour = HeetoxDarkGreen
             )


             when (selectedIndex) {
                 0 -> Nutrition(nutritionList)
                 1 -> Ingredients(ingredientList)
             }



//            UserData.value?.Token?.let { AlternativeList(ProductVM = ProductVM, token = it, UserData = UserData , navController = navController, barcode = Details.product_data.product_barcode) }



         }







     }



 }











    //image adding
    LaunchedEffect(key1 = Details) {

        imageList = emptyList()

        if(Details.product_data.product_front_image.isNotEmpty()){
            imageList = imageList + Details.product_data.product_front_image
        }
        if(Details.product_data.product_back_image.isNotEmpty()){
            imageList = imageList + Details.product_data.product_back_image
        }
    }

    //changing page by index
    LaunchedEffect(imageIndex) {
        pagerState.animateScrollToPage(imageIndex)
    }

    //updating icon on page change
    LaunchedEffect(pagerState.currentPage) {
        imageIndex = pagerState.currentPage
    }




}

















@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    width : Int = 357 ,
    bgcolor : Color,
    selectedColor : Color,
    selectedTextcolour : Color
) {
    val selectedOptionColor = selectedTextcolour
    val unselectedOptionColor = HeetoxDarkGray
    val backgroundColor = bgcolor
    val selectedBackgroundColor = selectedColor

    // Animate the selection movement
    val transition = updateTransition(targetState = selectedIndex, label = "SegmentedControl")
    val indicatorOffset by transition.animateDp(label = "IndicatorOffset") { state ->
        val segmentWidth = width.dp / options.size
        segmentWidth * state
    }

    Box(
        modifier = Modifier
            .width(394.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(backgroundColor, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp)) // Ensure contents are clipped to the shape
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(backgroundColor, shape = RoundedCornerShape(20.dp))
        ) {
            // Moving indicator
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(350.dp / options.size)
                    .fillMaxHeight()
                    .background(selectedBackgroundColor, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp)) // Ensure the indicator stays within bounds
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) selectedOptionColor else unselectedOptionColor,
                    label = ""
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            onOptionSelected(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = textColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

















// nutritional values
@Composable
fun Nutrition(data: MutableState<NutritionalValue>) {


    Row(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = R.drawable.nutrition),
            contentDescription = "Nutrition Image",
            modifier = Modifier
                .size(50.dp)

        )


        Text(
            text = "Per 100 ml/g",
            fontSize = 15.sp,
            color = HeetoxDarkGray,
            modifier = Modifier
                .padding(horizontal = 10.dp)
        )
    }

    
    
    
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Keyhighlights(data = data)
        
    }







    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 0.dp)
            .width(400.dp)

    ){



        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){


            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "Circle",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(15.dp),
                tint = Color(0xFFFFE500)

            )

            Text(text = "Moderate",
                color = HeetoxDarkGray,
                fontSize = 15.sp
                )

            Spacer(modifier = Modifier.width(5.dp))


            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "Circle",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(15.dp),
                tint = Color(0xA3FF0000)

            )

            Text(text = "High",
                color = HeetoxDarkGray,
                fontSize = 15.sp
            )


        }


           //energy
           if (data.value.energy > 0) {

               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(10.dp))
                       .background(Color(0x1F23A247))
                       .padding(10.dp)


                   ,
                   verticalAlignment = Alignment.CenterVertically
               ){



                  Box(
                      modifier = Modifier
                          .padding(end = 10.dp)
                  ){
                      IconWithCircularProgress(
                          icon = painterResource(id = R.drawable.energy), // Replace with your desired icon
                          progress = 100f,                // Example progress value (75 out of 100)
                          borderColor = Color(0xFFFFD500),
                          borderWidth = 4.dp,
                          iconSize = 20.dp,
                          backgroundColor = Color.White
                      )
                  }

                   Column(
                       modifier = Modifier
                           .fillMaxWidth()
//                           .shadow(3.dp, RoundedCornerShape(10.dp))
                           .clip(RoundedCornerShape(10.dp))
                           .background(Color.White)
                           .padding(10.dp)


                   ) {

                       Text(
                           text = "Energy",
                           fontWeight = FontWeight.Bold,
                           color = Color.Black,
                           fontSize = 15.sp
                       )


                       Spacer(modifier = Modifier.height(5.dp))

                       val decimalFormat = DecimalFormat("#.##")

                       Row(
                           modifier = Modifier
                               .padding(start = 20.dp)
                       ) {
                           Text(
                               text = "${data.value.energy} ",
                               fontSize = 15.sp
                           )

                           Text(
                               text = "(${decimalFormat.format(data.value.energy * 0.239)} kcal)",
                               fontWeight = FontWeight.Bold,
                               color = Color.Black,
                               fontSize = 15.sp
                           )
                       }
                   }
               }

           }





        Spacer(modifier = Modifier.height(5.dp))





        //protein
        if(data.value.protein > 0 ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x1F23A247))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                ){
                    IconWithCircularProgress(
                        icon = painterResource(id = R.drawable.protein), // Replace with your desired icon
                        progress = data.value.protein,                // Example progress value (75 out of 100)
                        borderColor = Color(0xFF4CAF50),
                        borderWidth = 4.dp,
                        iconSize = 20.dp,
                        backgroundColor = Color.White
                    )
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(10.dp)

                ){

                    Text(
                        text = "Protein",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${data.value.protein} ",
                        fontSize = 15.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )
                }


            }

        }





        Spacer(modifier = Modifier.height(5.dp))










        //carbs
    if(data.value.total_carbohydrates > 0) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x1F23A247))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Box(
                modifier = Modifier
                    .padding(end = 10.dp)
            ) {
                IconWithCircularProgress(
                    icon = painterResource(id = R.drawable.carbs), // Replace with your desired icon
                    progress = data.value.total_carbohydrates,                // Example progress value (75 out of 100)
                    borderColor = Color(0xFFFF9800),
                    borderWidth = 4.dp,
                    iconSize = 20.dp,
                    backgroundColor = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(10.dp)
            ) {

                Text(
                    text = "Total Carbohydrates : ${data.value.total_carbohydrates}",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (data.value.carbohydrates.dietry_fibre > 0) {
                    Text(
                        text = "Dietary Fiber : ${data.value.carbohydrates.dietry_fibre} ",
                        fontSize = 15.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }



                if (data.value.carbohydrates.total_sugar > 0) {


                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if(data.value.carbohydrates.total_sugar >= 22.5){

                            Icon(
                                imageVector = Icons.Default.Circle,
                                contentDescription = "Circle",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(15.dp),
                                tint = Color(0xA3FF0000)

                            )





                        }else if(data.value.carbohydrates.total_sugar > 5 && data.value.carbohydrates.total_sugar < 22.5){

                            Icon(
                                imageVector = Icons.Default.Circle,
                                contentDescription = "Circle",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(15.dp),
                                tint = Color(0xFFFFE500)

                            )


                            
                        }else{
                            Spacer(modifier = Modifier.width(20.dp))
                        }

                        Text(
                            text = "Total Sugar : ${data.value.carbohydrates.total_sugar} ",
                            fontSize = 15.sp,
                            color = Color.Black,
                        )

                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }

                if (data.value.carbohydrates.added_sugar > 0) {

                        Text(
                            text = "Added Sugar : ${data.value.carbohydrates.added_sugar} ",
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .padding(start = 20.dp)
                        )
                }


            }
        }
    }






        Spacer(modifier = Modifier.height(5.dp))






        //Fats

       if(data.value.total_fats > 0) {


           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .clip(RoundedCornerShape(10.dp))
                   .background(Color(0x1F23A247))
                   .padding(10.dp),
               verticalAlignment = Alignment.CenterVertically
           ) {


               Box(
                   modifier = Modifier
                       .padding(end = 10.dp)
               ) {
                   IconWithCircularProgress(
                       icon = painterResource(id = R.drawable.fat), // Replace with your desired icon
                       progress = data.value.total_fats,                // Example progress value (75 out of 100)
                       borderColor = Color.Red,
                       borderWidth = 4.dp,
                       iconSize = 20.dp,
                       backgroundColor = Color.White
                   )
               }

               Column(
                   modifier = Modifier
                       .fillMaxWidth()
                       .clip(RoundedCornerShape(10.dp))
                       .background(Color.White)
                       .padding(10.dp)
               ) {

                   Row(
                       verticalAlignment = Alignment.CenterVertically
                   ) {

                       if(data.value.total_fats >= 17.5){
                           Icon(imageVector = Icons.Default.Circle,
                               contentDescription = "Circle",
                               modifier = Modifier
                                   .padding(end = 5.dp)
                                   .size(15.dp),
                               tint = Color(0xA3FF0000)
                           )



                       }else if(data.value.total_fats < 17.5 && data.value.total_fats > 3){
                           Icon(imageVector = Icons.Default.Circle,
                               contentDescription = "Circle",
                               modifier = Modifier
                                   .padding(end = 5.dp)
                                   .size(15.dp),
                               tint = Color(0xFFFFE500)
                           )



                       }

                       Text(
                           text = "Total Fats : ${data.value.total_fats}",
                           fontWeight = FontWeight.Bold,
                           color = Color.Black,
                           fontSize = 15.sp
                       )

                   }

                   Spacer(modifier = Modifier.height(5.dp))



                   if (data.value.fats.saturates_fats > 0) {

                     Row(
                        verticalAlignment = Alignment.CenterVertically
                     ){

                        if(data.value.fats.saturates_fats > 5){
                            Icon(imageVector = Icons.Default.Circle,
                                contentDescription = "Circle",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(15.dp),
                                tint = Color(0xA3FF0000)
                            )


                        }else if(data.value.fats.saturates_fats > 1.5 && data.value.fats.saturates_fats <= 5){
                            Icon(imageVector = Icons.Default.Circle,
                                contentDescription = "Circle",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(15.dp),
                                tint = Color(0xFFFFE500)
                            )


                        }else{
                            Spacer(modifier = Modifier.width(20.dp))
                        }



                         Text(
                             text = "Saturated Fats : ${data.value.fats.saturates_fats}",
                             fontSize = 15.sp,
                             color = Color.Black
                         )
                     }

                   }



                   if (data.value.fats.trans_fats > 0) {
                       Spacer(modifier = Modifier.height(5.dp))
                       Row(
                           verticalAlignment = Alignment.CenterVertically
                       ) {

                           Icon(
                               imageVector = Icons.Default.Circle,
                               contentDescription = "Circle",
                               modifier = Modifier
                                   .padding(end = 5.dp)
                                   .size(15.dp),
                               tint = Color(0xA3FF0000)

                           )


                           Text(
                               text = "Trans Fat : ${data.value.fats.trans_fats} ",
                               fontSize = 15.sp,
                               color = Color.Black
                           )
                       }
                   }



                   if (data.value.fats.unsaturated_fats > 0) {

                       Spacer(modifier = Modifier.height(5.dp))

                       Text(
                           text = "Unsaturated Fat : ${data.value.fats.unsaturated_fats} ",
                           fontSize = 15.sp,
                           color = Color.Black,
                           modifier = Modifier
                               .padding(start = 20.dp)
                       )
                   }


               }
           }

       }






        Spacer(modifier = Modifier.height(5.dp))








        //sodium
        if(data.value.sodium > 0) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x1F23A247))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                ) {
                    IconWithCircularProgress(
                        icon = painterResource(id = R.drawable.sodium), // Replace with your desired icon
                        progress = data.value.sodium/1000,                // Example progress value (75 out of 100)
                        borderColor = Color(0xFF009688),
                        borderWidth = 4.dp,
                        iconSize = 20.dp,
                        backgroundColor = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(10.dp)

                ) {


                    Text(
                        text = "Sodium",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(5.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if(data.value.sodium >= 1500) {

                            Icon(
                                imageVector = Icons.Default.Circle,
                                contentDescription = "Circle",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(15.dp),
                                tint = Color(0xA3FF0000)

                            )



                        }else if(data.value.sodium > 300 && data.value.sodium < 1500){

                            Icon(
                                imageVector = Icons.Default.Circle,
                                contentDescription = "Circle",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(15.dp),
                                tint = Color(0xFFFFE500)

                            )



                        }else{
                            Spacer(modifier = Modifier.width(20.dp))
                        }

                        Text(
                            text = "${data.value.sodium} mg",
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }

                }


            }
        }









        Spacer(modifier = Modifier.height(5.dp))





        //Cholesterol
        if(data.value.cholestrol > 0) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x1F23A247))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                ) {
                    IconWithCircularProgress(
                        icon = painterResource(id = R.drawable.colesterol), // Replace with your desired icon
                        progress = data.value.cholestrol,                // Example progress value (75 out of 100)
                        borderColor = Color(0xFFE91E63),
                        borderWidth = 4.dp,
                        iconSize = 20.dp,
                        backgroundColor = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(10.dp)

                ) {

                    Text(
                        text = "Cholesterol",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(5.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = "Circle",
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(15.dp),
                            tint = Color(0xA3FF0000)

                        )


                        Text(
                            text = "${data.value.cholestrol} ",
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }

                }


            }
        }


        //micronutrients

        val mc by remember {
            mutableStateOf(data.value.micro_nutrients)
        }


        with(mc) {
            if (listOf(
                    vitamin_A,
                    vitamin_B,
                    vitamin_C,
                    vitamin_D,
                    vitamin_E,
                    calcium,
                    potassium,
                    iron,
                    zinc,
                    phosphorous,
                    magnessium
                ).any { it > 0 }
            ) {


                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x1F23A247))
                        .padding(10.dp)
                ) {

                    Text(text = "Micro Nutrients",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp)

                    Spacer(modifier = Modifier.height(10.dp))


                    if (vitamin_A > 0) {

                     Row(
                         modifier = Modifier
                             .fillMaxWidth()
                             .clip(RoundedCornerShape(10.dp))
                             .background(Color.White)
                             .padding(10.dp)


                     ){

                       Box(
                           modifier = Modifier
                               .padding(end = 10.dp)
                               .clip(CircleShape)
                               .background(HeetoxWhite)
                               .padding(10.dp)

                       ){
                           Image(painter = painterResource(id = R.drawable.a),
                               contentDescription = "vitamin A image",
                               modifier = Modifier
                                   .size(20.dp)
                               )
                       }

                         Column {
                             Text(text = "Vitamin A"
                                 ,fontSize = 15.sp,
                                 color = Color.Black,
                                 fontWeight = FontWeight.Bold
                                 )

                             Text(text = "$vitamin_A mg"
                                 ,fontSize = 15.sp,
                                 color = Color.Black)
                         }

                     }


                    }






                    if (vitamin_B > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.b),
                                    contentDescription = "vitamin B image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Vitamin B"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$vitamin_B mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }


                    }





                    if (vitamin_C > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.c),
                                    contentDescription = "vitamin C image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Vitamin C"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$vitamin_C mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }





                    if (vitamin_D > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.d),
                                    contentDescription = "vitamin D image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Vitamin D"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$vitamin_D mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }





                    if (vitamin_E > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.e),
                                    contentDescription = "vitamin E image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Vitamin E"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$vitamin_E mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }





                    if (calcium > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.calcium),
                                    contentDescription = "calcium image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Calcium"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$calcium mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }





                    if (potassium > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.potassium),
                                    contentDescription = "Potassium image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Potassium"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$potassium mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }


                    }






                    if (iron > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.iron),
                                    contentDescription = "iron image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Iron"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$iron mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }




                    if (zinc > 0) {

                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.zinc),
                                    contentDescription = "Zinc image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Zinc"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$zinc mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }



                    if (phosphorous > 0) {

                        Spacer(modifier = Modifier.height(5.dp))


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.phosphorus),
                                    contentDescription = "Phosphorous image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Phosphorous"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$phosphorous mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }



                    if (magnessium > 0) {


                        Spacer(modifier = Modifier.height(5.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(10.dp)


                        ){

                            Box(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .clip(CircleShape)
                                    .background(HeetoxWhite)
                                    .padding(10.dp)

                            ){
                                Image(painter = painterResource(id = R.drawable.magnesium),
                                    contentDescription = "Magnesium image",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }

                            Column {
                                Text(text = "Magnesium"
                                    ,fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = "$magnessium mg"
                                    ,fontSize = 15.sp,
                                    color = Color.Black)
                            }

                        }
                    }


                }


            }
        }











//col end
        }




}
















//Ingredients
@Composable
fun Ingredients(data: List<List<Ingredient>>){


    var expandedItem by rememberSaveable { mutableStateOf<String?>(null) }


    Row(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Image(painter = painterResource(id = R.drawable.ingredient), contentDescription ="Nutrition Image" ,
            modifier = Modifier
                .size(50.dp)

        )


        Text(text = "See what's inside!",
            fontSize = 15.sp,
            color = HeetoxDarkGray,
            modifier = Modifier
                .padding(horizontal = 10.dp)
        )
    }




data.forEach {
    item ->


    item.forEach{

        Column(
            modifier = Modifier
                .padding(2.5.dp)
                .width(350.dp)
//                .shadow(2.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x1F23A247))
                .padding(15.dp)

        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(text = it.name,
                    color = Color.Black,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                )


                if (it.description != null) {
                    Icon(
                        imageVector = if (expandedItem == it.name) Icons.Filled.ArrowCircleUp else Icons.Filled.ArrowDropDown,
                        contentDescription = "Arrow",
                        tint = HeetoxDarkGreen,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        ){
                            expandedItem = if (expandedItem == it.name) null else it.name
                        }
                    )
                }

            }

            AnimatedVisibility(visible = expandedItem == it.name) {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(10.dp)
                ) {
                   Column {
                       Text(
                           text = it.description ?: "Not Available",
                           color = Color.Black,
                           fontSize = 14.sp
                       )

                   if(it.keywords.isNotEmpty()){

                       Spacer(modifier = Modifier
                           .height(10.dp)
                       )

                       Row {
                           Text(
                               text = "Other Names : " ?: "Not Available",
                               color = HeetoxDarkGray,
                               fontSize = 14.sp
                           )

                           Column {
                               it.keywords.forEach {

                                   Text(
                                       text = "$it " ?: "Not Available",
                                       color = HeetoxDarkGray,
                                       fontSize = 14.sp,
                                       modifier = Modifier
                                           .padding(bottom = 2.dp)
                                   )

                               }
                           }
                       }

                   }


                   }



                   }
            }


        }

    }
}


}











@Composable
fun AlternativeList(ProductVM: ProductsViewModel, token : String, UserData : State<LocalStoredData?>, navController: NavHostController,barcode :String
){





    val data = ProductVM.AlternativeProductData.collectAsState()
    val datalist = data.value.data?.take(6)
    val context = LocalContext.current

    var isApiCalled by rememberSaveable {
        mutableStateOf(false)
    }



    if(isApiCalled == false) {

        LaunchedEffect(key1 = barcode) {

            datalist?.get(0)?.let { ProductVM.getalternativeproducts(it.product_category, token) }

            Log.d("-->", "AlternativeList: jkbjbjkb")

        }

        isApiCalled = true



    }



    if (datalist != null) {



        Column {


            Text(text = "Alternatives",
                fontSize = 15.sp,
                color = HeetoxDarkGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(10.dp)
                )


            datalist.forEach {

                    data ->

                var isLiked by remember { mutableStateOf(data.isliked) }
                var likeCount by remember { mutableStateOf(data.likesCount) }


                ProductCard(
                    data = data,
                    ProductVM = ProductVM,
                    UserData = UserData,
                    context = context,
                    navController,
                    isLiked = isLiked,
                    likeCount = likeCount ,
                    onLikeCountChange = { likeCount = it },
                    onLikeChange = { isLiked = it }
                )


            }
        }



        Button(onClick = {

            navController.navigate("productlist/${data.value.data?.get(0)?.product_category}")

        }) {


            Text(text = "See More")

        }



    }







    LaunchedEffect(key1 = data.value) {

        when(data.value){

            is Resource.Error -> {

            }
            is Resource.Loading -> {

            }
            is Resource.Nothing -> {

            }
            is Resource.Success -> {



            }


        }

    }

}








