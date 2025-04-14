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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.heetox.app.Composables.ProductCompose.SegmentedControl
import com.heetox.app.Di.checkInternetConnection
import com.heetox.app.Model.Authentication.LocalStoredData
import com.heetox.app.Model.Product.CalorieData
import com.heetox.app.Model.Product.ProductsData
import com.heetox.app.Utils.Action
import com.heetox.app.Utils.UiEvent
import com.heetox.app.ViewModel.ProductsVM.ConsumeViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxLightGray
import com.heetox.app.ui.theme.HeetoxWhite
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConsumedProductScreen(navController: NavHostController,userData: LocalStoredData?){

    val consumedVM : ConsumeViewModel = hiltViewModel()

    val consumedWeekData = consumedVM.consumedWeekData.collectAsState()
    val consumedDayData = consumedVM.consumedDayData.collectAsState()
    val consumedMonthData = consumedVM.consumedMonthData.collectAsState()
    val deleteConsumeData = consumedVM.deleteConsumedProductData.collectAsState()

    val token = userData?.Token

    val currentDate: LocalDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate: String = currentDate.format(formatter)

    val mWD = listOf("Month", "Week", "Day")
    var mWDSelectedIndex by rememberSaveable {
        mutableIntStateOf(2)
    }

    val days = listOf("sun", "mon", "tue", "wed", "thu", "fri", "sat")
    var daysSelectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }


    var selectedWeek by rememberSaveable {
        mutableStateOf("currentweek")
    }

    var dates by rememberSaveable { mutableStateOf(listOf<String>()) }


    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    val scrollState = rememberScrollState()

    var weekCalories by rememberSaveable { mutableStateOf(List(7) { 0 }) }

    var updateDayIndexOnWeekLoad by rememberSaveable { mutableStateOf<Int?>(null) }

   // On first launch, fetch week data
    LaunchedEffect(Unit) {
        token?.let {
            consumedVM.getConsumedWeekData(it, "currentweek")
        }
    }

// After week data is loaded, populate dates
    LaunchedEffect(consumedWeekData.value.data?.weekData) {
        consumedWeekData.value.data?.weekData?.let { weekList ->
            val tempDates = weekList.map { it.date }

            // update calories
            val updatedCalories = weekList.mapIndexed { index, item ->
                if (item.products.isNotEmpty()) {
                    (item.per_day_NutritionalValue.energy * 0.239).toInt()
                } else 0
            }

            weekCalories = updatedCalories
            dates = tempDates

//            // auto-select current day if matched
//            dates.forEach {
//                    item -> if (item == formattedDate) {
//                    daysSelectedIndex = dates.indexOf(item)
//                }
//            }

            updateDayIndexOnWeekLoad?.let { index ->
                daysSelectedIndex = index
                updateDayIndexOnWeekLoad = null
            } ?: run {
                val index = tempDates.indexOf(formattedDate)
                if (index != -1) {
                    daysSelectedIndex = index
                }
            }
        }
    }

// Once the selected day is valid, fetch day data
    LaunchedEffect(dates, daysSelectedIndex) {
        if (dates.isNotEmpty() && daysSelectedIndex in dates.indices) {
            token?.let {
                consumedVM.getConsumedDayData(it, dates[daysSelectedIndex])
            }
        }
    }

//
//    //get week data
//    LaunchedEffect(key1 = Unit) {
//
//
//        if (token != null) {
//            consumedVM.getConsumedWeekData(token, "currentweek")
//        }
//
//
//    }
//
//
//
//// set dates in days list
//    LaunchedEffect(key1 = consumedWeekData.value.data?.weekData) {
//
//        val tempDates = mutableListOf<String>()
//
//        consumedWeekData.value.data?.weekData?.forEachIndexed { index, item->
//
//            tempDates.add(item.date)
//
//            Log.d("item", "ConsumedProductScreen: ${consumedWeekData.value.data?.weekData}")
//
//            if(item.products.isNotEmpty()){
//
//                weekCalories[index] = (item.per_day_NutritionalValue.energy * 0.239).toInt()
//
//            }else{
//                weekCalories[index] = 0
//            }
//
//
//        }
//
//        dates = tempDates
//
//        dates.forEach {
//
//                item ->
//
//            if (item == formattedDate) {
//
//                daysSelectedIndex = dates.indexOf(item)
//
//            }
//
//        }
//
//
//        Log.d("dates -->", "ConsumedProductScreen: ${dates}")
//
//
//    }
//
//
//
//
//    //get data of particular day
//    if (dates.isNotEmpty()) {
//
//        LaunchedEffect(key1 = dates[daysSelectedIndex]) {
//
//                    Log.d("date", "ConsumedProductScreen: ${dates[daysSelectedIndex]}")
//
//            if (dates.isNotEmpty()){
//
//                if (token != null) {
//                    consumedVM.getConsumedDayData(
//                        token,
//                        dates[daysSelectedIndex]
//                    )
//                }
//
//            }
//
//        }
//    }



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
                   options = mWD,
                   selectedIndex = mWDSelectedIndex,
                   onOptionSelected = { mWDSelectedIndex = it },
                   bgcolor = Color.White,
                   selectedColor = HeetoxDarkGreen,
                   selectedTextcolour = HeetoxWhite,
                   width = 355
               )

           }


//           Log.d("jkn", "CategoriesHome: ")







//days Data
           if (mWDSelectedIndex == 2) {


               LaunchedEffect(Unit) {
                   consumedVM.uiEvent.collect { event ->
                       when (event) {
                           is UiEvent.Loading -> {
                               if (event.action == Action.GetConsumedDayData) {
                                   loading = true
                                   error = ""
                               }
                           }

                           is UiEvent.Error -> {
                               if (event.action == Action.GetConsumedDayData) {
                                   loading = false
                                   error = "Oops! Something went wrong"
                               }
                           }

                           is UiEvent.Success -> {
                               if (event.action == Action.GetConsumedDayData) {
                                   loading = false
                                   error = ""
                               }
                           }

                           UiEvent.Idle -> Unit
                       }
                   }
               }


               //  day data resources
//               LaunchedEffect(key1 = consumedDayData.value) {
//
//                   when (consumedDayData.value) {
//                       is Resource.Error -> {
//                           loading = false
//                           error = "oops! something went wrong"
//                       }
//
//                       is Resource.Loading -> {
//
//                           error = ""
//                           loading = true
//                       }
//
//                       is Resource.Nothing -> {
//                           error = ""
//                           loading = false
//
//                       }
//
//                       is Resource.Success -> {
//
//                           error = ""
//                           loading = false
//
//                       }
//                   }
//
//               }



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

                           if (consumedWeekData.value.data?.weekDatacondition?.hasPreviousWeek == true) {

                               Button(
                                   onClick = {

                                       val previousWeek = consumedWeekData.value.data?.weekDatacondition?.previousWeek

                                       if (token != null && !previousWeek.isNullOrBlank()) {
                                           consumedVM.getConsumedWeekData(token, previousWeek)
                                           selectedWeek = previousWeek
                                       }

                                       updateDayIndexOnWeekLoad = 6
//                                       daysSelectedIndex = 6

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
                               text = consumedWeekData.value.data?.date_range ?: "Start - End Date",
                               fontSize = 16.sp,
                               color = HeetoxDarkGray,
                               modifier = Modifier
                           )

                       }




                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ){

                           if (consumedWeekData.value.data?.weekDatacondition?.hasNextWeek == true) {

                               Button(
                                   onClick = {

                                       val nextWeek = consumedWeekData.value.data?.weekDatacondition?.nextWeek

                                       if (token != null && !nextWeek.isNullOrBlank()) {
                                           consumedVM.getConsumedWeekData(token, nextWeek)
                                           selectedWeek = nextWeek
                                       }

                                       updateDayIndexOnWeekLoad = 0

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


                       if(dates.size > 0){
                           Text(

                               text = " - ${dates[daysSelectedIndex]}",
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
                           consumedDayData.value.data?.totalNutritionalValue?.energy?.times(
                               0.239
                           )?.toInt() ?: "Calories"
                       }",
                           color = HeetoxDarkGreen,
                           fontSize = 25.sp,
                           modifier = Modifier,
                           textAlign = TextAlign.Start,
                           fontWeight = FontWeight.Bold

                       )

                       if(consumedDayData.value.data?.totalNutritionalValue?.energy != null){
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
                       CalorieData("Sun", weekCalories[0]),
                       CalorieData("Mon", weekCalories[1]),
                       CalorieData("Tue", weekCalories[2]),
                       CalorieData("Wed", weekCalories[3]),
                       CalorieData("Thu", weekCalories[4]),
                       CalorieData("Fri", weekCalories[5]),
                       CalorieData("Sat", weekCalories[6])
                   )

                   Log.e("---->", "ConsumedProductScreen: ${weekCalories} ", )
                   Log.e("---->", "ConsumedProductScreen: ${calorieDataList} ", )

                   VerticalCalorieBarChart(calorieData = calorieDataList,daysSelectedIndex){
                       daysSelectedIndex = it
                   }


                   //segmented days
                   SegmentedControl(
                       options = days,
                       selectedIndex = daysSelectedIndex,
                       onOptionSelected = { daysSelectedIndex = it },
                       width = 353,
                       bgcolor = Color.White,
                       selectedColor = Color.White,
                       selectedTextcolour = HeetoxDarkGreen
                   )

                   Spacer(modifier = Modifier.height(10.dp))



                   if (loading || error.isNotEmpty()) {

                       if (loading) {

//                           Spacer(modifier = Modifier.height(40.dp))

                           Column(
                               modifier = Modifier
                                   .padding(horizontal = 20.dp)
                                   .width(500.dp)
                                   .height(300.dp)
                                   .clip(RoundedCornerShape(20.dp))
                                   .background(Color.White)
                                   .padding(20.dp),
                               horizontalAlignment = Alignment.CenterHorizontally,
                               verticalArrangement = Arrangement.Center
                           ){
                               CircularProgressIndicator(
                                   color = HeetoxDarkGreen,
                                   modifier = Modifier
                                       .size(40.dp)
                               )
                           }

                       } else {


                           Spacer(modifier = Modifier.height(40.dp))

                           Text(
                               text = error,
                               color = HeetoxDarkGray,
                               fontSize = 15.sp

                           )


                       }

                   }else{



                       if (consumedDayData.value.data?.sanitized_data?.isNotEmpty() == true) {


                           Column(

                               modifier = Modifier
                                   .padding(horizontal = 20.dp)
                                   .width(500.dp)
                                   .clip(RoundedCornerShape(20.dp))
                                   .background(Color.White)
                                   .padding(20.dp)

                           ){

                               NutritionalValues(
                                   calcium = consumedDayData.value.data!!.totalNutritionalValue.calcium,
                                   cholesterol = consumedDayData.value.data!!.totalNutritionalValue.cholestrol,
                                   dietaryFibre = consumedDayData.value.data!!.totalNutritionalValue.dietry_fibre,
                                   energy = consumedDayData.value.data!!.totalNutritionalValue.energy,
                                   iron = consumedDayData.value.data!!.totalNutritionalValue.iron,
                                   magnesium = consumedDayData.value.data!!.totalNutritionalValue.magnessium,
                                   phosphorous = consumedDayData.value.data!!.totalNutritionalValue.phosphorous,
                                   potassium = consumedDayData.value.data!!.totalNutritionalValue.potassium,
                                   protein = consumedDayData.value.data!!.totalNutritionalValue.protein,
                                   saturatedFats = consumedDayData.value.data!!.totalNutritionalValue.saturates_fats,
                                   sodium = consumedDayData.value.data!!.totalNutritionalValue.sodium,
                                   totalCarbohydrates = consumedDayData.value.data!!.totalNutritionalValue.total_carbohydrates,
                                   totalFats = consumedDayData.value.data!!.totalNutritionalValue.total_fats,
                                   totalSugar = consumedDayData.value.data!!.totalNutritionalValue.total_sugar,
                                   transFats = consumedDayData.value.data!!.totalNutritionalValue.trans_fats,
                                   unsaturatedFats = consumedDayData.value.data!!.totalNutritionalValue.saturates_fats,
                                   vitaminA = consumedDayData.value.data!!.totalNutritionalValue.vitamin_A,
                                   vitaminB = consumedDayData.value.data!!.totalNutritionalValue.vitamin_B,
                                   vitaminC = consumedDayData.value.data!!.totalNutritionalValue.vitamin_C,
                                   vitaminD = consumedDayData.value.data!!.totalNutritionalValue.vitamin_D,
                                   vitaminE = consumedDayData.value.data!!.totalNutritionalValue.vitamin_E,
                                   zinc = consumedDayData.value.data!!.totalNutritionalValue.zinc
                               )

                           }


                           Text(

                               text = "Total Products Consumed (${consumedDayData.value.data!!.sanitized_data.size})",
                               fontSize = 18.sp,
                               color = HeetoxDarkGray,
                               fontWeight = FontWeight.Bold,
                               modifier = Modifier
                                   .padding(top = 20.dp, start = 34.dp)
                                   .width(500.dp)


                           )


                           Spacer(modifier = Modifier.height(10.dp))


                           consumedDayData.value.data?.sanitized_data?.forEach {

                                   item ->

                               if (token != null) {
                                   ConsumedItem(data = item, deleteProduct = {
                                           token,id -> consumedVM.deleteConsumedProduct(token,id)
                                   }, token,navController)
                               }


                               if (consumedDayData.value.data?.sanitized_data!!.size - 1 != consumedDayData.value.data?.sanitized_data?.indexOf(item)) {

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


                           if (!loading && consumedDayData.value.data?.sanitized_data?.isEmpty() == true) {
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
           if(mWDSelectedIndex == 1) {


// week data resources
               LaunchedEffect(Unit) {
                   consumedVM.uiEvent.collect { event ->
                       when (event) {
                           is UiEvent.Loading -> {
                               if (event.action == Action.GetConsumedWeekData) {
                                   error = ""
                                   loading = true
                               }
                           }

                           is UiEvent.Error -> {
                               if (event.action == Action.GetConsumedWeekData) {
                                   loading = false
                                   error = "Oops! Something went wrong"
                               }
                           }

                           is UiEvent.Success -> {
                               if (event.action == Action.GetConsumedWeekData) {
                                   loading = false
                                   error = ""

                                   // Trigger day-level data load after successful week fetch
                               }
                           }

                           UiEvent.Idle -> Unit
                       }
                   }
               }


//               LaunchedEffect(key1 = consumedWeekData.value) {
//
//                   when (consumedWeekData.value) {
//
//                       is Resource.Error -> {
//                           loading = false
//                           error = "oops! something went wrong"
//                       }
//
//                       is Resource.Loading -> {
//
//                           error = ""
//                           loading = true
//                       }
//
//                       is Resource.Nothing -> {
//
//                           error = ""
//                           loading = false
//
//                       }
//
//                       is Resource.Success -> {
//
//                           error = ""
//                           loading = false
//
//                           if (token != null) {
//                               consumedVM.getConsumedDayData(
//                                   token,
//                                   dates[daysSelectedIndex]
//                               )
//                           }
//                       }
//                   }
//
//               }


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

                           if (consumedWeekData.value.data?.weekDatacondition?.hasPreviousWeek == true) {

                               Button(
                                   onClick = {

                                       val previousWeek = consumedWeekData.value.data?.weekDatacondition?.previousWeek

                                       if (token != null && !previousWeek.isNullOrBlank()) {
                                           consumedVM.getConsumedWeekData(token, previousWeek)
                                           selectedWeek = previousWeek
                                       }

                                       updateDayIndexOnWeekLoad = 6

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
                               text = consumedWeekData.value.data?.date_range ?: "Start - End Date",
                               fontSize = 16.sp,
                               color = HeetoxDarkGray,
                               modifier = Modifier
                           )

                       }




                       Column(
                           modifier = Modifier
                               .width(80.dp)
                       ) {

                           if (consumedWeekData.value.data?.weekDatacondition?.hasNextWeek == true) {

                               Button(
                                   onClick = {

                                       val nextWeek = consumedWeekData.value.data?.weekDatacondition?.nextWeek

                                       if (token != null && !nextWeek.isNullOrBlank()) {
                                           consumedVM.getConsumedWeekData(token, nextWeek)
                                           selectedWeek = nextWeek
                                       }

                                       updateDayIndexOnWeekLoad = 0

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
                           val weekData = consumedWeekData.value.data?.totalproductconsumed


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
                                           calcium = consumedWeekData.value.data!!.totalNutritionalValue.calcium,
                                           cholesterol = consumedWeekData.value.data!!.totalNutritionalValue.cholestrol,
                                           dietaryFibre = consumedWeekData.value.data!!.totalNutritionalValue.dietry_fibre,
                                           energy = consumedWeekData.value.data!!.totalNutritionalValue.energy,
                                           iron = consumedWeekData.value.data!!.totalNutritionalValue.iron,
                                           magnesium = consumedWeekData.value.data!!.totalNutritionalValue.magnessium,
                                           phosphorous = consumedWeekData.value.data!!.totalNutritionalValue.phosphorous,
                                           potassium = consumedWeekData.value.data!!.totalNutritionalValue.potassium,
                                           protein = consumedWeekData.value.data!!.totalNutritionalValue.protein,
                                           saturatedFats = consumedWeekData.value.data!!.totalNutritionalValue.saturates_fats,
                                           sodium = consumedWeekData.value.data!!.totalNutritionalValue.sodium,
                                           totalCarbohydrates = consumedWeekData.value.data!!.totalNutritionalValue.total_carbohydrates,
                                           totalFats = consumedWeekData.value.data!!.totalNutritionalValue.total_fats,
                                           totalSugar = consumedWeekData.value.data!!.totalNutritionalValue.total_sugar,
                                           transFats = consumedWeekData.value.data!!.totalNutritionalValue.trans_fats,
                                           unsaturatedFats = consumedWeekData.value.data!!.totalNutritionalValue.saturates_fats,
                                           vitaminA = consumedWeekData.value.data!!.totalNutritionalValue.vitamin_A,
                                           vitaminB = consumedWeekData.value.data!!.totalNutritionalValue.vitamin_B,
                                           vitaminC = consumedWeekData.value.data!!.totalNutritionalValue.vitamin_C,
                                           vitaminD = consumedWeekData.value.data!!.totalNutritionalValue.vitamin_D,
                                           vitaminE = consumedWeekData.value.data!!.totalNutritionalValue.vitamin_E,
                                           zinc = consumedWeekData.value.data!!.totalNutritionalValue.zinc
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


                                   consumedWeekData.value.data!!.weekData.forEach {

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
                                                   deleteProduct = {
                                                       token,id -> consumedVM.deleteConsumedProduct(token,id)
                                                   },
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

           if(mWDSelectedIndex == 0){



               //get Month Data
               LaunchedEffect(key1 = Unit) {

                   if (token != null) {
                       consumedVM.getConsumedMonthData(token,"currentmonth")
                   }


               }


               LaunchedEffect(Unit) {
                   consumedVM.uiEvent.collect { event ->
                       when (event) {
                           is UiEvent.Loading -> {
                               if (event.action == Action.GetConsumedMonthData) {
                                   loading = true
                                   error = ""
                               }
                           }

                           is UiEvent.Error -> {
                               if (event.action == Action.GetConsumedMonthData) {
                                   loading = false
                                   error = "Oops! Something went wrong"
                               }
                           }

                           is UiEvent.Success -> {
                               if (event.action == Action.GetConsumedMonthData) {
                                   loading = false
                                   error = ""
                               }
                           }

                           UiEvent.Idle -> Unit
                       }
                   }
               }


               // Month data resources
//               LaunchedEffect(key1 = consumedMonthData.value) {
//
//                   when (consumedMonthData.value) {
//
//                       is Resource.Error -> {
//                           loading = false
//                           error = "oops! something went wrong"
//                       }
//
//                       is Resource.Loading -> {
//
//                           error = ""
//                           loading = true
//                       }
//
//                       is Resource.Nothing -> {
//
//                           error = ""
//                           loading = false
//
//                       }
//
//                       is Resource.Success -> {
//
//                           error = ""
//                           loading = false
//
//                       }
//                   }
//
//               }


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

                           if (consumedMonthData.value.data?.month_condition?.hasPreviousMonth == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           consumedVM.getConsumedMonthData(
                                               token,
                                               consumedMonthData.value.data?.month_condition!!.previousMonth
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
                               text = consumedMonthData.value.data?.month_range
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

                           if (consumedMonthData.value.data?.month_condition?.hasNextMonth == true) {

                               Button(
                                   onClick = {

                                       if (token != null) {
                                           consumedVM.getConsumedMonthData(
                                               token,
                                               consumedMonthData.value.data!!.month_condition.nextMonth
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

                       if(consumedMonthData.value.data?.totalproductconsumed != null && consumedMonthData.value.data?.totalproductconsumed!! > 0 ){


                           Text(

                               text = "Total Products Consumed (${consumedMonthData.value.data!!.totalproductconsumed})",
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

                               consumedMonthData.value.data?.totalNutritionalValue?.let {
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


    LaunchedEffect(Unit) {
        consumedVM.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Loading -> {
                    if (event.action == Action.DeleteConsumedProduct) {
                        loading = true
                        error = ""
                    }
                }

                is UiEvent.Error -> {
                    if (event.action == Action.DeleteConsumedProduct) {
                        loading = false
                        error = "Oops! Something went wrong"
                    }
                }

                is UiEvent.Success -> {
                    if (event.action == Action.DeleteConsumedProduct) {
                        loading = false
                        error = ""

                        if (token != null) {
                            consumedVM.getConsumedWeekData(token, selectedWeek)
                            // Optional: Trigger day data refresh if needed
                             consumedVM.getConsumedDayData(token, dates[daysSelectedIndex])
                        }
                    }
                }

                UiEvent.Idle -> Unit
            }
        }
    }

//    LaunchedEffect(key1 = deleteConsumeData.value) {
//
//        when (deleteConsumeData.value) {
//
//            is Resource.Error -> {
//                loading = false
//                error = "oops! something went wrong"
//            }
//
//            is Resource.Loading -> {
//
//                error = ""
//                loading = true
//            }
//
//            is Resource.Nothing -> {
//                error = ""
//                loading = false
//            }
//
//            is Resource.Success -> {
//
//                error = ""
//                loading = false
//
//                if (token != null) {
//
//                    consumedVM.getConsumedWeekData(
//                        token,
//                        selectedWeek
//                    )
//
////                    ProductVM.getConsumedDayData(
////                        token,
////                        Dates[DaysselectedIndex]
////                    )
//
//
//                }
//
//
//            }
//
//        }
//
//
//    }

}










@Composable
fun ConsumedItem(data : ProductsData,deleteProduct:(String,String)->Unit,token : String,navController: NavHostController){

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

                        deleteProduct(token, data._id)

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





