package com.watchtower.networking

import com.watchtower.storage.model.JokeApiModel
import retrofit.Call
import retrofit.http.GET

// API Interface for Retrofit
interface APIEndpointInterface {

    @GET("jokes/random")
    fun getRandomJoke() : Call<JokeApiModel>
}