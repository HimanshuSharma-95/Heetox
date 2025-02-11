package com.heetox.app.Model.Product

data class ProductsData(
    val _id: String,
    val consumed_At_date: String,
    val consumed_At_time: String,
    val consumed_By: String,
    val consumed_products: ConsumedProductX,
    val serving_size: Double
)