package com.heetox.app.Composables.ProductCompose

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.heetox.app.Model.Product.ProductbyBarcodeResponse
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxWhite


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumeProductCard(
    Details: ProductbyBarcodeResponse,
    ProductVM: ProductsViewModel,
    token: String?,
    navcontroller : NavHostController
) {



    var error by rememberSaveable {
        mutableStateOf("")
    }

    var Loading by rememberSaveable {
        mutableStateOf(false)
    }

    var ConsumeProductData = ProductVM.ConsumeProductData.collectAsState()


    var step by rememberSaveable { mutableStateOf(1) }

    val totalSize = Details.product_data.nutritional_value.total_weight
    val servingSize = Details.product_data.nutritional_value.serving_size

    val totalServings = totalSize / servingSize

    var sliderPosition by rememberSaveable { mutableStateOf(0f) } // Initial position at 0

    val maxValue = totalSize.toFloat()

    var consumedServing by rememberSaveable {
        mutableStateOf(0f)
    }

    var consumedInGrams by rememberSaveable {
        mutableStateOf("0")
    }

    val context = LocalContext.current

    var selectedNumber by rememberSaveable { mutableStateOf(0.0) }


    LaunchedEffect(key1 = ConsumeProductData.value) {

        when(ConsumeProductData.value){

            is Resource.Error -> {
                error = "oops! Couldn't Add Product"
                Loading = false

                Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show()

            }

            is Resource.Loading -> {
                error = " Just a second ;) "
                Loading = true
            }

            is Resource.Nothing -> {

            }

            is Resource.Success -> {
                step = 2
                Loading = false
                error = ""
            }

        }
    }


    LaunchedEffect(key1 = Unit) {
        step = 1
        sliderPosition = 0f
        consumedServing = 0f
        consumedInGrams = "0"
    }


    if(!Loading){

        if(step == 2){


            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .width(400.dp)
                    .height(500.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                
                
               Image(painter = painterResource(id = R.drawable.consumed), contentDescription = "tick",
                   modifier = Modifier.size(150.dp)
                   )

                Text(text = "Product Consumed!", color = Color.Black,
                    fontSize = 20.sp
                    )

                Spacer(modifier = Modifier.height(40.dp))

                Button(onClick = {

                                 navcontroller.navigate("consumed")

                },

                    colors = ButtonDefaults.buttonColors(
                        containerColor = HeetoxDarkGreen,
                        contentColor = Color.White
                    )

                    ) {

                    Text(text = "View Consumption", color = Color.White)

                }

            }
            
            
            

        }
        else if(step == 1) {

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .width(400.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(15.dp))

                Row {

                    if (totalSize > 0) {

                        Spacer(modifier = Modifier.height(5.dp))
                        Row {

                            Text(
                                text = "Total Size",
                                color = HeetoxDarkGray,
                                fontSize = 15.sp
                            )
                            Text(
                                text = " : $totalSize g",
                                color = HeetoxDarkGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(15.dp))

                    if (servingSize > 0) {
                        Spacer(modifier = Modifier.height(5.dp))

                        Row {
                            Text(
                                text = "Serving Size ",
                                color = HeetoxDarkGray,
                                fontSize = 15.sp
                            )
                            Text(
                                text = " : $servingSize g",
                                color = HeetoxDarkGray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Add to Consumption",
                    color = HeetoxDarkGreen,
                    fontSize = 17.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Select Any",
                    color = HeetoxDarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    var expanded by remember { mutableStateOf(false) }

                    val numbers = (1..totalServings).toList()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(HeetoxWhite),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedNumber.toString().take(4),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Servings Consumed", color = Color.Black) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .width(300.dp)
                                    .menuAnchor() // Required to anchor the menu correctly
                                    .clip(RoundedCornerShape(10.dp)),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = HeetoxWhite,
                                    unfocusedContainerColor = HeetoxWhite,
                                    focusedIndicatorColor = HeetoxWhite,
                                    unfocusedIndicatorColor = HeetoxWhite,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Black
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                numbers.forEach { number ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedNumber = number.toDouble()
                                            expanded = false
                                            consumedServing = ((selectedNumber * servingSize).toFloat())
                                            sliderPosition = consumedServing.toFloat()
                                            consumedInGrams = (selectedNumber * servingSize).toString()
                                        },
                                        text = { Text(number.toString()) }
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = "or",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color.Black
                    )




                    // Slider
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(HeetoxWhite)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = "${sliderPosition.toString().take(5)} g")

                        Slider(
                            value = sliderPosition,
                            onValueChange = {
                                sliderPosition = it
                                consumedServing = sliderPosition.toFloat()
                                selectedNumber = (sliderPosition / servingSize).toDouble()
                                consumedInGrams = sliderPosition.toString().take(5)
                            },
                            valueRange = 0f..maxValue,
                            steps = 100, // Adjust this based on how granular you want the slider to be
                            modifier = Modifier.padding(horizontal = 16.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = HeetoxDarkGreen,
                                activeTrackColor = HeetoxDarkGreen,
                                inactiveTrackColor = HeetoxDarkGreen.copy(alpha = 0.3f) // Slightly lighter shade for the inactive track
                            )
                        )
                    }

                    Text(
                        text = "or",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color.Black
                    )

                    // TextField for grams input

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(HeetoxWhite),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        TextField(
                            value = consumedInGrams,
                            onValueChange = { input ->
                                val parsedValue = input.toFloatOrNull()
                                if (parsedValue != null) {
                                    consumedInGrams = input
                                    consumedServing = parsedValue
                                    sliderPosition = parsedValue
                                    selectedNumber = (parsedValue / servingSize).toDouble()
                                } else {
                                    // Handle empty or invalid input
                                    consumedInGrams = ""
                                    consumedServing = 0f
                                    sliderPosition = 0f
                                    selectedNumber = 0.0
                                }
                            },
                            label = { Text("Consumed in grams", color = Color.Black) },
                            modifier = Modifier
                                .width(200.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = HeetoxWhite,
                                unfocusedContainerColor = HeetoxWhite,
                                focusedIndicatorColor = HeetoxWhite,
                                unfocusedIndicatorColor = HeetoxWhite,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor = Color.Black
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number // Set keyboard type to numbers only
                            )
                        )
                    }


                    Spacer(modifier = Modifier.height(28.dp))

                    Button(onClick = {

                        if(consumedServing.toDouble() == 0.0){
                            Toast.makeText(context,"Add Some Quantity", Toast.LENGTH_SHORT).show()
                        }else{

                            if (token != null) {
                                ProductVM.consumeproduct(token = token, barcode = Details.product_data.product_barcode, size = consumedServing)
                            }

                        }

                    },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = HeetoxDarkGreen,
                        ),
                        modifier = Modifier.width(200.dp)
                    ) {

                        Text(text = "Add  ${consumedServing.toString().take(5)} g",
                            color = Color.White
                        )


                    }

                    Spacer(modifier = Modifier.height(15.dp))


                }
            }

        }

    }else{

        Column(
            modifier = Modifier
                .padding(10.dp)
                .width(400.dp)
                .height(500.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            CircularProgressIndicator(
                color = HeetoxDarkGreen,
                modifier = Modifier.size(40.dp)
            )

        }



    }





    }

