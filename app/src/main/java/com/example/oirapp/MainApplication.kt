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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.oirapp.ui.BienvenidaScreen
import com.example.oirapp.viewmodel.MainViewModel

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
        title = {
            Text(stringResource(currentScreen.title!!))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
        //val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = MainApplication.Bienvenida.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = MainApplication.Bienvenida.name) {
                BienvenidaScreen(
                    onStartButtonClicked = {
                        viewModel.updateCurrentScreen(MainApplication.IniciarSesion)
                    },
                )
            }

            composable(route = MainApplication.IniciarSesion.name) {
                // Contenido de la pantalla de inicio de sesión
            }

            composable(route = MainApplication.CrearCuenta.name) {
                // Contenido de la pantalla de creación de cuenta
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
