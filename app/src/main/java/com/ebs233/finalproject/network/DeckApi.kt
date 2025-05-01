package com.ebs233.finalproject.network

import com.ebs233.finalproject.model.ShuffleResponse
import com.ebs233.finalproject.model.DrawResponse

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call

interface DeckApi {
    @GET("api/deck/new/shuffle/")
    fun shuffleDeck(@Query("deck_count") deckCount: Int = 1): Call<ShuffleResponse>

    @GET("api/deck/{deckId}/draw/")
    fun drawCard(@Path("deckId") deckId: String, @Query("count") count: Int = 1): Call<DrawResponse>
}