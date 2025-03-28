package com.heetox.app.Model.Product


data class Ingredient(
    val __v: Int,
    val description: String,
    val ingredients: List<Any>,
    val keywords: List<String>,
    val name: String,
    val purpose: String
)