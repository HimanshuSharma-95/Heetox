package com.heetox.app.Model.Product

data class MonthCondition(
    val currentMonth: String,
    val hasNextMonth: Boolean,
    val hasPreviousMonth: Boolean,
    val nextMonth: String,
    val previousMonth: String
)