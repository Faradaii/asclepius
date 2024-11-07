package com.dicoding.asclepius.network

data class Article(
    val title: String,
    val description: String,
    val urlToImage: String?,
    val publishedAt: String,
    val url: String,
)