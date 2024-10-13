package com.example.oirapp.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.data.network.Message
import com.example.oirapp.ui.theme.MyApplicationTheme
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    userId: String,
    userMessage: String,
    onUserMessageChanged: (String) -> Unit,
    onSendMessage: (String) -> Unit,
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
                onUserMessageChanged = onUserMessageChanged,
                onSendMessage = onSendMessage,
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        items(
            items = messages,
            key = { message -> message.id },
        ) { message ->
            ChatBubble(message = message, senderId = message.userId, currentUserId = userId)
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
        modifier = modifier.fillMaxWidth(),
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = if (isCurrentUser) 18.dp else 0.dp,
                topEnd = if (isCurrentUser) 0.dp else 18.dp,
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

@Preview(apiLevel = 28, showBackground = true)
@Composable
private fun ChatBubbleUserPreview() {
    MyApplicationTheme {
        ChatBubble(
            message = Message(
                id = 1,
                userId = "1",
                message = "Hello, world!",
                sentAt = Instant.fromEpochMilliseconds(1630000000000),
                groupId = 1,
            ),
            senderId = "1",
            currentUserId = "1",
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
        )
    }
}

@Preview(apiLevel = 28, showBackground = true)
@Composable
private fun ChatBubbleOtherPreview() {
    MyApplicationTheme {
        ChatBubble(
            message = Message(
                id = 1,
                userId = "2",
                message = "Hello, world!",
                sentAt = Instant.fromEpochMilliseconds(1630000000000),
                groupId = 1,
            ),
            senderId = "2",
            currentUserId = "1",
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
        )
    }
}

@Composable
private fun ChatMessageComposer(
    modifier: Modifier = Modifier,
    userMessage: String,
    onUserMessageChanged: (String) -> Unit,
    onSendMessage: (String) -> Unit,
) {
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
            modifier = Modifier.weight(1f),
        )

        IconButton(
            onClick = { onSendMessage(userMessage) },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
            )
        }
    }
}

@Preview(apiLevel = 28)
@Composable
private fun ChatScreenPreview() {
    MyApplicationTheme {
        ChatScreen(
            messages = emptyList(),
            userMessage = "",
            onUserMessageChanged = {},
            onSendMessage = {},
            userId = "1",
        )
    }
}
