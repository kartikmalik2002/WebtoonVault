package com.example.webtoonvault.data.models

data class Webtoon(

    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val averageRating: Float,
    val ratingCount: Int,
    val isFavourite: Boolean,
    val totalRatingValue: Int
){
    constructor() : this(
        "", "", "", "", 0f, 0, false, 0
    )
}