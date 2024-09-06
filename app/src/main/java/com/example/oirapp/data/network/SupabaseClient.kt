package com.example.oirapp.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://lydqdbpjgddgkzcdooqe.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx5ZHFkYnBqZ2RkZ2t6Y2Rvb3FlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjU1NTE3NDIsImV4cCI6MjA0MTEyNzc0Mn0.SrJ_Ln3HB6o_EXdKp26raJrdhh8W6WdDuW6UZHJ8PUg",
    ) {
        install(Postgrest)
    }
}