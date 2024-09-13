package com.example.oirapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oirapp.R
import com.example.oirapp.ui.components.CustomButton
import com.example.oirapp.ui.components.CustomFamilyText
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun IniciarSesionScreen(
    onLoginButtonClicked: (String, String) -> Unit,
    onRegisterTextClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

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

            OutlinedTextField(
                value = emailState.value, //
                onValueChange = { emailState.value = it }, //
                label = { CustomFamilyText(R.string.email) }, //
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), //
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.fillMaxWidth(), //
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { CustomFamilyText(R.string.password) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            CustomButton(
                onClick = { /*TODO*/ },
                textId = R.string.ingresar,
                modifier = Modifier.padding(top = 28.dp),
            )

            CustomFamilyText(
                textId = R.string.no_account_register,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 28.dp)
                    .clickable { onRegisterTextClicked() },
            )
        }
    }
}

@Preview(device = "id:pixel_5", apiLevel = 28, showBackground = true)
@Composable
private fun IniciarSesionScreenPreview() {
    MyApplicationTheme {
        IniciarSesionScreen(
            onLoginButtonClicked = { _, _ -> },
            onRegisterTextClicked = { },
        )
    }
}
