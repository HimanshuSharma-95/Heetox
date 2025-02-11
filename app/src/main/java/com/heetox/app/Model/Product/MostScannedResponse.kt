package com.heetox.app.Model.Product

data class MostScannedResponse(

    val _id: String,
    val brand_name: String,
    val fetchCount: Int,
    val likesCount: Int,
    val product_barcode: String,
    val product_category: String,
    val product_finalscore: Int,
    val product_front_image: String,
    val product_name: String,
    val product_nutriscore: String

)