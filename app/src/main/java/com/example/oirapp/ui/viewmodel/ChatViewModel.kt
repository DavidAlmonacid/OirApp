package com.example.oirapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oirapp.data.network.Message
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.data.network.client
import com.example.oirapp.utils.removeAccents
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.storage.FileObject
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

sealed interface TranscriptUiState {
    data class Success(val transcript: String) : TranscriptUiState
    data class Error(val message: String) : TranscriptUiState
    data object Loading : TranscriptUiState
}

sealed interface UploadState {
    data object Idle : UploadState
    data object Uploading : UploadState
    data class Success(val fileName: String) : UploadState
    data class Error(val message: String) : UploadState
}

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _transcriptUiState = MutableStateFlow<TranscriptUiState>(TranscriptUiState.Loading)
    val transcriptUiState: StateFlow<TranscriptUiState> = _transcriptUiState.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    var userMessage by mutableStateOf("")
        private set

    var channelName by mutableStateOf("")
        private set

    fun updateUserMessage(message: String) {
        userMessage = message
    }

    fun updateChannelName(name: String) {
        channelName = name
    }

    fun resetTranscriptUiState() {
        _transcriptUiState.value = TranscriptUiState.Loading
    }

    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }

    private fun resetMessage() {
        userMessage = ""
    }

    fun resetChannelName() {
        channelName = ""
    }

    fun resetFileName() {
        fileName = ""
    }

    private val table = supabaseClient.postgrest["mensajes"]

    fun subscribeToChannel(channelName: String, groupId: Int) {
        viewModelScope.launch {
            try {
                val channel = supabaseClient.channel(channelName)

                val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "mensajes"
                }

                changes.onEach {
                    if (it is PostgresAction.Insert) {
                        getMessages(groupId)
                    }
                }.launchIn(this)

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

    fun insertMessage(
        message: String,
        groupId: Int,
        userId: String,
        userName: String,
        userRole: String,
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val dateFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("America/Bogota")
                    val currentDate = dateFormat.format(Date(System.currentTimeMillis()))

                    table.insert(buildJsonObject {
                        put("mensaje", message)
                        put("fecha_envio", currentDate)
                        put("id_grupo", groupId)
                        put("id_usuario", userId)
                        put("sender_info", buildJsonObject {
                            put("nombre", userName.split(" ")[0])
                            put("rol", userRole)
                        })
                    })
                }

                resetMessage()
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

    private var fileName = ""

    fun uploadAudioFile(audioFile: File) {
        viewModelScope.launch(Dispatchers.IO) {
            _uploadState.value = UploadState.Uploading

            try {
                val bucketApi = supabaseClient.storage.from("audios")
                val bucketFiles = bucketApi.list()
                val consecutive = getConsecutive(bucketFiles).toString()

                fileName = "${consecutive.padStart(3, '0')}_${channelName.removeAccents()}.m4a"

                bucketApi.upload(path = fileName, file = audioFile) {
                    contentType = ContentType.Audio.MP4
                    upsert = false
                }

                _uploadState.value = UploadState.Success(fileName)
            } catch (e: Exception) {
                println("ChatViewModel.uploadAudioFile: Error: ${e.message}")
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getAudioTranscript(fileName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _transcriptUiState.value = TranscriptUiState.Loading
            _uploadState.value = UploadState.Idle

            _transcriptUiState.value = try {
                val response = client.get(
                    urlString = "https://transcripcion-voz-a-texto-asr.onrender.com/api/audio/$fileName"
                )

                if (response.status.value == 200) {
                    val transcript = response.body<TranscriptResponse>()
                    TranscriptUiState.Success(transcript.message)
                } else {
                    TranscriptUiState.Error(
                        "Error al obtener la transcripción del audio: ${response.status}"
                    )
                }
            } catch (e: IOException) {
                TranscriptUiState.Error("Error al comunicarse con el servidor: ${e.message}")
            }
        }
    }

    private fun getConsecutive(list: List<FileObject>): Int {
        val lastObject = list.lastOrNull()
        return lastObject?.name?.take(3)?.toInt()?.inc() ?: 1
    }
}

@Serializable
data class TranscriptResponse(
    val message: String,
)
