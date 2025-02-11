package com.heetox.app.Model.Product

data class NutritionalValue(
    val carbohydrates: Carbohydrates,
    val cholestrol: Float,
    val energy: Float,
    val fats: Fats,
    val micro_nutrients: MicroNutrients,
    val protein: Float,
    val sodium:Float,
    val total_carbohydrates: Float,
    val total_fats: Float,
    val serving_size : Int,
    val total_weight: Int
)