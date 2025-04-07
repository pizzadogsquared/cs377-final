package com.ebs233.finalproject.model

import com.ebs233.finalproject.model.Card


data class DrawResponse(
    val success: Boolean,
    val cards: List<Card>,
    val deck_id: String,
    val remaining: Int
)
