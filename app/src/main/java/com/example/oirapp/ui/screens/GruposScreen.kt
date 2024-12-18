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
import com.example.oirapp.ui.state.UserUiState
import com.example.oirapp.ui.theme.bodyFontFamilyMono
import com.example.oirapp.ui.viewmodel.GroupState
import com.example.oirapp.utils.removeAccents

@Composable
fun GruposScreen(
    modifier: Modifier = Modifier,
    userUiState: UserUiState,
    groupState: GroupState?,
    groups: List<Group>,
    showDialog: Boolean,
    userInput: String,
    onUserInputChanged: (String) -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmDialog: (String) -> Unit,
    errorMessage: String,
    openEditDialog: (Int, String) -> Unit,
    openDeleteDialog: (Int) -> Unit,
    onGroupCardCLick: (String, Int) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            UserInfo(
                userName = userUiState.name,
                userRole = userUiState.role,
                userImageUrl = userUiState.imageUrl,
            )

            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .alpha(0.2f),
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 20.dp),
            ) {
                items(
                    items = groups,
                    key = { group -> group.id },
                ) { group ->
                    GroupCard(
                        onClick = { onGroupCardCLick(group.name, group.id) },
                        groupName = group.name,
                        groupCode = group.code,
                        role = userUiState.role,
                        openEditDialog = { openEditDialog(group.id, group.name) },
                        openDeleteDialog = { openDeleteDialog(group.id) },
                    )
                }
            }

            if (showDialog && groupState !is GroupState.Delete) {
                GroupInputDialog(
                    groupState = groupState,
                    inputText = userInput,
                    onInputTextChange = { newValue -> onUserInputChanged(newValue) },
                    role = userUiState.role,
                    onDismissDialog = onDismissDialog,
                    onConfirmDialog = { onConfirmDialog(userUiState.id) },
                    errorMessage = errorMessage,
                )
            }

            if (showDialog && groupState is GroupState.Delete && userUiState.role == "Docente") {
                DeleteGroupDialog(
                    onDismissDialog = onDismissDialog,
                    onConfirmDialog = { onConfirmDialog(userUiState.id) },
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
    openEditDialog: () -> Unit,
    openDeleteDialog: () -> Unit,
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
                        .size(60.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        ),
                ) {
                    Text(
                        text = groupName.take(3).uppercase().removeAccents(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = groupName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (role == "Docente") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp),
                        ) {
                            Text(text = stringResource(R.string.codigo))
                            Text(
                                text = groupCode,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                fontFamily = bodyFontFamilyMono,
                                letterSpacing = 1.sp,
                            )
                        }
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
                        onClick = {
                            openEditDialog()
                            showMenuCard = false
                        },
                        icon = Icons.Default.Edit,
                        textId = R.string.editar,
                    )

                    MenuItem(
                        onClick = {
                            openDeleteDialog()
                            showMenuCard = false
                        },
                        icon = Icons.Default.Delete,
                        textId = R.string.eliminar,
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupInputDialog(
    groupState: GroupState?,
    inputText: String,
    onInputTextChange: (String) -> Unit,
    role: String,
    onDismissDialog: () -> Unit,
    onConfirmDialog: () -> Unit,
    errorMessage: String,
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
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
                        capitalization = if (role == "Estudiante") {
                            KeyboardCapitalization.Characters
                        } else {
                            KeyboardCapitalization.Words
                        },
                    ),
                    keyboardActions = KeyboardActions(onDone = { onConfirmDialog() }),
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
            TextButton(onClick = onDismissDialog) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmDialog) {
                Text(
                    text = stringResource(
                        if (role == "Estudiante") {
                            R.string.unirse
                        } else {
                            if (groupState is GroupState.Create) {
                                R.string.crear_grupo
                            } else {
                                R.string.editar
                            }
                        }
                    )
                )
            }
        },
    )
}

@Composable
private fun DeleteGroupDialog(
    onDismissDialog: () -> Unit,
    onConfirmDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = { Text(text = stringResource(R.string.eliminar_grupo)) },
        text = { Text(text = stringResource(R.string.confirmar_eliminacion_grupo)) },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmDialog) {
                Text(text = stringResource(R.string.eliminar))
            }
        },
    )
}
