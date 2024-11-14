package com.example.oirapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.oirapp.R
import com.example.oirapp.data.network.Message
import com.example.oirapp.record.AudioRecorderImpl
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    userId: String,
    userRole: String,
    userMessage: String,
    onUserMessageChanged: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onStopRecording: (File) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize(),
        ) {
            ChatMessages(
                messages = messages,
                userId = userId,
                modifier = Modifier.weight(1f),
            )

            ChatMessageComposer(
                userMessage = userMessage,
                userRole = userRole,
                onUserMessageChanged = onUserMessageChanged,
                onSendMessage = onSendMessage,
                onStopRecording = onStopRecording,
            )
        }
    }
}

@Composable
private fun ChatMessages(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    userId: String,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        items(
            items = messages,
            key = { message -> message.id },
        ) { message ->
            ChatBubble(
                message = message,
                senderId = message.userId,
                currentUserId = userId,
            )
        }
    }
}

@Composable
private fun ChatBubble(
    modifier: Modifier = Modifier,
    message: Message,
    senderId: String,
    currentUserId: String,
) {
    val isCurrentUser = senderId == currentUserId

    Box(
        contentAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = if (isCurrentUser) 18.dp else 4.dp,
                topEnd = if (isCurrentUser) 4.dp else 18.dp,
                bottomStart = 18.dp,
                bottomEnd = 18.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                },
                contentColor = if (isCurrentUser) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSecondary
                },
            ),
            modifier = Modifier.fillMaxWidth(0.9f),
        ) {
            Column(Modifier.padding(vertical = 10.dp, horizontal = 12.dp)) {
                val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                timeFormatter.timeZone = TimeZone.getTimeZone("America/Bogota")
                val formattedTime = timeFormatter.format(message.sentAt.toEpochMilliseconds())

                if (!isCurrentUser) {
                    Text(
                        text = "${message.senderInfo.name} | ${message.senderInfo.role}",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }

                Text(
                    text = message.message,
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text = formattedTime,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ChatMessageComposer(
    modifier: Modifier = Modifier,
    userMessage: String,
    userRole: String,
    onUserMessageChanged: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onStopRecording: (File) -> Unit,
) {
    val context = LocalContext.current
    val recorder by lazy { AudioRecorderImpl(context.applicationContext) }
    var audioFile: File? = null
    var isRecording by rememberSaveable { mutableStateOf(false) }
    var hasPermission by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            hasPermission = true
        }
    }

    // Add permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted

        if (isGranted) {
            isRecording = true
            audioFile = File(context.cacheDir, "audio.m4a").also {
                recorder.startRecording(it)
            }
        }
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(horizontal = 8.dp),
    ) {
        OutlinedTextField(
            value = userMessage,
            onValueChange = { newValue -> onUserMessageChanged(newValue) },
            placeholder = { Text(stringResource(R.string.message)) },
            shape = MaterialTheme.shapes.extraLarge,
            maxLines = 6,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier.weight(1f),
        )

        IconButton(
            onClick = {
                if (userRole == "Estudiante") {
                    onSendMessage(userMessage)
                }

                if (userRole == "Docente") {
                    if (userMessage.isEmpty()) {
                        if (isRecording) {
                            isRecording = false

                            recorder.stopRecording()
                            audioFile?.let { onStopRecording(it) }
                        } else {
                            if (hasPermission) {
                                isRecording = true
                                audioFile = File(context.cacheDir, "audio.m4a").also {
                                    recorder.startRecording(it)
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                    } else {
                        onSendMessage(userMessage)
                    }
                }
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier.size(56.dp),
        ) {
            if (userRole == "Estudiante") {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                )
            }

            if (userRole == "Docente") {
                if (userMessage.isEmpty()) {
                    if (isRecording) {
                        Icon(
                            painter = painterResource(R.drawable.stop),
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.microphone),
                            contentDescription = null,
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}
