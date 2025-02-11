package com.heetox.app.Model.Product

data class SubCategoriesResponse(
    val main_category: String,
    val subcategories: List<String>,
    val subcategories_count: Int
)