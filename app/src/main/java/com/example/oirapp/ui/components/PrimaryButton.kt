package com.example.oirapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.oirapp.R
import com.example.oirapp.ui.preview.DarkLightPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@DarkLightPreviews
@Composable
private fun PrimaryButtonPreview() {
    MyApplicationTheme {
        PrimaryButton(onClick = {}, textId = R.string.iniciar_sesion)
    }
}
