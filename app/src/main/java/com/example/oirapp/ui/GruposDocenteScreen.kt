package com.example.oirapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oirapp.R
import com.example.oirapp.ui.components.CustomButton
import com.example.oirapp.ui.components.CustomFamilyText

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
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.user_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(MaterialTheme.shapes.small),
                )

                Column {
                    CustomFamilyText(
                        text = "Nombre del docente",
                        //style = MaterialTheme.typography.h6,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
                    )

                    CustomFamilyText(
                        text = "Correo del docente",
                        //style = MaterialTheme.typography.body1,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 16.dp).alpha(0.8f),
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                onClick = {  },
                textId = R.string.crear_grupo,
                modifier = Modifier.padding(bottom = 20.dp),
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 28, device = "id:pixel_5")
@Composable
private fun GruposDocenteScreenPreview() {
    MyApplicationTheme {
        GruposDocenteScreen()
    }
}
