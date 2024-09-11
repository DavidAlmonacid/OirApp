package com.example.oirapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun BienvenidaScreen(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Box(modifier = Modifier.padding(top = 16.dp)) {
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 290.dp),
            ) {
                Text(
                    text = stringResource(R.string.bienvenida1),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp),
                )

                Text(
                    text = stringResource(R.string.bienvenida2),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 20.dp, bottom = 32.dp),
                )
            }

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_description),
                modifier = Modifier
                    .sizeIn(400.dp, 400.dp)
                    .align(Alignment.TopCenter),
            )

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .widthIn(max = 120.dp)
                    .padding(top = 20.dp, end = 24.dp),
            )

            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 28.dp),
            ) {
                Text(
                    text = "Inicio"
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5", apiLevel = 28)
@Composable
private fun BienvenidaScreenPreview() {
    MyApplicationTheme {
        BienvenidaScreen()
    }
}
