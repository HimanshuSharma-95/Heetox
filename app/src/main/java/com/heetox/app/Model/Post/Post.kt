package com.heetox.app.Model.Post

data class Post(
    val __v: Int,
    val _id: String,
    val author: String,
    val content: String,
    val createdAt: String,
    val created_At_date: String,
    val created_At_time: String,
    val featured_image: String,
    val isbookmarked: Boolean,
    val isliked: Boolean,
    val likesCount: Int,
    val tags: List<String>,
    val title: String,
    val updatedAt: String
)