package com.heetox.app.Screens.AuthenticationScreens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.heetox.app.Composables.ProductCompose.SegmentedControl
import com.heetox.app.Di.checkInternetConnection
import com.heetox.app.Model.Product.CalorieData
import com.heetox.app.Model.Product.ProductsData
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.Authentication.AuthenticationViewModel
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConsumedProductScreen(navController: NavHostController,ProductVM : ProductsViewModel,AuthVM:AuthenticationViewModel) {


    val ConsumedWeekData = ProductVM.ConsumedWeekData.collectAsState()
    val ConsumedDayData = ProductVM.ConsumedDayData.collectAsState()
    val ConsumedMonthData = ProductVM.ConsumedMonthData.collectAsState()
    val DeleteConsumeData = ProductVM.DeleteConsumedProductData.collectAsState()

    val UserData = AuthVM.Localdata.collectAsState()
    val token = UserData.value?.Token

    val currentDate: LocalDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate: String = currentDate.format(formatter)

    val MWD = listOf("Month", "Week", "Day")
    var MWDselectedIndex by rememberSaveable {
        mutableIntStateOf(2)
    }

    val Days = listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")
    var DaysselectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }


    var SelectedWeek by rememberSaveable {
        mutableStateOf("currentweek")
    }

    var Dates by rememberSaveable { mutableStateOf(listOf<String>()) }


    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var scrollState = rememberScrollState()

    var weekcalories by rememberSaveable {

        mutableStateOf(mutableListOf(0,0,0,0,0,0,0))

    }



    //get week data
    LaunchedEffect(key1 = ConsumedWeekData) {


        if (token != null) {
            ProductVM.getConsumedWeekData(token, "currentweek")
        }


    }



// set dates in days list
    LaunchedEffect(key1 = ConsumedWeekData.value.data?.weekData) {

        val tempdates = mutableListOf<String>()

        ConsumedWeekData.value.data?.weekData?.forEachIndexed { index,item->

            tempdates.add(item.date)

            if(item.products.isNotEmpty()){

                weekcalories[index] = (item.per_day_NutritionalValue.energy * 0.239).toInt()

            }else{
                weekcalories[index] = 0
            }


        }

        Dates = tempdates

        Dates.forEach {

                item ->

            if (item == formattedDate) {

                DaysselectedIndex = Dates.indexOf(item)

            }

        }

        Log.d("date", "ConsumedProductScreen: $formattedDate")

    }




    //get data of particular day
    if (Dates.isNotEmpty()) {

        LaunchedEffect(key1 = DaysselectedIndex) {


            if (Dates.isNotEmpty()){

                if (token != null) {
                    ProductVM.getConsumedDayData(
                        token,
                        Dates[DaysselectedIndex]
                    )
                }

            }

        }
    }



    val isConnected = checkInternetConnection()

if(!isConnected){
    error = "No! internet Connection"
}




       Column(
           modifier = Modifier
               .fillMaxSize()
               .verticalScroll(scrollState)
               .background(HeetoxWhite)
       ) {


           //segmented control month week day
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(top = 15.dp),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally,


               ) {
               SegmentedControl(
                   options = MWD,
                   selectedIndex = MWDselectedIndex,
                   onOptionSelected = { MWDselectedIndex = it },
                   bgcolor = Color.White,
                   selectedColor = HeetoxDarkGreen,
                   selectedTextcolour = HeetoxWhite,
                   width = 355
               )

           }










//days Data
           if (MWDselectedIndex == 2) {



               //  day data resources
               LaunchedEffect(key1 = ConsumedDayData.value) {

                   when (ConsumedDayData.value) {
                       is Resource.Error -> {
                           loading = false
                           error = "oops! something went wrong"
                       }

                       is Resource.Loading -> {

                           error = ""
                           loading = true
                       }

                       is Resource.Nothing -> {
                           error = ""
                           loading = false

                       }

                       is Resource.Success -> {

                           error = ""
                           loading = false

                       }
                   }

               }



               Column(
                   modifier = Modifier
                       .fillMaxWidth(),
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally

               ){


//arrow and start end date
                   Row(
                       modifier = Modifier
                           .width(400.dp)
                           .height(40.dp)
                           .padding(vertical = 0.dp, horizontal = 15.dp),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center
                   ) {


                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ) {

                           if (ConsumedWeekData.value.data?.weekDatacondition?.hasPreviousWeek == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           ProductVM.getConsumedWeekData(
                                               token,
                                               ConsumedWeekData.value.data!!.weekDatacondition.previousWeek
                                           )

                                           SelectedWeek = ConsumedWeekData.value.data!!.weekDatacondition.previousWeek
                                       }

                                       DaysselectedIndex = 6

                                   },
                                   colors = ButtonDefaults.buttonColors(
                                       contentColor = HeetoxDarkGreen,
                                       containerColor = HeetoxWhite
                                   ),
                               ) {

                                   Icon(
                                       imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                                       contentDescription = "back",
                                   )

                               }
                           }

                       }


                       Column(
                           modifier = Modifier
                               .width(200.dp),
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {

                           Text(
                               text = ConsumedWeekData.value.data?.date_range ?: "Start - End Date",
                               fontSize = 16.sp,
                               color = HeetoxDarkGray,
                               modifier = Modifier
                           )

                       }




                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ){

                           if (ConsumedWeekData.value.data?.weekDatacondition?.hasNextWeek == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           ProductVM.getConsumedWeekData(
                                               token,
                                               ConsumedWeekData.value.data!!.weekDatacondition.nextWeek
                                           )

                                           SelectedWeek = ConsumedWeekData.value.data!!.weekDatacondition.nextWeek
                                       }

                                       DaysselectedIndex = 0

                                   },
                                   colors = ButtonDefaults.buttonColors(
                                       contentColor = HeetoxDarkGreen,
                                       containerColor = HeetoxWhite
                                   )

                               ) {

                                   Icon(
                                       imageVector = Icons.Default.KeyboardDoubleArrowRight,
                                       contentDescription = "back"
                                   )


                               }
                           }
                       }


                   }




                   //dates consumed
                   Row(
                       modifier = Modifier
                           .padding(start = 25.dp, top = 10.dp)
                           .width(500.dp)
//

                   ){
                       Text(

                           text = "Consumed",
                           fontSize = 18.sp,
                           color = HeetoxDarkGray,
                           fontWeight = FontWeight.Bold,
                           modifier = Modifier

                       )


                       if(Dates.size > 0){
                           Text(

                               text = " - ${Dates[DaysselectedIndex]}",
                               fontSize = 18.sp,
                               color = HeetoxDarkGray,
                               fontWeight = FontWeight.Bold,
                               modifier = Modifier


                           )
                       }

                   }



                   //calories
                   Row(

                       modifier = Modifier
                           .width(500.dp)
                           .padding(start = 25.dp, top = 5.dp),
                       verticalAlignment = Alignment.Bottom

                   ){

                       Text(text = "${
                           ConsumedDayData.value.data?.totalNutritionalValue?.energy?.times(
                               0.239
                           )?.toInt() ?: "Calories"
                       }",
                           color = HeetoxDarkGreen,
                           fontSize = 25.sp,
                           modifier = Modifier,
                           textAlign = TextAlign.Start,
                           fontWeight = FontWeight.Bold

                       )

                       if(ConsumedDayData.value.data?.totalNutritionalValue?.energy != null){
                           Text(text = " Kcal",
                               color = HeetoxDarkGreen,
                               fontSize = 25.sp,
                               modifier = Modifier,
                               textAlign = TextAlign.Start,
                               fontWeight = FontWeight.Bold
                           )
                       }
                   }






                   val calorieDataList = listOf(
                       CalorieData("Sun", weekcalories[0]),
                       CalorieData("Mon", weekcalories[1]),
                       CalorieData("Tue", weekcalories[2]),
                       CalorieData("Wed", weekcalories[3]),
                       CalorieData("Thu", weekcalories[4]),
                       CalorieData("Fri", weekcalories[5]),
                       CalorieData("Sat", weekcalories[6])
                   )

//                val calorieDataList = listOf(
//                    CalorieData("Sun",0),
//                    CalorieData("Mon", 0),
//                    CalorieData("Tue",0),
//                    CalorieData("Wed", 0),
//                    CalorieData("Thu", weekcalories[4]),
//                    CalorieData("Fri", weekcalories[5]),
//                    CalorieData("Sat", weekcalories[6])
//                )


                   Log.d("kcal", "ConsumedProductScreen: $weekcalories ")




                   VerticalCalorieBarChart(calorieData = calorieDataList,DaysselectedIndex){
                       DaysselectedIndex = it
                   }


                   //segmented days
                   SegmentedControl(
                       options = Days,
                       selectedIndex = DaysselectedIndex,
                       onOptionSelected = { DaysselectedIndex = it },
                       width = 353,
                       bgcolor = Color.White,
                       selectedColor = Color.White,
                       selectedTextcolour = HeetoxDarkGreen
                   )

                   Spacer(modifier = Modifier.height(10.dp))



                   if (loading || error.isNotEmpty()) {

                       if (loading) {

                           Spacer(modifier = Modifier.height(40.dp))

                           CircularProgressIndicator(
                               color = HeetoxDarkGreen,
                               modifier = Modifier
                                   .size(40.dp)
                           )

                       } else {


                           Spacer(modifier = Modifier.height(40.dp))

                           Text(
                               text = error,
                               color = HeetoxDarkGray,
                               fontSize = 15.sp

                           )


                       }

                   }else{



                       if (ConsumedDayData.value.data?.sanitized_data?.isNotEmpty() == true) {


                           Column(

                               modifier = Modifier
                                   .padding(horizontal = 20.dp)
                                   .width(500.dp)
                                   .clip(RoundedCornerShape(20.dp))
                                   .background(Color.White)
                                   .padding(20.dp)

                           ){

                               NutritionalValues(
                                   calcium = ConsumedDayData.value.data!!.totalNutritionalValue.calcium,
                                   cholesterol = ConsumedDayData.value.data!!.totalNutritionalValue.cholestrol,
                                   dietaryFibre = ConsumedDayData.value.data!!.totalNutritionalValue.dietry_fibre,
                                   energy = ConsumedDayData.value.data!!.totalNutritionalValue.energy,
                                   iron = ConsumedDayData.value.data!!.totalNutritionalValue.iron,
                                   magnesium = ConsumedDayData.value.data!!.totalNutritionalValue.magnessium,
                                   phosphorous = ConsumedDayData.value.data!!.totalNutritionalValue.phosphorous,
                                   potassium = ConsumedDayData.value.data!!.totalNutritionalValue.potassium,
                                   protein = ConsumedDayData.value.data!!.totalNutritionalValue.protein,
                                   saturatedFats = ConsumedDayData.value.data!!.totalNutritionalValue.saturates_fats,
                                   sodium = ConsumedDayData.value.data!!.totalNutritionalValue.sodium,
                                   totalCarbohydrates = ConsumedDayData.value.data!!.totalNutritionalValue.total_carbohydrates,
                                   totalFats = ConsumedDayData.value.data!!.totalNutritionalValue.total_fats,
                                   totalSugar = ConsumedDayData.value.data!!.totalNutritionalValue.total_sugar,
                                   transFats = ConsumedDayData.value.data!!.totalNutritionalValue.trans_fats,
                                   unsaturatedFats = ConsumedDayData.value.data!!.totalNutritionalValue.saturates_fats,
                                   vitaminA = ConsumedDayData.value.data!!.totalNutritionalValue.vitamin_A,
                                   vitaminB = ConsumedDayData.value.data!!.totalNutritionalValue.vitamin_B,
                                   vitaminC = ConsumedDayData.value.data!!.totalNutritionalValue.vitamin_C,
                                   vitaminD = ConsumedDayData.value.data!!.totalNutritionalValue.vitamin_D,
                                   vitaminE = ConsumedDayData.value.data!!.totalNutritionalValue.vitamin_E,
                                   zinc = ConsumedDayData.value.data!!.totalNutritionalValue.zinc
                               )

                           }


                           Text(

                               text = "Total Products Consumed (${ConsumedDayData.value.data!!.sanitized_data.size})",
                               fontSize = 18.sp,
                               color = HeetoxDarkGray,
                               fontWeight = FontWeight.Bold,
                               modifier = Modifier
                                   .padding(top = 20.dp, start = 34.dp)
                                   .width(500.dp)


                           )


                           Spacer(modifier = Modifier.height(10.dp))


                           ConsumedDayData.value.data?.sanitized_data?.forEach {

                                   item ->

                               if (token != null) {
                                   ConsumedItem(data = item, ProductVM, token,navController)
                               }


                               if (ConsumedDayData.value.data?.sanitized_data!!.size - 1 != ConsumedDayData.value.data?.sanitized_data?.indexOf(item)) {

                                   Column(
                                       modifier = Modifier
                                   ) {
                                       Icon(
                                           imageVector = Icons.Default.KeyboardArrowDown,
                                           contentDescription = "",
                                           tint = HeetoxDarkGray,
                                           modifier = Modifier.size(30.dp)
                                       )
                                   }

                               }


                           }

                           Spacer(modifier = Modifier.height(20.dp))

                       } else {


                           if (!loading && ConsumedDayData.value.data?.sanitized_data?.isEmpty() == true) {
                               Text(
                                   text = "Total Products Consumed (0)",
                                   fontSize = 18.sp,
                                   color = HeetoxDarkGray,
                                   fontWeight = FontWeight.Bold,
                                   modifier = Modifier
                                       .fillMaxWidth()
                                       .padding(top = 20.dp, start = 34.dp)
                               )
                           }

                       }



                   }


               }



           }




//week data
           if(MWDselectedIndex == 1) {


// week data resources
               LaunchedEffect(key1 = ConsumedWeekData.value) {

                   when (ConsumedWeekData.value) {

                       is Resource.Error -> {
                           loading = false
                           error = "oops! something went wrong"
                       }

                       is Resource.Loading -> {

                           error = ""
                           loading = true
                       }

                       is Resource.Nothing -> {

                           error = ""
                           loading = false

                       }

                       is Resource.Success -> {

                           error = ""
                           loading = false

                           if (token != null) {
                               ProductVM.getConsumedDayData(
                                   token,
                                   Dates[DaysselectedIndex]
                               )
                           }
                       }
                   }

               }


               //arrow and start end date
               Column(
                   modifier = Modifier
                       .fillMaxWidth(),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   Row(
                       modifier = Modifier
                           .width(400.dp)
                           .height(40.dp)
                           .padding(vertical = 0.dp, horizontal = 15.dp),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center
                   ) {


                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ) {

                           if (ConsumedWeekData.value.data?.weekDatacondition?.hasPreviousWeek == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           ProductVM.getConsumedWeekData(
                                               token,
                                               ConsumedWeekData.value.data!!.weekDatacondition.previousWeek
                                           )

                                           SelectedWeek =
                                               ConsumedWeekData.value.data!!.weekDatacondition.previousWeek

                                       }

                                       DaysselectedIndex = 6

                                   },
                                   colors = ButtonDefaults.buttonColors(
                                       contentColor = HeetoxDarkGreen,
                                       containerColor = HeetoxWhite
                                   ),
                               ) {

                                   Icon(
                                       imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                                       contentDescription = "back",
                                   )

                               }
                           }

                       }


                       Column(
                           modifier = Modifier
                               .width(200.dp),
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {

                           Text(
                               text = ConsumedWeekData.value.data?.date_range ?: "Start - End Date",
                               fontSize = 16.sp,
                               color = HeetoxDarkGray,
                               modifier = Modifier
                           )

                       }




                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ) {

                           if (ConsumedWeekData.value.data?.weekDatacondition?.hasNextWeek == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           ProductVM.getConsumedWeekData(
                                               token,
                                               ConsumedWeekData.value.data!!.weekDatacondition.nextWeek
                                           )

                                           SelectedWeek =
                                               ConsumedWeekData.value.data!!.weekDatacondition.nextWeek

                                       }

                                       DaysselectedIndex = 0

                                   },
                                   colors = ButtonDefaults.buttonColors(
                                       contentColor = HeetoxDarkGreen,
                                       containerColor = HeetoxWhite
                                   )

                               ) {

                                   Icon(
                                       imageVector = Icons.Default.KeyboardDoubleArrowRight,
                                       contentDescription = "back"
                                   )


                               }
                           }
                       }


                   }



                   Row(
                       modifier = Modifier
                           .width(500.dp)
                           .padding(vertical = 5.dp, horizontal = 34.dp)
                   ) {

                       Text(

                           text = "Consumed",
                           fontSize = 18.sp,
                           color = HeetoxDarkGray,
                           fontWeight = FontWeight.Bold,

                           )


                   }


                   Spacer(modifier = Modifier.height(10.dp))


                   Column(
                       modifier = Modifier
                           .fillMaxWidth(),
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {


                       if (loading || error.isNotEmpty()) {
                           if (loading) {
                               Spacer(modifier = Modifier.height(40.dp))

                               CircularProgressIndicator(
                                   color = HeetoxDarkGreen,
                                   modifier = Modifier.size(40.dp)
                               )
                           } else {
                               Spacer(modifier = Modifier.height(40.dp))

                               Text(
                                   text = error,
                                   color = HeetoxDarkGray,
                                   fontSize = 15.sp
                               )
                           }
                       } else {
                           // Ensure ConsumedWeekData has non-empty week data
                           val weekData = ConsumedWeekData.value.data?.totalproductconsumed


                           if (weekData != null) {

                               if (weekData > 0) {
                                   Column(
                                       modifier = Modifier
                                           .padding(horizontal = 20.dp)
                                           .width(500.dp)
                                           .clip(RoundedCornerShape(20.dp))
                                           .background(Color.White)
                                           .padding(20.dp)
                                   ) {


                                       NutritionalValues(
                                           calcium = ConsumedWeekData.value.data!!.totalNutritionalValue.calcium,
                                           cholesterol = ConsumedWeekData.value.data!!.totalNutritionalValue.cholestrol,
                                           dietaryFibre = ConsumedWeekData.value.data!!.totalNutritionalValue.dietry_fibre,
                                           energy = ConsumedWeekData.value.data!!.totalNutritionalValue.energy,
                                           iron = ConsumedWeekData.value.data!!.totalNutritionalValue.iron,
                                           magnesium = ConsumedWeekData.value.data!!.totalNutritionalValue.magnessium,
                                           phosphorous = ConsumedWeekData.value.data!!.totalNutritionalValue.phosphorous,
                                           potassium = ConsumedWeekData.value.data!!.totalNutritionalValue.potassium,
                                           protein = ConsumedWeekData.value.data!!.totalNutritionalValue.protein,
                                           saturatedFats = ConsumedWeekData.value.data!!.totalNutritionalValue.saturates_fats,
                                           sodium = ConsumedWeekData.value.data!!.totalNutritionalValue.sodium,
                                           totalCarbohydrates = ConsumedWeekData.value.data!!.totalNutritionalValue.total_carbohydrates,
                                           totalFats = ConsumedWeekData.value.data!!.totalNutritionalValue.total_fats,
                                           totalSugar = ConsumedWeekData.value.data!!.totalNutritionalValue.total_sugar,
                                           transFats = ConsumedWeekData.value.data!!.totalNutritionalValue.trans_fats,
                                           unsaturatedFats = ConsumedWeekData.value.data!!.totalNutritionalValue.saturates_fats,
                                           vitaminA = ConsumedWeekData.value.data!!.totalNutritionalValue.vitamin_A,
                                           vitaminB = ConsumedWeekData.value.data!!.totalNutritionalValue.vitamin_B,
                                           vitaminC = ConsumedWeekData.value.data!!.totalNutritionalValue.vitamin_C,
                                           vitaminD = ConsumedWeekData.value.data!!.totalNutritionalValue.vitamin_D,
                                           vitaminE = ConsumedWeekData.value.data!!.totalNutritionalValue.vitamin_E,
                                           zinc = ConsumedWeekData.value.data!!.totalNutritionalValue.zinc
                                       )
                                   }

                                   Text(

                                       text = "Total Products Consumed (${weekData})",
                                       fontSize = 18.sp,
                                       color = HeetoxDarkGray,
                                       fontWeight = FontWeight.Bold,
                                       modifier = Modifier
                                           .width(500.dp)
                                           .padding(top = 20.dp, start = 34.dp)

                                   )



                                   Spacer(modifier = Modifier.height(10.dp))


                                   ConsumedWeekData.value.data!!.weekData.forEach {

                                           item ->


                                       if (item.products.size > 0) {
                                           Text(

                                               text = item.date,
                                               fontSize = 18.sp,
                                               color = HeetoxDarkGray,
                                               fontWeight = FontWeight.Bold,
                                               modifier = Modifier
                                                   .width(500.dp)
                                                   .padding(top = 20.dp, start = 34.dp)

                                           )
                                           Spacer(modifier = Modifier.height(5.dp))
                                       }


                                       item.products.forEach {

                                               eachproduct ->

                                           if (token != null) {
                                               ConsumedItem(
                                                   data = eachproduct,
                                                   ProductVM,
                                                   token,
                                                   navController
                                               )
                                           }

                                       }


                                   }

                                   Spacer(modifier = Modifier.height(20.dp))


                               } else {
                                   // Display this message when weekData is empty or null
                                   Text(
                                       text = "No products consumed this week.",
                                       fontSize = 18.sp,
                                       color = HeetoxDarkGray,
                                       fontWeight = FontWeight.Bold,
                                       modifier = Modifier
                                           .fillMaxWidth()
                                           .padding(top = 20.dp, start = 34.dp)
                                   )
                               }
                           }


                       }


                   }


               }


           }




           //month data

           if(MWDselectedIndex == 0){



               //get Month Data
               LaunchedEffect(key1 = Unit) {

                   if (token != null) {
                       ProductVM.getConsumedMonthData(token,"currentmonth")
                   }


               }



               // Month data resources
               LaunchedEffect(key1 = ConsumedMonthData.value) {

                   when (ConsumedMonthData.value) {

                       is Resource.Error -> {
                           loading = false
                           error = "oops! something went wrong"
                       }

                       is Resource.Loading -> {

                           error = ""
                           loading = true
                       }

                       is Resource.Nothing -> {

                           error = ""
                           loading = false

                       }

                       is Resource.Success -> {

                           error = ""
                           loading = false

                       }
                   }

               }


               Column(
                   modifier = Modifier
                       .fillMaxWidth(),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {

                   //arrows and range
                   Row(
                       modifier = Modifier
                           .width(400.dp)
                           .height(40.dp)
                           .padding(vertical = 0.dp, horizontal = 15.dp),
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center
                   ) {


                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ) {

                           if (ConsumedMonthData.value.data?.month_condition?.hasPreviousMonth == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           ProductVM.getConsumedMonthData(
                                               token,
                                               ConsumedMonthData.value.data?.month_condition!!.previousMonth
                                           )

                                       }

                                   },
                                   colors = ButtonDefaults.buttonColors(
                                       contentColor = HeetoxDarkGreen,
                                       containerColor = HeetoxWhite
                                   ),
                               ) {

                                   Icon(
                                       imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                                       contentDescription = "back",
                                   )

                               }
                           }

                       }


                       Column(
                           modifier = Modifier
                               .width(200.dp),
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {

                           Text(
                               text = ConsumedMonthData.value.data?.month_range
                                   ?: "Start - End Date",
                               fontSize = 16.sp,
                               color = HeetoxDarkGray,
                               modifier = Modifier
                           )

                       }




                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ) {

                           if (ConsumedMonthData.value.data?.month_condition?.hasNextMonth == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           ProductVM.getConsumedMonthData(
                                               token,
                                               ConsumedMonthData.value.data!!.month_condition.nextMonth
                                           )

                                       }

                                   },
                                   colors = ButtonDefaults.buttonColors(
                                       contentColor = HeetoxDarkGreen,
                                       containerColor = HeetoxWhite
                                   )

                               ) {

                                   Icon(
                                       imageVector = Icons.Default.KeyboardDoubleArrowRight,
                                       contentDescription = "back"
                                   )


                               }
                           }
                       }


                   }


                   if (loading || error.isNotEmpty()) {
                       if (loading) {
                           Spacer(modifier = Modifier.height(40.dp))

                           CircularProgressIndicator(
                               color = HeetoxDarkGreen,
                               modifier = Modifier.size(40.dp)
                           )
                       } else {
                           Spacer(modifier = Modifier.height(40.dp))

                           Text(
                               text = error,
                               color = HeetoxDarkGray,
                               fontSize = 15.sp
                           )
                       }
                   } else {

                       if(ConsumedMonthData.value.data?.totalproductconsumed != null && ConsumedMonthData.value.data?.totalproductconsumed!! > 0 ){


                           Text(

                               text = "Total Products Consumed (${ConsumedMonthData.value.data!!.totalproductconsumed})",
                               fontSize = 18.sp,
                               color = HeetoxDarkGray,
                               fontWeight = FontWeight.Bold,
                               modifier = Modifier
                                   .width(500.dp)
                                   .padding(top = 20.dp, start = 34.dp)

                           )


                           Spacer(modifier = Modifier.height(10.dp))

                           //nutrition total


                           Column(
                               modifier = Modifier
                                   .padding(horizontal = 20.dp)
                                   .width(500.dp)
                                   .clip(RoundedCornerShape(20.dp))
                                   .background(Color.White)
                                   .padding(20.dp)
                           ) {

                               ConsumedMonthData.value.data?.totalNutritionalValue?.let {
                                   NutritionalValues(
                                       calcium = it.calcium,
                                       cholesterol = it.cholestrol,
                                       dietaryFibre = it.dietry_fibre,
                                       energy = it.energy,
                                       iron = it.iron,
                                       magnesium = it.magnessium,
                                       phosphorous = it.phosphorous,
                                       potassium = it.potassium,
                                       protein = it.protein,
                                       saturatedFats = it.saturates_fats,
                                       sodium = it.sodium,
                                       totalCarbohydrates = it.total_carbohydrates,
                                       totalFats = it.total_fats,
                                       totalSugar = it.total_sugar,
                                       transFats = it.trans_fats,
                                       unsaturatedFats = it.unsaturated_fats.toDouble(),
                                       vitaminA = it.vitamin_A,
                                       vitaminB = it.vitamin_B,
                                       vitaminC = it.vitamin_C,
                                       vitaminD = it.vitamin_D,
                                       vitaminE = it.vitamin_E,
                                       zinc = it.zinc
                                   )
                               }
                           }

                       }else{

                           Text(

                               text = "Total Products Consumed (0)",
                               fontSize = 18.sp,
                               color = HeetoxDarkGray,
                               fontWeight = FontWeight.Bold,
                               modifier = Modifier
                                   .width(500.dp)
                                   .padding(top = 20.dp, start = 34.dp)

                           )

                       }



                   }

                   Spacer(modifier = Modifier.height(20.dp))


               }




           }













       }


    LaunchedEffect(key1 = DeleteConsumeData.value) {

        when (DeleteConsumeData.value) {

            is Resource.Error -> {
                loading = false
                error = "oops! something went wrong"
            }

            is Resource.Loading -> {

                error = ""
                loading = true
            }

            is Resource.Nothing -> {
                error = ""
                loading = false
            }

            is Resource.Success -> {

                error = ""
                loading = false

                if (token != null) {

                    ProductVM.getConsumedWeekData(
                        token,
                        SelectedWeek
                    )

//                    ProductVM.getConsumedDayData(
//                        token,
//                        Dates[DaysselectedIndex]
//                    )


                }


            }

        }


    }

}










@Composable
fun ConsumedItem(data : ProductsData,ProductVM: ProductsViewModel,token : String,navController: NavHostController){

Row(
    modifier = Modifier
        .padding(horizontal = 20.dp, vertical = 5.dp)
        .width(500.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Color.White)
        .padding(10.dp)
        .clickable {

            navController.navigate("productdetails/${data.consumed_products.product_barcode}")


        }
    ,
    horizontalArrangement = Arrangement.Center,

) {


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(HeetoxLightGray)
            .padding(5.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = data.consumed_products.product_front_image,
            contentDescription = "",
            modifier = Modifier.size(100.dp)
        )


    }




    Column(
        modifier = Modifier
            .weight(1f)
            .padding(vertical = 10.dp)
    ) {


        Column(

            modifier = Modifier
                .padding(horizontal = 10.dp)

        ){
            Text(
                text = data.consumed_At_time.ifEmpty { "NA" },
                fontSize = 12.sp,
                color = HeetoxDarkGray,
                modifier = Modifier.fillMaxWidth(),
            )
        }

//        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {


            Column(
                modifier = Modifier
//                .weight(1f)
                    .padding(start = 10.dp)
            ) {

                val truncatedText = data.consumed_products.product_name
                    .split(" ") // Split into words
                    .take(3) // Take only the first 3 words
                    .joinToString(" ") // Join them back
                    .let { if (it.length < data.consumed_products.product_name.length) "$it..." else it } // Append "..." if truncated

                Text(
                    text = truncatedText.ifEmpty { "NA" },
                    fontSize = 12.sp,
                    color = Color.Black
                )

                Text(
                    text = data.consumed_At_date.ifEmpty { "NA" },
                    fontSize = 12.sp,
                    color = Color.Black,
                )

                Text(
                    text = data.serving_size.toString().take(5) + " g".ifEmpty { "NA" },
                    fontSize = 12.sp,
                    color = Color.Black,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()  // Ensures the column fills the available height
                    .padding(end = 10.dp)  // Optional padding if needed
                    .clickable {

                        ProductVM.deleteConsumedProduct(token, data._id)

                    },
                verticalArrangement = Arrangement.Bottom  // Aligns the content to the bottom
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete button",
                    tint = HeetoxDarkGray
                )
            }
        }

    }
}

}






@Composable
fun NutritionalValues(
    calcium: Double,
    cholesterol: Int,
    dietaryFibre: Double,
    energy: Double,
    iron: Double,
    magnesium: Int,
    phosphorous: Int,
    potassium: Double,
    protein: Double,
    saturatedFats: Double,
    sodium: Double,
    totalCarbohydrates: Double,
    totalFats: Double,
    totalSugar: Double,
    transFats: Double,
    unsaturatedFats: Double,
    vitaminA: Double,
    vitaminB: Int,
    vitaminC: Double,
    vitaminD: Int,
    vitaminE: Int,
    zinc: Int
) {


    val nutritionalData = listOf(
        "Energy" to energy,

        "Protein" to protein,

        "Total Carbohydrates" to totalCarbohydrates,
        "Dietary Fibre" to dietaryFibre,
        "Total Sugar" to totalSugar,

        "Total Fats" to totalFats,
        "Saturated Fats" to saturatedFats,
        "Unsaturated Fats" to unsaturatedFats,
        "Trans Fats" to transFats,

        "Sodium" to sodium,

        "Cholesterol" to cholesterol,

        "Vitamin A" to vitaminA,
        "Vitamin B" to vitaminB,
        "Vitamin C" to vitaminC,
        "Vitamin D" to vitaminD,
        "Vitamin E" to vitaminE,

        "Iron" to iron,
        "Magnesium" to magnesium,
        "Phosphorous" to phosphorous,
        "Potassium" to potassium,
        "Calcium" to calcium,
        "Zinc" to zinc
    )



    Column(modifier = Modifier) {


        val decimalFormat = DecimalFormat("#.##")

        nutritionalData.forEach { (label, value) ->
            if (value != 0.0 && value != 0) {
                Row {
                    Text(
                        text = "$label : ",
                        fontSize = 15.sp,
                        color = HeetoxDarkGray,
                        modifier = Modifier.padding(vertical = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                    val formattedValue = decimalFormat.format(value)
                    if (label == "Sodium") {
                        Text(
                            text = "$formattedValue mg",
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }else if(label == "Energy") {

                        Text(
                            text = "$formattedValue (${decimalFormat.format(formattedValue.toDouble() * 0.239)} kcal)",
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )

                    }else{
                        Text(
                            text = "$formattedValue g",
                            fontSize = 15.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                }
            }
        }

    }

}



@Composable
fun VerticalCalorieBarChart(calorieData: List<CalorieData>, dayIndex: Int, onClick :(Int) -> Unit){
    val maxCalories = calorieData.maxOf { it.calories } // Get the maximum calorie value

    val selectedBarColor = HeetoxDarkGreen
    val unselectedBarColor = HeetoxLightGray
    val selectedTextColor = HeetoxDarkGreen
    val unselectedTextColor = HeetoxDarkGray

    Row(
        modifier = Modifier
            .width(390.dp)
            .padding(horizontal = 25.dp)

        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {

        calorieData.forEachIndexed { index, data ->
            val isSelected = index == dayIndex // Determine if the current bar is selected

            CalorieBarVertical(
                day = data.day,
                calories = data.calories,
                maxCalories = maxCalories,
                isSelected = isSelected,
                selectedBarColor = selectedBarColor,
                unselectedBarColor = unselectedBarColor,
                selectedTextColor = selectedTextColor,
                unselectedTextColor = unselectedTextColor,
                index = index,
                onClick = {
                    onClick(index)
                }
            )
        }

    }
}




@Composable
fun CalorieBarVertical(
    day: String,
    calories: Int,
    maxCalories: Int,
    isSelected: Boolean,
    selectedBarColor: Color,
    unselectedBarColor: Color,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    index : Int
    ,
    onClick :(Int) -> Unit

) {
    // Calculate bar height as a fraction of the maximum calories
    val barHeightFraction = calories / maxCalories.toFloat()
    val maxHeight = 130.dp // Max height of the bar
    val barHeight = barHeightFraction * maxHeight.value

    // Set the colors based on the selection state
    val barColor = if (isSelected) selectedBarColor else unselectedBarColor
    val textColor = if (isSelected) selectedTextColor else unselectedTextColor

    Column(
        modifier = Modifier
            .width(40.dp) // Width of each bar
            .height(maxHeight + 10.dp)

        , // Total height (bar + label)
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom // Align items to the bottom
    ) {
        // Draw the bar using Canvas with variable height at the bottom
        Canvas(
            modifier = Modifier
                .height(barHeight.dp) // Variable height for each bar
                .width(30.dp) // Width of the bar
                .clickable {
                    onClick(index)
                }
        ) {
            drawRoundRect(
                color = barColor, // Use the selected or unselected bar color
                size = androidx.compose.ui.geometry.Size(width = size.width, height = size.height),
                cornerRadius = CornerRadius(x = 10f, y = 10f)
            )
        }


    }
}





