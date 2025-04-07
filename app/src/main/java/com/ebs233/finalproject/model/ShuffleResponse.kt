package com.ebs233.finalproject.model

data class ShuffleResponse(
    val success: Boolean,
    val deck_id: String,
    val shuffled: Boolean,
    val remaining: Int
)
