package com.example.oirapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.oirapp.data.network.SupabaseClient.supabaseClient
import com.example.oirapp.ui.components.MenuCard
import com.example.oirapp.ui.components.MenuItem
import com.example.oirapp.ui.screens.BienvenidaScreen
import com.example.oirapp.ui.screens.ChatScreen
import com.example.oirapp.ui.screens.CrearCuentaScreen
import com.example.oirapp.ui.screens.GruposScreen
import com.example.oirapp.ui.screens.IniciarSesionScreen
import com.example.oirapp.ui.state.GroupState
import com.example.oirapp.ui.state.LoginState
import com.example.oirapp.ui.state.RegisterState
import com.example.oirapp.ui.state.UserUiState
import com.example.oirapp.ui.viewmodel.ChatViewModel
import com.example.oirapp.ui.viewmodel.GruposViewModel
import com.example.oirapp.ui.viewmodel.LoginViewModel
import com.example.oirapp.ui.viewmodel.NavigationViewModel
import com.example.oirapp.ui.viewmodel.RegisterViewModel
import io.github.jan.supabase.gotrue.auth
import kotlinx.serialization.json.jsonPrimitive

enum class MainApplication(var title: String? = null) {
    Bienvenida,
    IniciarSesion(title = "Iniciar sesión"),
    CrearCuenta(title = "Crear cuenta"),
    Grupos(title = "Grupos"),
    Chat(title = ""),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    currentScreen: MainApplication,
    navigationViewModel: NavigationViewModel,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            val title by navigationViewModel.title.observeAsState(currentScreen.title ?: "")

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
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
        actions = {
            if (currentScreen == MainApplication.Grupos) {
                IconButton(onClick = onMenuButtonClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.open_menu),
                    )
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
    navigationViewModel: NavigationViewModel = viewModel(),
    registerViewModel: RegisterViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    gruposViewModel: GruposViewModel = viewModel(),
    chatViewModel: ChatViewModel = viewModel(),
) {
    val currentScreen by navigationViewModel.currentScreen.observeAsState(MainApplication.Bienvenida)
    var showMenuCard by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val user = supabaseClient.auth.currentSessionOrNull()?.user

        if (user != null) {
            val userName = user.userMetadata?.get("nombre")?.jsonPrimitive?.content!!
            val userRole = user.userMetadata?.get("rol")?.jsonPrimitive?.content!!
            val userImageUrl = user.userMetadata?.get("imagen_url")?.jsonPrimitive?.content

            loginViewModel.updateUserUiState(
                UserUiState(
                    id = user.id,
                    name = userName,
                    role = userRole,
                    imageUrl = userImageUrl,
                )
            )

            navController.navigate(MainApplication.Grupos.name) {
                popUpTo(MainApplication.Bienvenida.name) { inclusive = true }
            }
            navigationViewModel.updateCurrentScreen(MainApplication.Grupos)
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen != MainApplication.Bienvenida) {
                MainAppBar(
                    currentScreen = currentScreen,
                    navigationViewModel = navigationViewModel,
                    canNavigateBack = false,
                    navigateUp = {},
                    onMenuButtonClick = { showMenuCard = true },
                )
            }
        },
        floatingActionButton = {
            if (currentScreen == MainApplication.Grupos) {
                FloatingActionButton(
                    onClick = {
                        if (loginViewModel.userUiState.value.role == "Docente") {
                            gruposViewModel.openDialog(GroupState.Create)
                        } else {
                            gruposViewModel.openDialog(GroupState.Join)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ) {
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
                val showDialog by loginViewModel.showDialog.observeAsState(false)

                IniciarSesionScreen(
                    userEmail = loginViewModel.userEmail,
                    onUserEmailChanged = { loginViewModel.updateUserEmail(it) },
                    userPassword = loginViewModel.userPassword,
                    onUserPasswordChanged = { loginViewModel.updateUserPassword(it) },
                    onLoginButtonClicked = {
                        if (loginViewModel.userPassword.isBlank()) {
                            loginViewModel.updateUserPassword("")
                        }

                        loginViewModel.signInWithEmail(
                            userEmail = loginViewModel.userEmail.trim(),
                            userPassword = loginViewModel.userPassword,
                        )
                    },
                    onRegisterTextClicked = {
                        navController.navigate(MainApplication.CrearCuenta.name)
                        navigationViewModel.updateCurrentScreen(MainApplication.CrearCuenta)
                    },
                    loginState = loginState,
                    showDialog = showDialog,
                    onDismissDialog = { loginViewModel.setShowDialog(false) },
                )

                LaunchedEffect(loginState) {
                    if (loginState is LoginState.Success) {
                        println((loginState as LoginState.Success).message)

                        navController.navigate(MainApplication.Grupos.name) {
                            popUpTo(MainApplication.IniciarSesion.name) { inclusive = true }
                        }
                        navigationViewModel.updateCurrentScreen(MainApplication.Grupos)
                    }
                }
            }

            composable(route = MainApplication.CrearCuenta.name) {
                val registerState by registerViewModel.registerState.observeAsState()
                val showDialog by registerViewModel.showDialog.observeAsState(false)

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

                DisposableEffect(Unit) {
                    onDispose {
                        navigationViewModel.updateCurrentScreen(MainApplication.IniciarSesion)
                        registerViewModel.resetData()
                    }
                }
            }

            composable(route = MainApplication.Grupos.name) {
                val userUiState by loginViewModel.userUiState.collectAsState()

                val groupState by gruposViewModel.groupState.observeAsState()
                val showDialog by gruposViewModel.showDialog.observeAsState(false)
                val teacherGroups by gruposViewModel.teacherGroups.collectAsState()
                val studentGroups by gruposViewModel.studentGroups.collectAsState()

                LaunchedEffect(Unit) {
                    if (userUiState.role == "Docente") {
                        gruposViewModel.getCreatedGroups(userUiState.id)
                    } else {
                        gruposViewModel.getJoinedGroups(userUiState.id)
                    }
                }

                GruposScreen(
                    userUiState = userUiState,
                    groupState = groupState,
                    groups = if (userUiState.role == "Docente") teacherGroups else studentGroups,
                    userInput = gruposViewModel.userInput,
                    onUserInputChanged = { gruposViewModel.updateUserInput(it) },
                    errorMessage = gruposViewModel.errorMessage,
                    showDialog = showDialog,
                    onDismissDialog = {
                        gruposViewModel.setShowDialog(false)
                        gruposViewModel.resetData()
                    },
                    onConfirmDialog = { userId ->
                        if (gruposViewModel.userInput.isBlank()) {
                            gruposViewModel.updateUserInput("")
                        }

                        if (userUiState.role == "Docente") {
                            if (groupState is GroupState.Create) {
                                gruposViewModel.createGroup(
                                    groupName = gruposViewModel.userInput.trim(),
                                    idDocente = userId,
                                )
                            }

                            if (groupState is GroupState.Edit) {
                                gruposViewModel.updateGroupName(
                                    groupId = gruposViewModel.groupId,
                                    newName = gruposViewModel.userInput.trim(),
                                    idDocente = userId,
                                )
                            }
                        }

                        if (userUiState.role == "Estudiante") {
                            if (groupState is GroupState.Join) {
                                gruposViewModel.joinGroup(
                                    accessCode = gruposViewModel.userInput.trim().uppercase(),
                                    idEstudiante = userId,
                                )
                            }
                        }
                    },
                    openEditDialog = { groupId, groupName ->
                        gruposViewModel.openDialog(GroupState.Edit)
                        gruposViewModel.updateGroupId(groupId)
                        gruposViewModel.updateUserInput(groupName)
                    },
                    onDeleteGroup = { groupId ->
                        gruposViewModel.deleteGroup(groupId, userUiState.id)
                    },
                    onGroupCardCLick = { groupName, groupId ->
                        navController.navigate("${MainApplication.Chat.name}/$groupName/$groupId")
                        navigationViewModel.updateCurrentScreen(MainApplication.Chat)
                        navigationViewModel.updateTitle(groupName)
                    },
                )
            }

            composable(
                route = "${MainApplication.Chat.name}/{groupName}/{groupId}",
                arguments = listOf(
                    navArgument("groupName") { type = NavType.StringType },
                    navArgument("groupId") { type = NavType.IntType },
                ),
            ) { backStackEntry ->
                val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
                val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
                val channelName = "messages_${groupName.replace(" ", "_").lowercase()}_$groupId"

                val userUiState by loginViewModel.userUiState.collectAsState()
                val messages by chatViewModel.messages.collectAsState()

                LaunchedEffect(Unit) {
                    chatViewModel.subscribeToChannel(channelName, groupId)
                    chatViewModel.getMessages(groupId)
                }

                DisposableEffect(Unit) {
                    onDispose {
                        chatViewModel.removeChannel(channelName)
                        navigationViewModel.updateCurrentScreen(MainApplication.Grupos)
                        navigationViewModel.updateTitle("Grupos")
                    }
                }

                ChatScreen(
                    messages = messages,
                    userMessage = chatViewModel.userMessage,
                    onUserMessageChanged = { chatViewModel.updateUserMessage(it) },
                    onSendMessage = { message ->
                        chatViewModel.insertMessage(
                            message = message,
                            groupId = groupId,
                            userId = userUiState.id,
                        )
                    },
                )
            }
        }
    }

    if (showMenuCard) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset(x = -48, y = 160),
            onDismissRequest = { showMenuCard = false },
        ) {
            MenuCard {
                MenuItem(
                    onClick = { /* TODO('Go to profile screen') */ },
                    icon = Icons.Default.AccountCircle,
                    textId = R.string.mi_cuenta,
                )

                MenuItem(
                    onClick = {
                        loginViewModel.signOut()

                        showMenuCard = false

                        navController.navigate(MainApplication.IniciarSesion.name) {
                            popUpTo(MainApplication.Grupos.name) { inclusive = true }
                        }
                        navigationViewModel.updateCurrentScreen(MainApplication.IniciarSesion)
                    },
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    textId = R.string.cerrar_sesion,
                )
            }
        }
    }
}
