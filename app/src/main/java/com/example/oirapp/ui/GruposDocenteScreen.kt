package com.example.oirapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oirapp.R
import com.example.oirapp.ui.components.CustomButton
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun GruposDocenteScreen(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // TODO: Convertir en un componente reutilizable
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.user_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(MaterialTheme.shapes.small),
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Nombre del usuario",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )

                    Text(
                        text = "Rol del usuario",
                        fontSize = 14.sp,
                        modifier = Modifier.alpha(0.8f),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            // TODO: El docente puede crear grupos y estos serán mostrados en una lista
            //  con su respectivo nombre y color

            CustomButton(
                onClick = {  },
                textId = R.string.crear_grupo,
                modifier = Modifier.padding(bottom = 20.dp),
            )
        }
    }
}

@DarkLightScreenPreviews
@Composable
private fun GruposDocenteScreenPreview() {
    MyApplicationTheme {
        GruposDocenteScreen()
    }
}
