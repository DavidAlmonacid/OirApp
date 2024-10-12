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
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
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
import java.util.TimeZone

class ChatViewModel  : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    var userMessage by mutableStateOf("")
        private set

    fun updateUserMessage(message: String) {
        userMessage = message
    }

    private fun resetData() {
        userMessage = ""
    }

    private val table = supabaseClient.postgrest["mensajes"]

    fun subscribeToChannel(channelName: String, groupId: Int) {
        viewModelScope.launch {
            try {
                val channel = supabaseClient.channel(channelName)

                val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "mensajes"
                }

                changes
                    .onEach {
                        if (it is PostgresAction.Insert) {
                            getMessages(groupId)
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
    fun getMessages(groupId: Int) {
        viewModelScope.launch {
            try {
                val flow: Flow<List<Message>> = table.selectAsFlow(
                    primaryKey = Message::id,
                    filter = FilterOperation("id_grupo", FilterOperator.EQ, groupId),
                )

                flow.collect { _messages.value = it }
            } catch (e: Exception) {
                println("ChatViewModel.getMessages: Error: ${e.message}")
            }
        }
    }

    fun insertMessage(message: String, groupId: Int, userId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("America/Bogota")
                    val currentDate = dateFormat.format(Date(System.currentTimeMillis()))

                    table.insert(buildJsonObject {
                        put("mensaje", message)
                        put("fecha_envio", currentDate)
                        put("id_grupo", groupId)
                        put("id_usuario", userId)
                    })
                }

                resetData()
            } catch (e: Exception) {
                println("ChatViewModel.insertMessage: Error: ${e.message}")
            }
        }
    }

    fun removeChannel(channelName: String) {
        viewModelScope.launch {
            try {
                _messages.value = emptyList()

                val channel = supabaseClient.channel(channelName)
                supabaseClient.realtime.removeChannel(channel)
            } catch (e: Exception) {
                println("ChatViewModel.removeChannel: Error: ${e.message}")
            }
        }
    }
}
