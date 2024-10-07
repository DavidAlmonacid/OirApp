package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.oirapp.data.network.Message
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.state.MessageUiState
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.HasOldRecord
import io.github.jan.supabase.realtime.HasRecord
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.json.buildJsonObject

class ChatViewModel  : ViewModel() {
    private val _messages = MutableStateFlow<List<MessageUiState>>(emptyList())
    val messages: StateFlow<List<MessageUiState>> = _messages.asStateFlow()
    val table = supabaseClient.postgrest["grupos"]
    suspend fun subscribeToChannel(channelName: String, coroutineScope: CoroutineScope) {
        val channel = supabaseClient.channel(channelName)

        val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "mensajes"
        }

        changes
            .onEach {
                when(it) { //You can also check for <is PostgresAction.Insert>, etc.. manually
                    //is PostgresAction.Insert -> getMessages()
                    is HasRecord -> println(it.record)
                    is HasOldRecord -> println(it.oldRecord)
                    else -> println(it)
                }
            }
            .launchIn(coroutineScope)

        channel.subscribe()
    }

    @OptIn(SupabaseExperimental::class)
    suspend fun getMessages() {
        val flow: Flow<List<Message>> = table.selectAsFlow(Message::id)

        flow.collect {
            for (message in it) {
                println(message)
            }
        }
    }

    suspend fun insertMessage(message: String) {
        table.insert(buildJsonObject {
            put("mensaje", message)
        })
    }
}