package com.example.neupsipromovil.presentation.screens.forum

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ly.com.tahaben.showcase_layout_compose.model.Gravity
import ly.com.tahaben.showcase_layout_compose.model.ShowcaseMsg
import ly.com.tahaben.showcase_layout_compose.ui.ShowcaseLayout

private val BackgroundColor = Color(0xFFF5F6FA)
private val AccentBlue      = Color(0xFF3F51B5)
private val SurfaceWhite    = Color(0xFFFFFFFF)

// ── Entrada principal ─────────────────────────────────────────────────────────
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

// ── Contenido principal ───────────────────────────────────────────────────────
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
    var showLogoutModal by remember { mutableStateOf(false) }
    var isShowcasing    by remember { mutableStateOf(false) }
    val lazyListState   = rememberLazyListState()
    val coroutineScope  = rememberCoroutineScope()
    var isRefreshing    by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape   = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // En landscape: header+search se ocultan al bajar y reaparecen al subir
    var headerVisible by remember { mutableStateOf(true) }

    LaunchedEffect(lazyListState, isLandscape) {
        if (!isLandscape) {
            headerVisible = true
            return@LaunchedEffect
        }
        var prevIndex  = 0
        var prevOffset = 0
        snapshotFlow {
            lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                val scrollingDown = index > prevIndex ||
                        (index == prevIndex && offset > prevOffset + 8)
                val scrollingUp   = index < prevIndex ||
                        (index == prevIndex && offset < prevOffset - 8)
                if (scrollingDown && headerVisible)  headerVisible = false
                if (scrollingUp   && !headerVisible) headerVisible = true
                prevIndex  = index
                prevOffset = offset
            }
    }

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
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh    = {
                coroutineScope.launch {
                    isRefreshing = true
                    onRetry()
                    delay(1500)
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                topBar = {
                    AnimatedVisibility(
                        visible = headerVisible,
                        enter   = expandVertically(),
                        exit    = shrinkVertically(),
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text       = "Foro Neupsi-Pro",
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 20.sp,
                                    color      = Color.White,
                                )
                            },
                            actions = {
                                HeaderIconButton(
                                    icon               = Icons.AutoMirrored.Filled.HelpOutline,
                                    contentDescription = "Ayuda",
                                    onClick            = { isShowcasing = true },
                                    modifier           = Modifier
                                        .size(24.dp)
                                        .showcase(
                                            index   = 3,
                                            message = ShowcaseMsg(
                                                text      = "¿Necesitas ayuda? Toca aquí para iniciar el recorrido interactivo.",
                                                textStyle = TextStyle(color = Color.White),
                                                gravity   = Gravity.Bottom,
                                            ),
                                        ),
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                HeaderIconButton(
                                    icon               = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Cerrar sesión",
                                    onClick            = { showLogoutModal = true },
                                    modifier           = Modifier.size(24.dp),
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = AccentBlue,
                            ),
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick        = onCreatePost,
                        containerColor = AccentBlue,
                        contentColor   = Color.White,
                        shape          = CircleShape,
                        modifier       = Modifier.showcase(
                            index   = 4,
                            message = ShowcaseMsg(
                                text      = "¡Comparte tus ideas! Haz clic aquí para crear una nueva publicación en el foro.",
                                textStyle = TextStyle(color = Color.White),
                                gravity   = Gravity.Top,
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


                    AnimatedVisibility(
                        visible = headerVisible,
                        enter   = expandVertically(),
                        exit    = shrinkVertically(),
                    ) {
                        OutlinedTextField(
                            value         = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder   = {
                                Text(
                                    text     = "Buscar discusiones...",
                                    color    = Color(0xFFAAAAAA),
                                    fontSize = 14.sp,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector        = Icons.Default.Search,
                                    contentDescription = "Buscar",
                                    tint               = Color(0xFFAAAAAA),
                                    modifier           = Modifier.size(18.dp),
                                )
                            },
                            singleLine = true,
                            shape      = RoundedCornerShape(20.dp),
                            colors     = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor    = Color(0xFFE0E0E0),
                                focusedBorderColor      = AccentBlue,
                                unfocusedContainerColor = SurfaceWhite,
                                focusedContainerColor   = SurfaceWhite,
                            ),
                            // Fuente compacta dentro del campo
                            textStyle = TextStyle(fontSize = 14.sp),
                            modifier  = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .showcase(
                                    index   = 1,
                                    message = ShowcaseMsg(
                                        text      = "Usa el buscador para encontrar publicaciones por tema.",
                                        textStyle = TextStyle(color = Color.White),
                                        gravity   = Gravity.Bottom,
                                    ),
                                ),
                        )
                    }

                    // ── Lista de posts ────────────────────────────────────────────
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
                                        start  = 12.dp,
                                        end    = 12.dp,
                                        top    = 6.dp,
                                        bottom = 88.dp,
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    items(items = state.posts, key = { it.id }) { post ->
                                        ForumPostCard(
                                            post     = post,
                                            modifier = if (state.posts.indexOf(post) == 0) {
                                                Modifier.showcase(
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
        } // PullToRefreshBox
    }
}