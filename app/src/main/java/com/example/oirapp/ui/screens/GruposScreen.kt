package com.example.oirapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.ui.components.GroupCard
import com.example.oirapp.ui.components.PrimaryButton
import com.example.oirapp.ui.components.UserInfo
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun GruposScreen(
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

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(vertical = 20.dp).alpha(0.2f),
            )

            /*
             * TODO: El usuario puede cerrar sesión
             */

            /*
             * TODO: El docente puede crear grupos y estos serán mostrados en una lista
             *  con su respectivo nombre, código y color
             */

            /*
             * TODO: El estudiante puede unirse a un grupo con un código y este será mostrado en una lista
             */

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(top = 24.dp),
            ) {
                GroupCard(
                    groupName = "Cálculo Diferencial",
                    groupCode = "3456",
                    role = userRole,
                )

                GroupCard(
                    groupName = "Emprendimiento I",
                    groupCode = "3456",
                    role = userRole,
                )

                GroupCard(
                    groupName = "Álgebra Lineal",
                    groupCode = "3456",
                    role = userRole,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                onClick = { },
                textId = if (userRole == "Estudiante") R.string.unirse_grupo else R.string.crear_grupo,
                modifier = Modifier.padding(bottom = 20.dp),
            )
        }
    }
}

@DarkLightScreenPreviews
@Composable
private fun GruposScreenDocentePreview() {
    MyApplicationTheme {
        GruposScreen(
            userName = "Francisco Garzón",
            userRole = "Docente",
            userImageUrl = "",
        )
    }
}

@DarkLightScreenPreviews
@Composable
private fun GruposScreenEstudiantePreview() {
    MyApplicationTheme {
        GruposScreen(
            userName = "David Almonacid",
            userRole = "Estudiante",
            userImageUrl = "",
        )
    }
}
