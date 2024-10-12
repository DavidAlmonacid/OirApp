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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.data.network.Message
import com.example.oirapp.ui.theme.MyApplicationTheme
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    messages: List<Message>,
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
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        items(
            items = messages,
            key = { message -> message.id },
        ) { message ->
            ChatBubble(message = message, senderId = message.userId)
        }
    }
}

@Composable
private fun ChatBubble(
    message: Message,
    senderId: String,
    currentUserId: String = "1",
) {
    Box {
        Text(text = message.message)
        Text(text = message.sentAt.toLocalDateTime(TimeZone.currentSystemDefault()).toString())
    }
    // TODO: Saber si se puede usar Box para el mensaje, de lo contrario, usar Column
    Box {
        Text(text = message.message)
        Text(text = message.sentAt.toLocalDateTime(TimeZone.currentSystemDefault()).toString())
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
        )
    }
}
