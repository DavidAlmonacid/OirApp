package com.example.oirapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup

@Composable
fun MenuPopup(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Popup(
        alignment = Alignment.TopEnd,
        offset = IntOffset(x = -48, y = 160),
        onDismissRequest = onDismissRequest,
    ) {
        MenuCard {
            content()
        }
    }
}
