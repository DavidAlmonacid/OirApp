package com.example.oirapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://pokeapi.co/api/v2"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface TranscriptApiService {
    @GET("pokemon/ditto")
    suspend fun getTranscriptMessage(): String
}

object TranscriptApi {
    val retrofitService: TranscriptApiService by lazy {
        retrofit.create(TranscriptApiService::class.java)
    }
}
