package com.example.oirapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.components.GroupCard
import com.example.oirapp.ui.components.PrimaryButton
import com.example.oirapp.ui.components.UserInfo
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oirapp.data.network.Group
import com.example.oirapp.ui.viewmodel.GruposViewModel
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun GruposScreen(
    userName: String,
    userRole: String,
    /*
     * TODO: Poder agregar una imagen desde un URL proveniente de Supabase
     */
    userImageUrl: String,
    modifier: Modifier = Modifier,
    gruposViewModel: GruposViewModel = viewModel()
) {
    var groupName by remember { mutableStateOf("") }
    var grupos by remember { mutableStateOf<List<Group>>(listOf()) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            grupos = supabaseClient.from("grupos")
                .select().decodeList<Group>()
        }
    }
    println("userImageUrl: $userImageUrl")
    println("grupos: $grupos")
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
            TextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Nombre del Grupo") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(top = 24.dp),
            ) {
                items(grupos) { group ->
                    GroupCard(
                        groupName = group.name,
                        groupCode = group.code,
                        role = userRole,
                        onClick = {},
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                onClick = {
                    gruposViewModel.createGroup(groupName)
                },
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
