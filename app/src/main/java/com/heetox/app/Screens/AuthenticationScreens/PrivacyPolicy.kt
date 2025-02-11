package com.heetox.app.Screens.AuthenticationScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heetox.app.ui.theme.HeetoxDarkGray
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun PrivacyPolicy(){

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
                "Privacy Policy",
                color = Color.Black,
                fontWeight = FontWeight.W700,
                fontSize = 23.sp,
            )
        }


        Column(modifier = Modifier
            .background(Color.White)
            .padding(10.dp)
            .fillMaxHeight()
        ){

            Spacer(modifier = Modifier.height(20.dp))

            Text(
            "Effective Date: 3-01-2025\n" +
                    "Last Updated: 3-01-2025\n\n" +
                    "Welcome to Heetox! Your privacy is important to us, and we are committed to protecting the personal information you share with us. This Privacy Policy outlines how Heetox collects, uses, stores, and protects your data.\n\n" +
                        "1. Information We Collect\n\n" +
                        "a. Account Information\n" +
                        "To create an account, we collect the following information:\n" +
                        "Name\n" +
                        "Age\n" +
                        "Gender"+
                        "Phone number\n" +
                        "Email address\n" +
                        "Password\n\n" +
                        "b. Optional Profile Information\n" +
                        "You may upload a profile picture, which will be securely stored on our servers.\n\n" +
                        "c. Usage Without Account\n" +
                        "You can scan products to view their nutritional score without creating an account. However, features like tracking your consumption history , liking Product require an account.\n\n" +
                        "d. Permissions\n" +
                        "The app requests the following permissions:\n" +
                        "Camera Access: To scan barcodes for nutritional information.\n" +
                        "Gallery Access: To upload a profile picture.\n" +
                        "Internet Access: To communicate with servers for account creation, calorie tracking, consumption history, and barcode data retrieval and Product Updates like Liking Product.\n\n" +
                        "2. How We Use Your Information\n" +
                        "We use the data you provide for the following purposes:\n" +
                        "To create and maintain your account.\n" +
                        "To allow you to track and view your calorie consumption and history for up to four months.\n" +
                        "To display your profile information.\n\n" +
                        "3. Data Storage and Security\n" +
                        "Your data is securely stored on our servers.\n" +
                        "We employ encryption and other industry-standard measures to protect your information.\n\n" +
                        "4. Data Sharing\n" +
                        "We do not collect usage data or share any of your information with third parties.\n\n" +
                        "5. Your Rights\n" +
                        "You have the following rights:\n" +
                        "Access and Correction: You can view and update your account information within the app.\n" +
                        "Data Deletion: If you wish to delete your account and associated data, please contact us at heetoxofficial@gmail.com.\n\n" +
                        "6. Permissions and Their Use\n" +
                        "The app requests the following permissions:\n" +
                        "Camera Access: For barcode scanning.\n" +
                        "Gallery Access: For uploading a profile picture.\n" +
                        "Both permissions are optional, but denying them will limit certain app functionalities.\n\n" +
                        "7. Minors\n" +
                        "Heetox is intended for users of all ages. However, we recommend parental supervision for minors under 13 when creating accounts or using the app.\n\n" +
                        "8. No Ads and Free Usage\n" +
                        "Heetox is completely free and does not display ads.\n\n" +
                        "9. Contact Us\n" +
                        "If you have any questions or concerns about this Privacy Policy, please contact us at:\n" +
                        "Email: heetoxofficial@gmail.com\n\n" +
                        "10. Changes to This Privacy Policy\n" +
                        "We may update this Privacy Policy from time to time. Changes will be posted within the app, and the effective date will be revised.\n",
                color = HeetoxDarkGray,
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
            )



        }


    }

}