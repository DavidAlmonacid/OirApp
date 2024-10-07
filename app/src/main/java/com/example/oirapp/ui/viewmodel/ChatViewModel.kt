package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.Message
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.HasOldRecord
import io.github.jan.supabase.realtime.HasRecord
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ChatViewModel  : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val table = supabaseClient.postgrest["grupos"]

    fun subscribeToChannel(channelName: String) {
        viewModelScope.launch {
            try {
                val channel = supabaseClient.channel(channelName)

                val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "mensajes"
                }

                changes
                    .onEach {
                        when (it) { //You can also check for <is PostgresAction.Insert>, etc.. manually
                            //is PostgresAction.Insert -> getMessages()
                            is HasRecord -> println(it.record)
                            is HasOldRecord -> println(it.oldRecord)
                            else -> println(it)
                        }
                    }
                    .launchIn(this)

                channel.subscribe()
            } catch (e: Exception) {
                println("ChatViewModel.subscribeToChannel: Error: ${e.message}")
            }
        }
    }

    @OptIn(SupabaseExperimental::class)
    fun getMessages() {
        viewModelScope.launch {
            try {
                val flow: Flow<List<Message>> = table.selectAsFlow(Message::id)

                flow.onEach {
                    _messages.value = it
                }.launchIn(this)

//                flow.collect {
//                    for (message in it) {
//                        println(message)
//                    }
//                }
            } catch (e: Exception) {
                println("ChatViewModel.getMessages: Error: ${e.message}")
            }
        }
    }

    fun insertMessage(message: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    table.insert(buildJsonObject {
                        put("mensaje", message)
                    })
                }
            } catch (e: Exception) {
                println("ChatViewModel.insertMessage: Error: ${e.message}")
            }
        }
    }
}
