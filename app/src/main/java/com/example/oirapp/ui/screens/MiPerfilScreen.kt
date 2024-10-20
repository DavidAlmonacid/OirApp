package com.example.oirapp.ui.screens

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.oirapp.R
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.components.PrimaryButton
import com.example.oirapp.ui.preview.DarkLightScreenPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MiPerfilScreen(
    userEmail: String,
    onUserEmailChanged: (String) -> Unit,
    userPassword: String,
    onUserPasswordChanged: (String) -> Unit,
    userName: String,
    onUserNameChanged: (String) -> Unit,
    userRole: String,
    onUserRoleChanged: (String) -> Unit,
    onUpdateButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    ProfileImagePicker(onImagePicked = { uri ->
        selectedImageUri = uri
    })

    selectedImageUri?.let { uri ->
        Image(
            painter = rememberImagePainter(uri),
            contentDescription = "Imagen de perfil",
            modifier = Modifier.size(128.dp)
        )
    }
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp),
        ) {
            // Imagen de usuario
            Image(
                painter = painterResource(R.drawable.user_placeholder),
                contentDescription = null, // No es necesario describir la imagen de usuario
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            // Campo de Correo
            CustomTextField(
                value = userEmail,
                onValueChange = onUserEmailChanged,
                labelId = R.string.email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            // Campo de Contraseña
            CustomTextField(
                value = userPassword,
                onValueChange = onUserPasswordChanged,
                labelId = R.string.password,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            // Campo de Nombre y Apellido
            CustomTextField(
                value = userName,
                onValueChange = onUserNameChanged,
                labelId = R.string.name,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            // Campo de Rol
            CustomTextField(
                value = userRole,
                onValueChange = onUserRoleChanged,
                labelId = R.string.select_role,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onUpdateButtonClicked() })
            )

            // Botón Actualizar
            PrimaryButton(
                onClick = onUpdateButtonClicked,
                textId = R.string.update,
                modifier = Modifier.padding(top = 28.dp)
            )
        }
    }
}

@DarkLightScreenPreviews
@Composable
fun ActualizarInfoScreenPreview() {
    MyApplicationTheme {
        MiPerfilScreen(
            userEmail = "",
            onUserEmailChanged = {},
            userPassword = "",
            onUserPasswordChanged = {},
            userName = "",
            onUserNameChanged = {},
            userRole = "",
            onUserRoleChanged = {},
            onUpdateButtonClicked = {}
        )
    }
}

@Composable
fun ProfileImagePicker(
    onImagePicked: (Uri) -> Unit,
) {
    val context = LocalContext.current

    // URI donde se almacenará la foto tomada con la cámara
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // ActivityResultLauncher para tomar una foto con la cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let { uri ->
                onImagePicked(uri) // Retornar la URI de la foto capturada
            }
        }
    }

    // ActivityResultLauncher para seleccionar una imagen de la galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onImagePicked(it) // Retornar la URI de la imagen seleccionada
        }
    }

    // Función para abrir la cámara
    fun openCamera() {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            createImageFile(context) // Función que crea un archivo temporal
        )
        photoUri = uri
        takePictureLauncher.launch(uri)
    }

    // Función para abrir la galería
    fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    // Botones para las opciones
    Column {
        Button(onClick = { openCamera() }) {
            Text(text = "Tomar foto")
        }
        Button(onClick = { openGallery() }) {
            Text(text = "Seleccionar desde galería")
        }
    }
}

fun createImageFile(context: Context): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timestamp}_",
        ".jpg",
        storageDir
    )
}