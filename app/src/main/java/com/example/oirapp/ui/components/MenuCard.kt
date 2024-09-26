package com.example.oirapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.model.MenuItem
import com.example.oirapp.ui.preview.DarkLightPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun MenuCard(menuItems: List<MenuItem>, modifier: Modifier = Modifier) {
    Card(
        onClick = {},
        enabled = false,
        elevation = CardDefaults.cardElevation(disabledElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 24.dp)
                .width(IntrinsicSize.Max),
        ) {
            menuItems.forEach { item ->
                MenuOption(
                    onClick = item.onClick,
                    icon = item.icon,
                    textId = item.textId,
                )
            }
        }
    }
}

@Composable
fun MenuOption(
    onClick: () -> Unit,
    icon: ImageVector,
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
        modifier = modifier,
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W500,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@DarkLightPreviews
@Composable
private fun MenuCardPreview() {
    MyApplicationTheme {
        MenuCard(
            menuItems = listOf(
                MenuItem(onClick = {}, icon = Icons.Default.Edit, textId = R.string.editar),
                MenuItem(onClick = {}, icon = Icons.Default.Delete, textId = R.string.eliminar),
            ),
        )
    }
}
