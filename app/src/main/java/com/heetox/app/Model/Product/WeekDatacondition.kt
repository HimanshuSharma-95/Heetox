package com.heetox.app.Model.Product

data class WeekDatacondition(
    val currentWeek: String,
    val hasNextWeek: Boolean,
    val hasPreviousWeek: Boolean,
    val nextWeek: String,
    val previousWeek: String
)