package com.heetox.app.Model.Product

data class ConsumeProductResponse(
    val __v: Int,
    val _id: String,
    val consumed_At_date: String,
    val consumed_At_time: String,
    val consumed_By: String,
    val createdAt: String,
    val product_id: String,
    val serving_size: Any,
    val updatedAt: String
)