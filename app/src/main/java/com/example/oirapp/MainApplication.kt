package com.example.oirapp

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.oirapp.ui.screens.BienvenidaScreen
import com.example.oirapp.ui.screens.CrearCuentaScreen
import com.example.oirapp.ui.screens.GruposScreen
import com.example.oirapp.ui.screens.IniciarSesionScreen
import com.example.oirapp.ui.state.LoginState
import com.example.oirapp.ui.state.RegisterState
import com.example.oirapp.ui.viewmodel.GruposViewModel
import com.example.oirapp.ui.viewmodel.LoginViewModel
import com.example.oirapp.ui.viewmodel.NavigationViewModel
import com.example.oirapp.ui.viewmodel.RegisterViewModel

enum class MainApplication(@StringRes val title: Int? = null) {
    Bienvenida,
    IniciarSesion(title = R.string.iniciar_sesion),
    CrearCuenta(title = R.string.crear_cuenta),
    Grupos(title = R.string.grupos),
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
    gruposViewModel: GruposViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val currentScreen by navigationViewModel.currentScreen.observeAsState(MainApplication.Bienvenida)

    Scaffold(
        topBar = {
            if (currentScreen != MainApplication.Bienvenida) {
                MainAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = false,
                    navigateUp = {},
                )
            }
        },
        floatingActionButton = {
            if (currentScreen == MainApplication.Grupos) {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
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
                        navController.navigate(MainApplication.IniciarSesion.name) {
                            popUpTo(MainApplication.Bienvenida.name) { inclusive = true }
                        }
                        navigationViewModel.updateCurrentScreen(MainApplication.IniciarSesion)
                    },
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
                )

                LaunchedEffect(loginState) {
                    val currentState = loginState

                    if (currentState is LoginState.Success) {
                        val route = "${MainApplication.Grupos.name}/${currentState.name}/${currentState.role}/${currentState.imageUrl}"

                        navController.navigate(route) {
                            popUpTo(MainApplication.IniciarSesion.name) { inclusive = true }
                        }
                        navigationViewModel.updateCurrentScreen(MainApplication.Grupos)
                    }

                    if (currentState is LoginState.Error) {

                        println("Error: ${currentState.message}")

                    }
                }

                /*
                 * TODO: Al navegar a la pantalla de "GruposEstudiante" o "GruposDocente",
                 *  quitar todas las pantallas de la pila de navegación excepto la pantalla actual.
                 */
            }

            composable(route = MainApplication.CrearCuenta.name) {
                val registerState by registerViewModel.registerState.observeAsState()
                val showDialog by registerViewModel.showDialog.observeAsState(false)

                DisposableEffect(Unit) {
                    onDispose {
                        navigationViewModel.updateCurrentScreen(MainApplication.IniciarSesion)
                        registerViewModel.resetData()
                    }
                }

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
                    showDialog = showDialog,
                    onDismissDialog = {
                        registerViewModel.setShowDialog(false)

                        if (registerState is RegisterState.Success) {
                            navController.navigate(MainApplication.IniciarSesion.name) {
                                popUpTo(MainApplication.CrearCuenta.name) { inclusive = true }
                            }
                            navigationViewModel.updateCurrentScreen(MainApplication.IniciarSesion)

                            registerViewModel.resetData()
                        }
                    },
                )
            }

            composable(
                route = "${MainApplication.Grupos.name}/{userName}/{userRole}/{userImageUrl}",
                arguments = listOf(
                    navArgument("userName") { type = NavType.StringType },
                    navArgument("userRole") { type = NavType.StringType },
                    navArgument("userImageUrl") { type = NavType.StringType },
                )
            ) {
                val showDialog by gruposViewModel.showDialog.observeAsState(false)

                val userName = it.arguments?.getString("userName") ?: ""
                val userRole = it.arguments?.getString("userRole") ?: ""
                val userImageUrl = it.arguments?.getString("userImageUrl") ?: ""

                GruposScreen(
                    userName = userName,
                    userRole = userRole,
                    userImageUrl = userImageUrl,
                    //gruposViewModel = gruposViewModel,
                    showDialog = showDialog,
                    onDismissDialog = { gruposViewModel.setShowDialog(false) },
                )
            }
        }
    }
}
