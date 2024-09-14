package com.example.oirapp

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.oirapp.ui.BienvenidaScreen
import com.example.oirapp.ui.CrearCuentaScreen
import com.example.oirapp.ui.IniciarSesionScreen
import com.example.oirapp.ui.MainViewModel
import com.example.oirapp.ui.components.CustomFamilyText

enum class MainApplication(@StringRes val title: Int? = null) {
    Bienvenida,
    IniciarSesion(title = R.string.iniciar_sesion),
    CrearCuenta(title = R.string.crear_cuenta),
    GruposEstudiante(title = R.string.grupos),
    GruposDocente(title = R.string.grupos),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    currentScreen: MainApplication,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { CustomFamilyText(textId = currentScreen.title!!, fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                    )
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
fun MainApp(
    viewModel: MainViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        topBar = {
            val currentScreen by viewModel.currentScreen.observeAsState(MainApplication.Bienvenida)

            if (currentScreen != MainApplication.Bienvenida) {
                MainAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = false,
                    navigateUp = {},
                )
            }
        },
    ) { innerPadding ->
        val userUiState by viewModel.userUiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = MainApplication.Bienvenida.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = MainApplication.Bienvenida.name) {
                BienvenidaScreen(
                    onStartButtonClicked = {
                        navController.navigate(MainApplication.IniciarSesion.name)
                        viewModel.updateCurrentScreen(MainApplication.IniciarSesion)
                    },
                )
            }

            composable(route = MainApplication.IniciarSesion.name) {
                IniciarSesionScreen(
                    onLoginButtonClicked = { email, password ->
                        //
                    },
                    onRegisterTextClicked = {
                        navController.navigate(MainApplication.CrearCuenta.name)
                        viewModel.updateCurrentScreen(MainApplication.CrearCuenta)
                    },
                )
            }

            composable(route = MainApplication.CrearCuenta.name) {
                CrearCuentaScreen(
                    userEmail = viewModel.userEmail,
                    onUserEmailChanged = { viewModel.updateUserEmail(it) },
                    userPassword = viewModel.userPassword,
                    onUserPasswordChanged = { viewModel.updateUserPassword(it) },
                    userName = viewModel.userName,
                    onUserNameChanged = { viewModel.updateUserName(it) },
                    userRol = viewModel.userRol,
                    onUserRolChanged = { viewModel.updateUserRol(it) },
                    onRegisterButtonClicked = {
                        viewModel.createAccount(
                            userEmail = userUiState.email,
                            userPassword = userUiState.password,
                            userName = userUiState.name,
                            userRol = userUiState.role,
                            onSuccess = {
//                                navController.navigate(MainApplication.GruposEstudiante.name)
//                                viewModel.updateCurrentScreen(MainApplication.GruposEstudiante)
                            },
                            onError = { error ->
                                // Mostrar mensaje de error
                            },
                        )
                    },
                )
            }

            composable(route = MainApplication.GruposEstudiante.name) {
                // Contenido de la pantalla de grupos para estudiantes
            }

            composable(route = MainApplication.GruposDocente.name) {
                // Contenido de la pantalla de grupos para docentes
            }
        }
    }
}
