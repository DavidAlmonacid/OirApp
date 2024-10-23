package com.example.oirapp.data.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val client = HttpClient(OkHttp) {
//    install(ContentNegotiation) {
//        json(
//            json = Json {
//                ignoreUnknownKeys = true
//            }
//        )
//    }
}
