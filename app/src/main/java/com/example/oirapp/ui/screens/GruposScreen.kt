package com.example.oirapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oirapp.R
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
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
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.preview.CustomPreview
import com.example.oirapp.ui.viewmodel.GruposViewModel
import com.example.oirapp.utils.removeUppercaseAccents
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
    //gruposViewModel: GruposViewModel,
    //userState: UserUiState,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
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
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .alpha(0.2f),
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

            if (showDialog) {
                GroupInputDialog(
                    inputText = groupName,
                    onInputTextChange = { newValue -> groupName = newValue },
                    onDismissRequest = onDismissDialog,
                    onConfirm = {
                        //gruposViewModel.createGroup(groupName)
                    },
                )
            }

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
                    //gruposViewModel.createGroup(groupName)
                },
                textId = if (userRole == "Estudiante") R.string.unirse_grupo else R.string.crear_grupo,
                modifier = Modifier.padding(bottom = 20.dp),
            )
        }
    }
}

@Composable
fun GroupCard(
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
fun GroupInputDialog(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(
            text =
//            stringResource(
//                if (inputText.isEmpty()) {
//                    R.string.unirse_grupo
//                } else {
//                    R.string.crear_grupo
//                }
//            )
            "Ingrese el nombre del grupo o el código de acceso"
        ) },
        text = {
            Column {
                CustomTextField(
                    value = inputText,
                    onValueChange = onInputTextChange,
                    labelId = R.string.iniciar_sesion
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(
//                    if (userState.role == "Estudiante") {
//                        R.string.unirse_grupo
//                    } else {
//                        R.string.crear_grupo
//                    }
                    R.string.accept
                ))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
@CustomPreview
@Composable
private fun GroupInputDialogPreview() {
    MyApplicationTheme {
        GroupInputDialog(
            inputText = "",
            onInputTextChange = {},
            onDismissRequest = {},
            onConfirm = {},
        )
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
            showDialog = true,
            onDismissDialog = {},
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
            showDialog = false,
            onDismissDialog = {},
        )
    }
}
