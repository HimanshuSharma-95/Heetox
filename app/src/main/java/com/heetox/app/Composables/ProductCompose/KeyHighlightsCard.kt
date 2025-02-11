package com.heetox.app.Composables.ProductCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heetox.app.Model.Product.NutritionalValue
import com.heetox.app.ui.theme.HeetoxDarkGray


@Composable
fun Keyhighlights(data: MutableState<NutritionalValue>){

    val highValues = mutableListOf<String>()

    val moderateValues = mutableListOf<String>()





    // carbs - Total sugar
    if(data.value.carbohydrates.total_sugar >= 22.5){

        highValues.add("High sugar")

    }else if(data.value.carbohydrates.total_sugar > 5 && data.value.carbohydrates.total_sugar < 22.5){

        moderateValues.add("Moderate sugar")

    }





    //fats

    //total fats
    if(data.value.total_fats >= 17.5){

        highValues.add("High Total Fats")

    }else if(data.value.total_fats < 17.5 && data.value.total_fats > 3){

        moderateValues.add("Moderate Total Fats")

    }

    //saturated fat

    if(data.value.fats.saturates_fats > 5){

        highValues.add("High Saturated Fat")

    }else if(data.value.fats.saturates_fats > 1.5 && data.value.fats.saturates_fats <= 5){

        moderateValues.add("Moderate Saturated Fat")

    }

    //Trans fat
    if (data.value.fats.trans_fats > 0) {

        highValues.add("Includes Trans Fat")

    }





    //sodium
    if(data.value.sodium >= 1500) {

      highValues.add("High Sodium")



    }else if(data.value.sodium > 300 && data.value.sodium < 1500){

     moderateValues.add("Moderate Sodium")

    }



    //chalesotrol

    if(data.value.cholestrol > 0) {
        highValues.add("Includes Cholesterol")
    }





    Column(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .width(500.dp)
            .clip(RoundedCornerShape(10.dp))
//            .background(Color(0x1F23A247))
//            .padding(10.dp)

    ){

        Text(text = "Key Highlights",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = HeetoxDarkGray
            )


        Spacer(modifier = Modifier.height(10.dp))


        com.google.accompanist.flowlayout.FlowRow(
//            mainAxisSpacing = 4.dp,
        ) {

            if(highValues.size > 0){

                highValues.forEach {
                        item ->
                 Keyhighlightitem(item,Color(0xFFFFC1C1))
                }

            }


        }


        com.google.accompanist.flowlayout.FlowRow(
        ) {


            if(moderateValues.size > 0){

             moderateValues.forEach {
                        item ->
                    Keyhighlightitem(item,Color(0xFFFFF285))
                }

            }

        }


    }



}






@Composable
fun Keyhighlightitem(item : String,color : Color){


    Column(
        modifier = Modifier
            .padding(4.dp)
//            .border(2.dp, color, RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(10.dp)
    ){

        Text(text = item,
            fontSize = 14.sp,
            color = Color.Black,
            )

    }




}
