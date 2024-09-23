package com.example.oirapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oirapp.R

@Composable
fun MenuCard(
    onCloseSession: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 32.dp),
        ) {
            Text(
                text = stringResource(R.string.mi_cuenta),
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .clickable(onClick = {
                        /* TODO: Send to the 'Mi cuenta' screen */
                    }),
            )

            Text(
                text = stringResource(R.string.cerrar_sesion),
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .clickable(onClick = onCloseSession),
            )
        }
    }
}
//@CustomPreview
//@DarkLightPreviews
//@Composable
//private fun MenuCardPreview() {
//    MyApplicationTheme {
//        MenuCard(onCloseSession = {})
//    }
//}
