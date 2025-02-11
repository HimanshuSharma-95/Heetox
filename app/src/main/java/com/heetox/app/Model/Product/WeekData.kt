package com.heetox.app.Model.Product

data class WeekData(
    val date: String,
    val products: List<ProductsData>,
    val per_day_NutritionalValue : WeekPerDayNutriton
)