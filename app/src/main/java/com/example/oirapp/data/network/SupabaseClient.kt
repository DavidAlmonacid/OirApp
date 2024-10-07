package com.example.oirapp.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import com.example.oirapp.BuildConfig
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    val supabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_KEY,
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
    }
}
