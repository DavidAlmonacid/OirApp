package com.example.oirapp.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun MiPerfilScreen(modifier: Modifier = Modifier) {
    Surface {
        Text("Mi Perfil")
    }
}

@Preview
@Composable
private fun MiPerfilScreenPreview() {
    MyApplicationTheme {
        MiPerfilScreen()
    }
}
