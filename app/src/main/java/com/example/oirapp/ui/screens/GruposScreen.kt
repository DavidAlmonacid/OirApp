package com.example.oirapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oirapp.R
import com.example.oirapp.data.network.Group
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.components.UserInfo
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.state.UserUiState
import com.example.oirapp.ui.theme.MyApplicationTheme
import com.example.oirapp.utils.removeUppercaseAccents
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GruposScreen(
    userState: UserUiState,
    userInput: String,
    onUserInputChanged: (String) -> Unit,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    onConfirmDialog: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {

    /*
     * TODO: Poder agregar una imagen desde un URL proveniente de Supabase
     */

    println("userImageUrl: ${userState.imageUrl}")

    var grupos by remember { mutableStateOf<List<Group>>(listOf()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            grupos = supabaseClient.from("grupos").select().decodeList<Group>()
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            UserInfo(userName = userState.name, userRole = userState.role)

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .alpha(0.2f),
            )

            /*
             * TODO: El docente puede crear grupos y estos serán mostrados en una lista
             *  con su respectivo nombre, código y color
             */

            /*
             * TODO: El estudiante puede unirse a un grupo con un código y este será mostrado en una lista
             */

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 20.dp),
            ) {
                items(grupos) { group ->
                    GroupCard(
                        groupName = group.name,
                        groupCode = group.code,
                        role = userState.role,
                        onClick = {},
                    )
                }
            }

            if (showDialog) {
                GroupInputDialog(
                    inputText = userInput,
                    onInputTextChange = { newValue -> onUserInputChanged(newValue) },
                    role = userState.role,
                    onDismissRequest = onDismissDialog,
                    onConfirm = { onConfirmDialog(userInput, userState.id) },
                )
            }
        }
    }
}

@Composable
private fun GroupCard(
    groupName: String,
    groupCode: String,
    role: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(68.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    ),
            ) {
                Text(
                    text = groupName.take(3).uppercase().removeUppercaseAccents(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium,
                )
            }

            Column {
                Text(
                    text = groupName,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (role == "Docente") {
                    Text(
                        text = groupCode,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupInputDialog(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    role: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Ingrese el " + stringResource(
                    if (role == "Estudiante") R.string.codigo_acceso else R.string.nombre_grupo
                ).lowercase(),
            )
        },
        text = {
            CustomTextField(
                value = inputText,
                onValueChange = { newValue -> onInputTextChange(newValue) },
                labelId = if (role == "Estudiante") R.string.codigo_acceso else R.string.nombre_grupo,
            )
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(
                        if (role == "Estudiante") R.string.unirse else R.string.crear_grupo
                    )
                )
            }
        },
    )
}

@DarkLightScreenPreviews
@Composable
private fun GruposScreenDocentePreview() {
    MyApplicationTheme {
        GruposScreen(
            userState = UserUiState(
                id = "",
                name = "Francisco Garzón",
                role = "Docente",
                imageUrl = "",
            ),
            userInput = "",
            onUserInputChanged = {},
            showDialog = true,
            onDismissDialog = {},
            onConfirmDialog = { _, _ -> },
        )
    }
}

@DarkLightScreenPreviews
@Composable
private fun GruposScreenEstudiantePreview() {
    MyApplicationTheme {
        GruposScreen(
            userState = UserUiState(
                id = "",
                name = "David Almonacid",
                role = "Estudiante",
                imageUrl = "",
            ),
            userInput = "",
            onUserInputChanged = {},
            showDialog = true,
            onDismissDialog = {},
            onConfirmDialog = { _, _ -> },
        )
    }
}
