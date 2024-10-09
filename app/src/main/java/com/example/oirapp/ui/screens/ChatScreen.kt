package com.example.oirapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                items(36) {
                    Text("Item ${it.inc()}")
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(stringResource(R.string.message)) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    MyApplicationTheme {
        ChatScreen()
    }
}
