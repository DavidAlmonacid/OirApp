package com.example.oirapp.ui.screens

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.oirapp.R
import com.example.oirapp.data.network.SupabaseClient
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.components.CustomTextField
import com.example.oirapp.ui.components.PrimaryButton
import com.example.oirapp.ui.theme.MyApplicationTheme
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    userRole: String,
    onUserRoleChanged: (String) -> Unit,
    onUpdateButtonClicked: () -> Unit,
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    // URI donde se almacenará la foto tomada con la cámara
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // ActivityResultLauncher para tomar una foto con la cámara
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let { uri ->
                selectedImageUri = uri
                //uploadImageToSupabase(context, uri)
            }
        }
    }

    // ActivityResultLauncher para seleccionar una imagen de la galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            //uploadImageToSupabase(context, it)
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

//    ProfileImagePicker(onImagePicked = { uri ->
//        selectedImageUri = uri
//    })

//    selectedImageUri?.let { uri ->
//        Image(
//            painter = rememberAsyncImagePainter(uri),
//            contentDescription = "Imagen de perfil",
//            modifier = Modifier.size(128.dp),
//        )
//    }

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
//            // Imagen de usuario
//            Image(
//                painter = painterResource(R.drawable.user_placeholder),
//                contentDescription = null, // No es necesario describir la imagen de usuario
//                modifier = Modifier
//                    .size(100.dp)
//                    .padding(bottom = 16.dp),
//            )
            // Imagen de usuario con botón de edición
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

                IconButton(onClick = { openImagePicker(openCamera, openGallery) }) {
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

            // Campo de Rol
            CustomTextField(
                value = userRole,
                onValueChange = onUserRoleChanged,
                labelId = R.string.select_role,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { onUpdateButtonClicked() }),
            )

            // Botón Actualizar
            PrimaryButton(
                onClick = onUpdateButtonClicked,
                textId = R.string.update,
                modifier = Modifier.padding(top = 28.dp),
            )
        }
    }
}

// Función para abrir el selector de imagen
@Composable
fun openImagePicker(openCamera: () -> Unit, openGallery: () -> Unit) {
    // Mostrar un diálogo para que el usuario elija entre abrir la cámara o la galería
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Seleccionar imagen") },
        text = { Text("Elige una opción para seleccionar la imagen de perfil") },
        confirmButton = {
            Button(onClick = { openCamera() }) {
                Text("Tomar foto")
            }
        },
        dismissButton = {
            Button(onClick = { openGallery() }) {
                Text("Seleccionar desde galería")
            }
        }
    )
}

fun createImageFile(context: Context): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timestamp}_",
        ".jpg",
        storageDir,
    )
}


suspend fun uploadImageToSupabase(context: Context, uri: Uri) {
    val supabaseClient = SupabaseClient.create("https://your-project.supabase.co", "your-anon-key")
    val inputStream = context.contentResolver.openInputStream(uri)
    val fileName = "profile_pictures/${System.currentTimeMillis()}.jpg"

    inputStream?.let {
         supabaseClient.storage.from("your-bucket").upload(fileName, it)
             .thenAccept { response ->
                 if (response.error == null) {
                     // Handle successful upload
                 } else {
                     // Handle error
                 }
             }
     }
    /*bucket.upload(fileName, imageFile){
        upsert = true
    }*/
}

@Preview
@Composable
private fun MiPerfilScreenPreview(){
    MyApplicationTheme {
        MiPerfilScreen(
            imageUrl = null,
            userEmail = "",
            onUserEmailChanged = {},
            userPassword = "",
            onUserPasswordChanged = {},
            userName = "",
            onUserNameChanged = {},
            userRole = "",
            onUserRoleChanged = {},
            onUpdateButtonClicked = {},
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

    // Función para abrir el selector de imagen
    fun openImagePicker() {
        // Aquí puedes mostrar un diálogo para que el usuario elija entre abrir la cámara o la galería
        // Por simplicidad, aquí se abre directamente la galería
        openGallery()
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

