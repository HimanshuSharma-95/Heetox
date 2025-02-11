package com.heetox.app.Model.Product

data class ConsumedWeekDataResponse(
    val totalNutritionalValue: DayNutritionalValue,
    val totalproductconsumed: Int,
    val weekData: List<WeekData>,
    val weekDatacondition: WeekDatacondition,
    val date_range : String,
)