package com.example.oirapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.components.SelectRoleDropdown
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun CrearCuentaScreen(modifier: Modifier = Modifier) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val nameState = remember { mutableStateOf("") }

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
                value = emailState.value,
                onValueChange = { emailState.value = it },
                labelId = R.string.email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            CustomTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                labelId = R.string.password,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            CustomTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                labelId = R.string.name,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            )

            SelectRoleDropdown(options = listOf(R.string.rol_estudiante, R.string.rol_docente))

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                onClick = { /*TODO*/ },
                textId = R.string.crear_cuenta,
                modifier = Modifier.padding(bottom = 40.dp),
            )
        }
    }
}

@Preview(device = "id:pixel_5", apiLevel = 28, showBackground = true)
@Composable
private fun CrearCuentaScreenPreview() {
    MyApplicationTheme {
        CrearCuentaScreen()
    }
}
