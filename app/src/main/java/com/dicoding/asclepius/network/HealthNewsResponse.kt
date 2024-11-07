package com.dicoding.asclepius.network

data class HealthNewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>,
)

