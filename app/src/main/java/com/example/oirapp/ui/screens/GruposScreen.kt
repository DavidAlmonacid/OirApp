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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.oirapp.R
import com.example.oirapp.data.network.Group
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.components.MenuCard
import com.example.oirapp.ui.components.MenuItem
import com.example.oirapp.ui.components.UserInfo
import com.example.oirapp.ui.preview.CustomPreview
import com.example.oirapp.ui.preview.DarkLightPreviews
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.state.UserUiState
import com.example.oirapp.ui.theme.MyApplicationTheme
import com.example.oirapp.utils.removeUppercaseAccents

@Composable
fun GruposScreen(
    userUiState: UserUiState,
    groups: List<Group>,
    userInput: String,
    onUserInputChanged: (String) -> Unit,
    errorMessage: String,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    onConfirmDialog: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    /*
     * TODO: Poder agregar una imagen desde un URL proveniente de Supabase
     */

    println("userImageUrl: ${userUiState.imageUrl}")

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            UserInfo(userName = userUiState.name, userRole = userUiState.role)

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .alpha(0.2f),
            )

            /*
             * TODO: El estudiante puede unirse a un grupo con un código y este será mostrado en una lista
             */

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 20.dp),
            ) {
                items(
                    items = groups,
                    key = { group -> group.id },
                ) { group ->
                    GroupCard(
                        onClick = {},
                        groupName = group.name,
                        groupCode = group.code,
                        role = userUiState.role,
                    )
                }
            }

            if (showDialog) {
                GroupInputDialog(
                    inputText = userInput,
                    onInputTextChange = { newValue -> onUserInputChanged(newValue) },
                    role = userUiState.role,
                    onDismissRequest = onDismissDialog,
                    onConfirm = { onConfirmDialog(userUiState.id) },
                    errorMessage = errorMessage,
                )
            }
        }
    }
}

@Composable
private fun GroupCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    groupName: String,
    groupCode: String,
    role: String,
) {
    var showMenuCard by remember { mutableStateOf(false) }

    Box {
        Card(
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = groupName,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (role == "Docente") {
                        Text(
                            text = groupCode,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }

                if (role == "Docente") {
                    IconButton(
                        onClick = { showMenuCard = !showMenuCard },
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(24.dp)
                            .align(Alignment.Top),
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.open_menu),
                        )
                    }
                }
            }
        }

        if (showMenuCard) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(x = -40, y = 88),
                onDismissRequest = { showMenuCard = false },
            ) {
                MenuCard {
                    MenuItem(
                        onClick = { /* TODO('El docente puede editar el nombre de un grupo') */ },
                        icon = Icons.Default.Edit,
                        textId = R.string.editar,
                    )

                    MenuItem(
                        onClick = { /* TODO('El docente puede eliminar un grupo') */ },
                        icon = Icons.Default.Delete,
                        textId = R.string.eliminar,
                    )
                }
            }
        }
    }
}

@DarkLightPreviews
@Composable
private fun GroupCardDocentePreview() {
    MyApplicationTheme {
        GroupCard(
            onClick = {},
            groupName = "Grupo de Matemáticas",
            groupCode = "ABC123",
            role = "Docente",
        )
    }
}

@Composable
private fun GroupInputDialog(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    role: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    errorMessage: String,
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
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CustomTextField(
                    value = inputText,
                    onValueChange = { newValue -> onInputTextChange(newValue) },
                    labelId = if (role == "Estudiante") R.string.codigo_acceso else R.string.nombre_grupo,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Words,
                    ),
                    keyboardActions = KeyboardActions(onDone = { onConfirm() }),
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
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

@CustomPreview
@Composable
private fun GroupInputDialogDocentePreview() {
    MyApplicationTheme {
        GroupInputDialog(
            inputText = "",
            onInputTextChange = {},
            role = "Docente",
            onDismissRequest = {},
            onConfirm = {},
            errorMessage = "El nombre del grupo ya existe."
        )
    }
}

@CustomPreview
@Composable
private fun GroupInputDialogEstudiantePreview() {
    MyApplicationTheme {
        GroupInputDialog(
            inputText = "",
            onInputTextChange = {},
            role = "Estudiante",
            onDismissRequest = {},
            onConfirm = {},
            errorMessage = "",
        )
    }
}

@DarkLightScreenPreviews
@Composable
private fun GruposScreenDocentePreview() {
    MyApplicationTheme {
        GruposScreen(
            userUiState = UserUiState(
                id = "",
                name = "Francisco Garzón",
                role = "Docente",
                imageUrl = "",
            ),
            groups = emptyList(),
            userInput = "",
            onUserInputChanged = {},
            errorMessage = "",
            showDialog = true,
            onDismissDialog = {},
            onConfirmDialog = { _ -> },
        )
    }
}

@DarkLightScreenPreviews
@Composable
private fun GruposScreenEstudiantePreview() {
    MyApplicationTheme {
        GruposScreen(
            userUiState = UserUiState(
                id = "",
                name = "David Almonacid",
                role = "Estudiante",
                imageUrl = "",
            ),
            groups = listOf(
                Group(
                    id = 1,
                    name = "Grupo de Matemáticas",
                    code = "ABC123",
                    idDocente = "1",
                ),
                Group(
                    id = 2,
                    name = "Grupo de Física",
                    code = "DEF456",
                    idDocente = "2",
                ),
            ),
            userInput = "",
            onUserInputChanged = {},
            errorMessage = "",
            showDialog = false,
            onDismissDialog = {},
            onConfirmDialog = { _ -> },
        )
    }
}
