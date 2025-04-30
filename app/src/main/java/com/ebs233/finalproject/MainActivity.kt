package com.ebs233.finalproject

import com.ebs233.finalproject.model.ShuffleResponse
import com.ebs233.finalproject.model.DrawResponse
import com.ebs233.finalproject.model.Card
import com.ebs233.finalproject.network.DeckApi

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private lateinit var shuffleButton: Button
    private lateinit var drawButton: Button
    //private lateinit var cardImage: ImageView
    private lateinit var cardContainer: FrameLayout

    var cardCounter = 0

    // Retrofit API service
    private lateinit var deckApi: DeckApi
    // Hold the current deck id after shuffling
    private var currentDeckId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shuffleButton = findViewById(R.id.shuffleButton)
        drawButton = findViewById(R.id.drawButton)
        //cardImage  = findViewById(R.id.cardImage)
        cardContainer  = findViewById(R.id.cardContainer)


        // Set up Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://deckofcardsapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        deckApi = retrofit.create(DeckApi::class.java)
        shuffleButton.setOnClickListener { shuffleDeck() }
        drawButton.setOnClickListener { drawCard() }
    }

    private fun shuffleDeck() {
        deckApi.shuffleDeck(1).enqueue(object : Callback<ShuffleResponse> {
            override fun onResponse(call: Call<ShuffleResponse>, response: Response<ShuffleResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { shuffleResponse ->
                        currentDeckId = shuffleResponse.deck_id
                        Toast.makeText(this@MainActivity, "Deck shuffled!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to shuffle deck", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ShuffleResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun drawCard() {
        val deckId = currentDeckId
        if (deckId == null) {
            Toast.makeText(this, "Please shuffle the deck first!", Toast.LENGTH_SHORT).show()
            return
        }
        deckApi.drawCard(deckId, 1).enqueue(object : Callback<DrawResponse> {
            override fun onResponse(call: Call<DrawResponse>, response: Response<DrawResponse>) {
                // create new image view for each draw
                val imageView = ImageView(this@MainActivity)
                imageView.layoutParams= LinearLayout.LayoutParams(400, 400)
                imageView.rotation = Random.nextFloat() * 100 + -50
                imageView.x = Random.nextFloat() * 200 + 200 // slight random X
                imageView.y = Random.nextFloat() * 200 + 200  // slight random Y

                if (response.isSuccessful) {
                    response.body()?.cards?.firstOrNull()?.let { card ->
                        Glide.with(this@MainActivity)
                        .load(card.image)
                        .into(imageView)
                        cardContainer.addView(imageView)
                    } ?: Toast.makeText(this@MainActivity, "No card drawn", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to draw card", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DrawResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}