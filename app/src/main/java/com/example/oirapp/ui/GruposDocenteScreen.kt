package com.example.oirapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.ui.components.CustomButton
import com.example.oirapp.ui.components.UserInfo
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun GruposDocenteScreen(
    userName: String,
    userRole: String,
    /*
     * TODO: Poder agregar una imagen desde un URL proveniente de Supabase
     */
    userImageUrl: String,
    modifier: Modifier = Modifier,
) {

    println("userImageUrl: $userImageUrl")

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            UserInfo(userName, userRole)

            Spacer(modifier = Modifier.weight(1f))

            /*
             * TODO: El docente puede crear grupos y estos serán mostrados en una lista
             *  con su respectivo nombre, código y color
             */

            CustomButton(
                onClick = { },
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
        GruposDocenteScreen(
            userName = "Francisco Garzón",
            userRole = "Docente",
            userImageUrl = "",
        )
    }
}
