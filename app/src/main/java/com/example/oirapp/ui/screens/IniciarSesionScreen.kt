package com.example.oirapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun IniciarSesionScreen(
    userEmail: String,
    onUserEmailChanged: (String) -> Unit,
    userPassword: String,
    onUserPasswordChanged: (String) -> Unit,
    onLoginButtonClicked: (String, String) -> Unit,
    onRegisterTextClicked: () -> Unit,
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
                    onDone = { onLoginButtonClicked(userEmail, userPassword) }
                ),
            )

            PrimaryButton(
                onClick = { onLoginButtonClicked(userEmail, userPassword) },
                textId = R.string.ingresar,
                modifier = Modifier.padding(top = 28.dp),
            )

            Text(
                text = stringResource(R.string.no_account_register),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(top = 28.dp)
                    .clickable { onRegisterTextClicked() },
            )
        }
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
            onLoginButtonClicked = { _, _ -> },
            onRegisterTextClicked = {},
        )
    }
}
