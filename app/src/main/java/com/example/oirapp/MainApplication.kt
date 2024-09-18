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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oirapp.ui.BienvenidaScreen
import com.example.oirapp.ui.CrearCuentaScreen
import com.example.oirapp.ui.GruposDocenteScreen
import com.example.oirapp.ui.GruposEstudianteScreen
import com.example.oirapp.ui.IniciarSesionScreen
import com.example.oirapp.ui.LoginState
import com.example.oirapp.ui.MainViewModel

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
            Text(
                text = stringResource(currentScreen.title!!),
                fontWeight = FontWeight.Bold,
            )
        },
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
        //val userUiState by viewModel.userUiState.collectAsState()
        val loginState by viewModel.loginState.observeAsState()

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
                    userEmail = viewModel.userEmailLogin,
                    onUserEmailChanged = { viewModel.updateUserEmailLogin(it) },
                    userPassword = viewModel.userPasswordLogin,
                    onUserPasswordChanged = { viewModel.updateUserPasswordLogin(it) },
                    onLoginButtonClicked = { email, password ->
                        viewModel.signInWithEmail(email, password)
                    },
                    onRegisterTextClicked = {
                        navController.navigate(MainApplication.CrearCuenta.name)
                        viewModel.updateCurrentScreen(MainApplication.CrearCuenta)
                    },
                )

                LaunchedEffect(loginState) {
                    when (loginState) {
                        is LoginState.Success -> {
                            val state = loginState as LoginState.Success
                            val route = if (state.role == "Estudiante") {
                                "${MainApplication.GruposEstudiante.name}/${state.name}/${state.role}/${state.imageUrl}"
                            } else {
                                "${MainApplication.GruposDocente.name}/${state.name}/${state.role}/${state.imageUrl}"
                            }

                            navController.navigate(route)
                            viewModel.updateCurrentScreen(
                                if (state.role == "Estudiante") MainApplication.GruposEstudiante else MainApplication.GruposDocente
                            )
                        }
                        is LoginState.Error -> {
                            // Mostrar mensaje de error
                        }
                        else -> {}
                    }
                }
            }

            composable(route = MainApplication.CrearCuenta.name) {
                val showSuccessDialog by viewModel.showSuccessDialog.observeAsState(false)

                CrearCuentaScreen(
                    userEmail = viewModel.userEmail,
                    onUserEmailChanged = { viewModel.updateUserEmail(it) },
                    userPassword = viewModel.userPassword,
                    onUserPasswordChanged = { viewModel.updateUserPassword(it) },
                    userName = viewModel.userName,
                    onUserNameChanged = { viewModel.updateUserName(it) },
                    userRole = viewModel.userRole,
                    onUserRoleChanged = { viewModel.updateUserRole(it) },
                    onRegisterButtonClicked = {
                        if (viewModel.userPassword.isBlank()) {
                            viewModel.updateUserPassword("")
                        }

                        viewModel.createAccount(
                            userEmail = viewModel.userEmail.trim(),
                            userPassword = viewModel.userPassword,
                            userName = viewModel.userName.trim(),
                            userRole = viewModel.userRole,
                            onSuccess = { viewModel.setShowSuccessDialog(true) },
                            onError = { errorMessage ->
                                // Mostrar mensaje de error
                            },
                        )
                    },
                    showSuccessDialog = showSuccessDialog,
                    onDismissSuccessDialog = {
                        navController.navigate(MainApplication.IniciarSesion.name) {
                            popUpTo(MainApplication.CrearCuenta.name) { inclusive = true }
                        }
                        viewModel.updateCurrentScreen(MainApplication.IniciarSesion)

                        viewModel.setShowSuccessDialog(false)
                        viewModel.resetData()
                    },
                )
            }

            composable(
                route = "${MainApplication.GruposEstudiante.name}/{userName}/{userRole}/{userImageUrl}",
                arguments = listOf(
                    navArgument("userName") { type = NavType.StringType },
                    navArgument("userRole") { type = NavType.StringType },
                    navArgument("userImageUrl") { type = NavType.StringType },
                )
            ) {
                val userName = it.arguments?.getString("userName") ?: ""
                val userRole = it.arguments?.getString("userRole") ?: ""
                val userImageUrl = it.arguments?.getString("userImageUrl") ?: ""

                GruposEstudianteScreen()
            }

            composable(
                route = "${MainApplication.GruposDocente.name}/{userName}/{userRole}/{userImageUrl}",
                arguments = listOf(
                    navArgument("userName") { type = NavType.StringType },
                    navArgument("userRole") { type = NavType.StringType },
                    navArgument("userImageUrl") { type = NavType.StringType },
                )
            ) {
                val userName = it.arguments?.getString("userName") ?: ""
                val userRole = it.arguments?.getString("userRole") ?: ""
                val userImageUrl = it.arguments?.getString("userImageUrl") ?: ""

                GruposDocenteScreen(userName, userRole, userImageUrl)
            }
        }
    }
}
