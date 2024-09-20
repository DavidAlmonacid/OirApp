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
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oirapp.ui.screens.BienvenidaScreen
import com.example.oirapp.ui.screens.CrearCuentaScreen
import com.example.oirapp.ui.screens.GruposDocenteScreen
import com.example.oirapp.ui.screens.GruposEstudianteScreen
import com.example.oirapp.ui.screens.IniciarSesionScreen
import com.example.oirapp.ui.viewmodel.LoginViewModel
import com.example.oirapp.ui.viewmodel.NavigationViewModel
import com.example.oirapp.ui.viewmodel.RegisterViewModel

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
    navigationViewModel: NavigationViewModel = viewModel(),
    registerViewModel: RegisterViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        topBar = {
            val currentScreen by navigationViewModel.currentScreen.observeAsState(MainApplication.Bienvenida)

            if (currentScreen != MainApplication.Bienvenida) {
                MainAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = false,
                    navigateUp = {},
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainApplication.Bienvenida.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = MainApplication.Bienvenida.name) {
                BienvenidaScreen(
                    onStartButtonClicked = {
                        navController.navigate(MainApplication.IniciarSesion.name)
                        navigationViewModel.updateCurrentScreen(MainApplication.IniciarSesion)
                    },

                    /*
                     * TODO: Al darle al botón de "Inicio", sacar la pantalla actual de la pila de navegación.
                     *  Luego de esto, navegar a la pantalla de "IniciarSesion" y actualizar el estado de la pantalla actual.
                     */
                )
            }

            composable(route = MainApplication.IniciarSesion.name) {
                val loginState by loginViewModel.loginState.observeAsState()

                IniciarSesionScreen(
                    userEmail = loginViewModel.userEmail,
                    onUserEmailChanged = { loginViewModel.updateUserEmail(it) },
                    userPassword = loginViewModel.userPassword,
                    onUserPasswordChanged = { loginViewModel.updateUserPassword(it) },
                    onLoginButtonClicked = { email, password ->
                        loginViewModel.signInWithEmail(email, password)
                    },
                    onRegisterTextClicked = {
                        navController.navigate(MainApplication.CrearCuenta.name)
                        navigationViewModel.updateCurrentScreen(MainApplication.CrearCuenta)
                    },
                    loginState = loginState,
                    onNavigateToGroups = { route ->
                        navController.navigate(route)
                        navigationViewModel.updateCurrentScreen(
                            if (route.contains(MainApplication.GruposEstudiante.name)) {
                                MainApplication.GruposEstudiante
                            } else {
                                MainApplication.GruposDocente
                            }
                        )
                    },
                )

                /*
                 * TODO: Al navegar a la pantalla de "GruposEstudiante" o "GruposDocente",
                 *  quitar todas las pantallas de la pila de navegación excepto la pantalla actual.
                 */
            }

            composable(route = MainApplication.CrearCuenta.name) {
                val registerState by registerViewModel.registerState.observeAsState()
                val showSuccessDialog by registerViewModel.showSuccessDialog.observeAsState(false)
                val showErrorDialog by registerViewModel.showErrorDialog.observeAsState(false)

                CrearCuentaScreen(
                    userEmail = registerViewModel.userEmail,
                    onUserEmailChanged = { registerViewModel.updateUserEmail(it) },
                    userPassword = registerViewModel.userPassword,
                    onUserPasswordChanged = { registerViewModel.updateUserPassword(it) },
                    userName = registerViewModel.userName,
                    onUserNameChanged = { registerViewModel.updateUserName(it) },
                    userRole = registerViewModel.userRole,
                    onUserRoleChanged = { registerViewModel.updateUserRole(it) },
                    onRegisterButtonClicked = {
                        if (registerViewModel.userPassword.isBlank()) {
                            registerViewModel.updateUserPassword("")
                        }

                        registerViewModel.createAccount(
                            userEmail = registerViewModel.userEmail.trim(),
                            userPassword = registerViewModel.userPassword,
                            userName = registerViewModel.userName.trim(),
                            userRole = registerViewModel.userRole,
                        )
                    },
                    registerState = registerState,
                    showSuccessDialog = showSuccessDialog,
                    onDismissSuccessDialog = {
                        registerViewModel.setShowSuccessDialog(false)

                        navController.navigate(MainApplication.IniciarSesion.name) {
                            popUpTo(MainApplication.CrearCuenta.name) { inclusive = true }
                        }
                        navigationViewModel.updateCurrentScreen(MainApplication.IniciarSesion)

                        registerViewModel.resetData()
                    },
                    showErrorDialog = showErrorDialog,
                    onDismissErrorDialog = { registerViewModel.setShowErrorDialog(false) },
                )

                /*
                 * TODO: Al realizar la acción de retroceso, actualizar el estado de la pantalla actual.
                 */
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
