package com.example.oirapp.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.oirapp.R
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.components.PrimaryButton
import com.example.oirapp.ui.preview.CustomPreview
import com.example.oirapp.ui.theme.MyApplicationTheme
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun MiPerfilScreen(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    userEmail: String,
    onUserEmailChanged: (String) -> Unit,
    userPassword: String,
    onUserPasswordChanged: (String) -> Unit,
    userName: String,
    onUserNameChanged: (String) -> Unit,
    onUpdateButtonClicked: () -> Unit,
) {
    val context = LocalContext.current

    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var bitmapImage by rememberSaveable { mutableStateOf<Bitmap?>(null) }
    var imageFile by rememberSaveable { mutableStateOf<File?>(null) }

    var showSelectionPicker by rememberSaveable { mutableStateOf(false) }

    // Utiliza LaunchedEffect para subir la imagen cuando imageFile cambia
    LaunchedEffect(imageFile) {
        imageFile?.let { file ->
            withContext(Dispatchers.IO) {
                try {
                    uploadImageToSupabase(userName, file)
                    imageFile = null
                } catch (e: Exception) {
                    println("MiPerfilScreen: Error al subir la imagen a Supabase: ${e.message}")
                }
            }
        }
    }

    // Image picker from Gallery
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            bitmapImage = null

            // Guarda el archivo en el caché
            imageFile = File(context.cacheDir, "image.jpg").apply {
                // Aquí asegúrate de que la imagen de `selectedImageUri` sea copiada a este archivo
                // usando, por ejemplo, un flujo de entrada y salida.
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        } ?: Toast.makeText(context, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pickImageLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permiso de almacenamiento denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestPermissionsAndOpenGallery() {
        storagePermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    // Image picker from Camera
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            bitmapImage = bitmap

            // Crea el archivo y guarda el bitmap en él
            imageFile = File(context.cacheDir, "image.jpg").apply {
                outputStream().use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }

            // Asigna el URI del archivo creado a selectedImageUri para su uso
            selectedImageUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile!!,
            )
        } ?: Toast.makeText(context, "No se tomó ninguna foto", Toast.LENGTH_SHORT).show()
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permiso de la cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestPermissionsAndOpenCamera() {
        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
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
            Box(contentAlignment = Alignment.BottomEnd) {
                selectedImageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.size(128.dp),
                    )
                } ?: Image(
                    painter = painterResource(R.drawable.user_placeholder),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                )

                IconButton(onClick = { showSelectionPicker = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar imagen de perfil",
                    )
                }
            }

            // Campo de Correo
            CustomTextField(
                value = userEmail,
                onValueChange = onUserEmailChanged,
                labelId = R.string.email,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )

            // Campo de Contraseña
            CustomTextField(
                value = userPassword,
                onValueChange = onUserPasswordChanged,
                labelId = R.string.password,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
            )

            // Campo de Nombre y Apellido
            CustomTextField(
                value = userName,
                onValueChange = onUserNameChanged,
                labelId = R.string.name,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
            )

            // Botón Actualizar
            PrimaryButton(
                onClick = onUpdateButtonClicked,
                textId = R.string.update,
                modifier = Modifier.padding(top = 28.dp),
            )
        }
    }

    if (showSelectionPicker) {
        SelectionPicker(
            onCloseSelectionPicker = { showSelectionPicker = false },
            onOpenCamera = { requestPermissionsAndOpenCamera() },
            onOpenGallery = { requestPermissionsAndOpenGallery() },
        )
    }
}

@Composable
fun SelectionPicker(
    modifier: Modifier = Modifier,
    onCloseSelectionPicker: () -> Unit,
    onOpenCamera: () -> Unit,
    onOpenGallery: () -> Unit,
) {
    Surface(
        onClick = onCloseSelectionPicker,
        color = Color.Black.copy(alpha = 0.5f),
        modifier = modifier.fillMaxSize(),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(32.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = modifier,
                    ) {
                        Button(
                            onClick = {
                                onOpenCamera()
                                onCloseSelectionPicker()
                            },
                        ) {
                            Text("Tomar foto")
                        }

                        Button(
                            onClick = {
                                onOpenGallery()
                                onCloseSelectionPicker()
                            },
                        ) {
                            Text("Galería")
                        }
                    }
                }
            }
        }
    }
}

suspend fun uploadImageToSupabase(userName: String, imageFile: File) {
    try {
        val bucket = supabaseClient.storage.from("profile_images")
        val fileName = "${userName}.jpg"

        bucket.upload(fileName, imageFile) {
            upsert = true
        }
    } catch (e: Exception) {
        println("uploadImageToSupabase: Error: ${e.message}")
    }
}

@CustomPreview
@Composable
private fun MiPerfilScreenPreview() {
    MyApplicationTheme {
        MiPerfilScreen(
            imageUrl = null,
            userEmail = "",
            onUserEmailChanged = {},
            userPassword = "",
            onUserPasswordChanged = {},
            userName = "",
            onUserNameChanged = {},
            onUpdateButtonClicked = {},
        )
    }
}
