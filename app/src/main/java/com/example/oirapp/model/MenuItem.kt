package com.example.oirapp.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val onClick: () -> Unit,
    val icon: ImageVector,
    @StringRes val textId: Int,
)
