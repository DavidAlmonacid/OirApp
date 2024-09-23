package com.example.oirapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
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
import com.example.oirapp.ui.components.PrimaryButton
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.state.LoginState
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun IniciarSesionScreen(
    userEmail: String,
    onUserEmailChanged: (String) -> Unit,
    userPassword: String,
    onUserPasswordChanged: (String) -> Unit,
    onLoginButtonClicked: () -> Unit,
    onRegisterTextClicked: () -> Unit,
    loginState: LoginState?,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
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
                onValueChange = onUserEmailChanged,
                labelId = R.string.email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )

            CustomTextField(
                value = userPassword,
                onValueChange = onUserPasswordChanged,
                labelId = R.string.password,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onLoginButtonClicked() }
                ),
            )

            PrimaryButton(
                onClick = onLoginButtonClicked,
                textId = R.string.ingresar,
                modifier = Modifier.padding(top = 28.dp),
            )

            Text(
                text = stringResource(R.string.no_account_register),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = 28.dp)
                    .clickable(onClick = onRegisterTextClicked),
            )
        }
    }

    if (showDialog && loginState is LoginState.Error) {
        AlertDialog(
            onDismissRequest = { onDismissDialog() },
            title = { Text(text = stringResource(R.string.error)) },
            text = { Text(text = loginState.message) },
            confirmButton = {
                TextButton(onClick = { onDismissDialog() }) {
                    Text(text = stringResource(R.string.accept))
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@DarkLightScreenPreviews
@Composable
private fun IniciarSesionScreenPreview() {
    MyApplicationTheme {
        IniciarSesionScreen(
            userEmail = "",
            onUserEmailChanged = {},
            userPassword = "",
            onUserPasswordChanged = {},
            onLoginButtonClicked = {},
            onRegisterTextClicked = {},
            loginState = null,
            showDialog = true,
            onDismissDialog = {},
        )
    }
}
