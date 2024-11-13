package com.example.oirapp.ui.components

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.ui.preview.DarkLightPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun MenuCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
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
            content()
        }
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    @StringRes textId: Int,
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
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    @StringRes textId: Int,
) {
    TextButton(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
        modifier = modifier,
    ) {
        Icon(painter = painterResource(icon), contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@DarkLightPreviews
@Composable
private fun MenuCardPreview() {
    MyApplicationTheme {
        MenuCard {
            MenuItem(
                onClick = {},
                icon = R.drawable.file,
                textId = R.string.editar,
            )

            MenuItem(
                onClick = {},
                icon = Icons.Default.Delete,
                textId = R.string.eliminar,
            )
        }
    }
}
