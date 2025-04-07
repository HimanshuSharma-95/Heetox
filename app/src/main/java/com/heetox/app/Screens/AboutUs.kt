package com.heetox.app.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heetox.app.R
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite



@Composable
fun AboutUs(){




    Column(
        modifier = Modifier
            .background(HeetoxWhite)
            .fillMaxSize()
            .padding(10.dp,0.dp)
            .verticalScroll(rememberScrollState())
    ){

        Spacer(modifier = Modifier.height(20.dp))


       Column(
           modifier = Modifier
               .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
               .background(HeetoxGreen)
               .padding(20.dp)
               .fillMaxWidth()

       ){
           Text(
               "Our Motive Behind Building Heetox",
               color = Color.Black,
               fontWeight = FontWeight.W700,
               fontSize = 22.sp,
           )
       }


       Column(modifier = Modifier
           .background(Color.White)
           .padding(10.dp)
           .fillMaxHeight()
       ){

           Spacer(modifier = Modifier.height(20.dp))

           Text(
               "In today's fast-paced world, health often takes a back seat. People are increasingly consuming packaged foods without fully understanding the nutritional value and potential health impacts. Despite the availability of nutritional labels, many consumers find it challenging to decode the information, leading to unhealthy dietary choices. Heetox was born from the realization that there’s a gap between awareness and action when it comes to making informed food choices.\n" +
                       "\n" +
                       "Our motive is to empower individuals to take control of their health through informed eating habits. We aim to bridge the gap between nutritional knowledge and its practical application in daily life. Heetox is not just another app; it’s a companion for a healthier lifestyle, designed to simplify the complexities of nutritional science and make healthy eating accessible to everyone. By offering personalized health alerts, Heetox helps users identify potentially harmful ingredients in products that could aggravate existing health conditions. It suggests healthier alternatives, enabling users to make better choices effortlessly. Additionally, the app tracks consumption patterns and provides insightful reports on daily, weekly, and monthly packaged food intake. For those aiming to monitor their calorie and nutrient consumption, Heetox acts as a dedicated tracker, guiding users toward balanced eating habits.\n" +
                       "\n" +
                       "This is just the beginning—Heetox is currently in its first version, and we have an ambitious roadmap ahead. We envision adding more features like real-time meal recommendations, collaborative insights with fitness and health professionals, and deeper integration with fitness apps to create a holistic health ecosystem. Your feedback and suggestions are invaluable to us as we strive to improve and expand Heetox. Together, we can make healthy living more attainable and enjoyable for everyone.",
               color = HeetoxDarkGray,
               fontWeight = FontWeight.W500,
               fontSize = 16.sp,
           )

           Spacer(modifier = Modifier.height(20.dp))


           Text(
               "Best Regards Team,",
               color = HeetoxDarkGray,
               fontWeight = FontWeight.W700,
               fontSize = 18.sp,
           )

           Column(
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally,

               modifier = Modifier
                   .fillMaxWidth()
           ){

               Image(
                   painter = painterResource(id = R.drawable.heetoxlogobg),
                   contentDescription = "Profile Icon",
                   modifier = Modifier
                       .size(200.dp)

                   )

           }





       }


    }


}