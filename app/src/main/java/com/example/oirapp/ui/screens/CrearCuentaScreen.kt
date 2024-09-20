package com.example.oirapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.ui.components.CustomButton
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.components.SelectRoleDropdown
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.state.RegisterState
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun CrearCuentaScreen(
    userEmail: String,
    onUserEmailChanged: (String) -> Unit,
    userPassword: String,
    onUserPasswordChanged: (String) -> Unit,
    userName: String,
    onUserNameChanged: (String) -> Unit,
    userRole: String,
    onUserRoleChanged: (String) -> Unit,
    onRegisterButtonClicked: () -> Unit,
    registerState: RegisterState?,
    showSuccessDialog: Boolean,
    onDismissSuccessDialog: () -> Unit,
    showErrorDialog: Boolean,
    onDismissErrorDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_description),
                modifier = Modifier.size(152.dp),
            )

            CustomTextField(
                value = userEmail,
                onValueChange = { newValue -> onUserEmailChanged(newValue) },
                labelId = R.string.email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )

            CustomTextField(
                value = userPassword,
                onValueChange = { newValue -> onUserPasswordChanged(newValue) },
                labelId = R.string.password,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
            )

            CustomTextField(
                value = userName,
                onValueChange = { newValue -> onUserNameChanged(newValue) },
                labelId = R.string.name,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
            )

            SelectRoleDropdown(
                options = roleOptions,
                selectedOption = userRole,
                onOptionSelected = { newValue -> onUserRoleChanged(newValue) },
            )

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                onClick = onRegisterButtonClicked,
                textId = R.string.crear_cuenta,
                modifier = Modifier.padding(bottom = 40.dp),
            )

            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { onDismissSuccessDialog() },
                    title = { Text(text = stringResource(R.string.success)) },
                    text = {
                        Text(
                            text = stringResource(
                                R.string.email_verification_sent,
                                userEmail,
                            )
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { onDismissSuccessDialog() }) {
                            Text(text = stringResource(R.string.accept))
                        }
                    },
                )
            }

            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { onDismissErrorDialog() },
                    title = { Text(text = stringResource(R.string.error)) },
                    text = { Text(text = "Error") },
                    confirmButton = {
                        TextButton(onClick = { onDismissErrorDialog() }) {
                            Text(text = stringResource(R.string.accept))
                        }
                    },
                )
            }
        }
    }
}

val roleOptions = listOf(
    R.string.rol_estudiante,
    R.string.rol_docente,
)

@DarkLightScreenPreviews
@Composable
private fun CrearCuentaScreenPreview() {
    MyApplicationTheme {
        CrearCuentaScreen(
            userEmail = "",
            onUserEmailChanged = {},
            userPassword = "",
            onUserPasswordChanged = {},
            userName = "",
            onUserNameChanged = {},
            userRole = "",
            onUserRoleChanged = {},
            onRegisterButtonClicked = {},
            registerState = null,
            showSuccessDialog = false,
            onDismissSuccessDialog = {},
            showErrorDialog = false,
            onDismissErrorDialog = {},
        )
    }
}
