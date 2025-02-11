package com.heetox.app.Model.Product

data class ConsumedDayDataResponse(
    val sanitized_data: List<ProductsData>,
    val totalNutritionalValue : DayNutritionalValue
)