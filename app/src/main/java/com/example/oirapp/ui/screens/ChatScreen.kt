package com.example.oirapp.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    // TODO('Implement ChatScreen')
    Surface(
        onClick = { /*TODO*/ },
        modifier = modifier,
    ) {
        // ChatScreenContent()
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    MyApplicationTheme {
        ChatScreen()
    }
}
