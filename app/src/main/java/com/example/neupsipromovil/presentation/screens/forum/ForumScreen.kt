package com.example.neupsipromovil.presentation.screens.forum


import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.neupsipromovil.presentation.common.molecules.HeaderIconButton
import com.example.neupsipromovil.presentation.common.organisms.LogoutConfirmationModal
import com.example.neupsipromovil.presentation.common.organisms.MainAppBottomBar
import com.example.neupsipromovil.presentation.navegation.Screen
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.ForumEmptyState
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.ForumErrorState
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.ForumLoadingState
import kotlinx.coroutines.launch
import ly.com.tahaben.showcase_layout_compose.model.Gravity
import ly.com.tahaben.showcase_layout_compose.model.ShowcaseMsg
import ly.com.tahaben.showcase_layout_compose.ui.ShowcaseLayout

private val BackgroundColor = Color(0xFFF5F6FA)
private val AccentBlue      = Color(0xFF3F51B5)
private val SurfaceWhite    = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    navController: NavHostController,
    userId: String,
    viewModel: ForumViewModel = hiltViewModel(),
) {
    val uiState     by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val postCreated by navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("post_created", false)
        ?.collectAsState() ?: return

    LaunchedEffect(postCreated) {
        if (postCreated) {
            viewModel.loadPosts()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("post_created", false)
        }
    }

    ForumScreenContent(
        uiState             = uiState,
        searchQuery         = searchQuery,
        userId              = userId,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onRetry             = viewModel::retry,
        onCreatePost        = { navController.navigate(Screen.CreatePost.createRoute(userId)) },
        onNavigateToProfile = {
            navController.navigate(Screen.Profile.createRoute(userId)) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState    = true
            }
        },
        onLogout = {
            viewModel.logout(onSuccessLogout = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        },
    )
}

// ── Overload para previews / tests ────────────────────────────────────────────
@Composable
fun ForumScreenContent(viewModel: ForumViewModel) {
    val uiState     by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    ForumScreenContent(
        uiState             = uiState,
        searchQuery         = searchQuery,
        userId              = "",
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onRetry             = viewModel::retry,
        onCreatePost        = {},
        onNavigateToProfile = {},
        onLogout            = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreenContent(
    uiState: ForumUiState,
    searchQuery: String,
    userId: String = "",
    onSearchQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onCreatePost: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    // ── Estado local ──────────────────────────────────────────────────────────
    var showLogoutModal by remember { mutableStateOf(false) }
    var isShowcasing    by remember { mutableStateOf(false) }
    val lazyListState   = rememberLazyListState()
    val coroutineScope  = rememberCoroutineScope()

    LaunchedEffect(isShowcasing) {
        if (isShowcasing) lazyListState.animateScrollToItem(0)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    ShowcaseLayout(
        isShowcasing = isShowcasing,
        onFinish = {
            isShowcasing = false
            coroutineScope.launch { lazyListState.scrollToItem(0) }
        },
        greeting = ShowcaseMsg(
            text      = "Bienvenido al Foro. Presiona en cualquier lado para iniciar el recorrido.",
            textStyle = TextStyle(color = Color.White),
        ),
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick        = onCreatePost,
                    containerColor = AccentBlue,
                    contentColor   = Color.White,
                    shape          = CircleShape,
                    modifier       = Modifier.showcase(       // 👈 paso 3 del tutorial agregado
                        index   = 3,
                        message = ShowcaseMsg(
                            text      = "¡Comparte tus ideas! Haz clic aquí para crear una nueva publicación en el foro.",
                            textStyle = TextStyle(color = Color.White),
                            gravity   = Gravity.Top,          // Apunta hacia arriba al estar en la esquina inferior
                        ),
                    ),
                ) {
                    Icon(
                        imageVector        = Icons.Default.Add,
                        contentDescription = "Nueva publicación",
                    )
                }
            },
            bottomBar = {
                MainAppBottomBar(
                    currentScreen = "foro",
                    onNavigate    = { screen ->
                        if (screen == "perfil") onNavigateToProfile()
                    },
                )
            },
            containerColor = BackgroundColor,
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF3F50B4)),
                ) {
                    Spacer(modifier = Modifier.height(statusBarPadding.calculateTopPadding()))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .offset(y = (-20).dp)              // 👈 empuja hacia arriba a la fuerza
                            .padding(horizontal = 16.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text       = "Foro Neupsi-Pro",
                            fontWeight = FontWeight.Bold,
                            fontSize   = 32.sp,
                            color      = Color.White,
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment     = Alignment.CenterVertically,
                        ) {
                            HeaderIconButton(
                                icon               = Icons.AutoMirrored.Filled.HelpOutline,
                                contentDescription = "Ayuda",
                                onClick            = { isShowcasing = true },
                                modifier           = Modifier.size(32.dp),
                            )
                            HeaderIconButton(
                                icon               = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Cerrar sesión",
                                onClick            = { showLogoutModal = true },
                                modifier           = Modifier.size(32.dp),
                            )
                        }
                    }
                }

                // ── Buscador ──────────────────────────────────────────────────
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder   = {
                        Text("Buscar discusiones ...", color = Color(0xFFAAAAAA), fontSize = 15.sp)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector        = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint               = Color(0xFFAAAAAA),
                        )
                    },
                    singleLine = true,
                    shape      = RoundedCornerShape(24.dp),
                    colors     = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor    = Color(0xFFE0E0E0),
                        focusedBorderColor      = AccentBlue,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedContainerColor   = SurfaceWhite,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .showcase(                               // 👈 paso 1 del tutorial
                            index   = 1,
                            message = ShowcaseMsg(
                                text      = "Usa el buscador para encontrar publicaciones por tema.",
                                textStyle = TextStyle(color = Color.White),
                                gravity   = Gravity.Bottom,
                            ),
                        ),
                )

                // ── Contenido principal ───────────────────────────────────────
                when (val state = uiState) {
                    is ForumUiState.Loading -> ForumLoadingState()
                    is ForumUiState.Error   -> ForumErrorState(
                        message = state.message,
                        onRetry = onRetry,
                    )
                    is ForumUiState.Success -> {
                        if (state.posts.isEmpty()) {
                            ForumEmptyState(query = searchQuery)
                        } else {
                            LazyColumn(
                                state               = lazyListState,
                                contentPadding      = PaddingValues(
                                    start  = 16.dp,
                                    end    = 16.dp,
                                    top    = 4.dp,
                                    bottom = 88.dp,
                                ),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                items(items = state.posts, key = { it.id }) { post ->
                                    ForumPostCard(
                                        post     = post,
                                        modifier = if (state.posts.indexOf(post) == 0) {
                                            Modifier.showcase(  // 👈 paso 2 del tutorial
                                                index   = 2,
                                                message = ShowcaseMsg(
                                                    text      = "Cada tarjeta muestra una publicación del foro. Tócala para ver más.",
                                                    textStyle = TextStyle(color = Color.White),
                                                    gravity   = Gravity.Bottom,
                                                ),
                                            )
                                        } else Modifier,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Modal de cerrar sesión ────────────────────────────────────────
            if (showLogoutModal) {
                LogoutConfirmationModal(
                    onDismiss = { showLogoutModal = false },
                    onConfirm = {
                        showLogoutModal = false
                        onLogout()
                    },
                )
            }
        }
    }
}