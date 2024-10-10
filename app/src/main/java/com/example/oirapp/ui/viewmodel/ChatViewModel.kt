package com.example.oirapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel  : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    var userMessage by mutableStateOf("")
        private set

    fun updateUserMessage(message: String) {
        userMessage = message
    }

    fun resetData() {
        userMessage = ""
    }

    private val table = supabaseClient.postgrest["mensajes"]

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
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val currentDate = dateFormat.format(Date(System.currentTimeMillis()))

                    table.insert(buildJsonObject {
                        put("mensaje", message)
                        put("fecha_envio", currentDate)
                        //put("id_grupo", ) // TODO: Add group id
                    })
                }
            } catch (e: Exception) {
                println("ChatViewModel.insertMessage: Error: ${e.message}")
            }
        }
    }
}
