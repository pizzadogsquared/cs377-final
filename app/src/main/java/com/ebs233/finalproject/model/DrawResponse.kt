package com.ebs233.finalproject.model

data class DrawResponse(
    val success: Boolean,
    val cards: List<Card>,
    val deckId: String,
    val remaining: Int
)
