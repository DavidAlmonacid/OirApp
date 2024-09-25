package com.example.oirapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import com.example.oirapp.R

object MenuItemRepository {
    fun getMainOptions(
        onGoToMyAccount: () -> Unit,
        onCloseSession: () -> Unit,
    ) = listOf(
        MenuItem(
            onClick = onGoToMyAccount,
            icon = Icons.Default.AccountCircle,
            textId = R.string.mi_cuenta,
        ),
        MenuItem(
            onClick = onCloseSession,
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            textId = R.string.cerrar_sesion,
        ),
    )

    fun getCardOptions(
        onEdit: () -> Unit,
        onDelete: () -> Unit,
    ) = listOf(
        MenuItem(
            onClick = onEdit,
            icon = Icons.Default.Edit,
            textId = R.string.editar,
        ),
        MenuItem(
            onClick = onDelete,
            icon = Icons.Default.Delete,
            textId = R.string.eliminar,
        ),
    )
}
