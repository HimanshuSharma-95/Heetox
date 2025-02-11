package com.heetox.app.Model.Product

data class ConsumedMonthDataResponse(
    val month_condition: MonthCondition,
    val month_range: String,
    val totalNutritionalValue: DayNutritionalValue,
    val totalproductconsumed: Int
)