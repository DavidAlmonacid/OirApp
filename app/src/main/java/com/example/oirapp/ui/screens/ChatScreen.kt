package com.example.oirapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(text = "Chat Screen")
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    MyApplicationTheme {
        ChatScreen()
    }
}
