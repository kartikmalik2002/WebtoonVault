package com.example.webtoonvault.data.models

data class User(
    val userId: String,
    val webtoonToRating: HashMap<String, Int>
){
    constructor() : this(
        "", hashMapOf()
    )
}