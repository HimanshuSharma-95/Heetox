package com.heetox.app.Composables.AuthCompose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.heetox.app.ui.theme.HeetoxDarkGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(label : String, value:String, onValueChange : (String)-> Unit, keyboardOptions: KeyboardOptions = KeyboardOptions.Default, visualTransformation: VisualTransformation = VisualTransformation.None){

    OutlinedTextField(
        value = value ,
        onValueChange = onValueChange ,
        label = { Text(text = label) },
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .width(300.dp)
        ,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedLabelColor = HeetoxDarkGray,
        )

    )


}

