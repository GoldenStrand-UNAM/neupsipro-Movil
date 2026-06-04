package com.example.neupsipromovil.presentation.screens.forum

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.neupsipromovil.presentation.common.organisms.MainAppBottomBar
import com.example.neupsipromovil.presentation.navegation.Screen

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
        ?.collectAsState()
        ?: return

    LaunchedEffect(postCreated) {
        if (postCreated) {
            viewModel.loadPosts()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("post_created", false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "Foro",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 22.sp,
                        color      = Color(0xFF1A1A2E),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { navController.navigate(Screen.CreatePost.createRoute(userId)) },
                containerColor = AccentBlue,
                contentColor   = Color.White,
                shape          = CircleShape,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva publicación")
            }
        },
        bottomBar = {
            MainAppBottomBar(
                currentScreen = "foro",
                onNavigate = { screen ->
                    if (screen == "perfil") {
                        navController.navigate(Screen.Profile.createRoute(userId)) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
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
            OutlinedTextField(
                value         = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder   = {
                    Text(text = "Buscar discusiones ...", color = Color(0xFFAAAAAA), fontSize = 15.sp)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFFAAAAAA))
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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )

            when (val state = uiState) {
                is ForumUiState.Loading -> ForumLoadingState()
                is ForumUiState.Error   -> ForumErrorState(
                    message = state.message,
                    onRetry = viewModel::retry,
                )
                is ForumUiState.Success -> {
                    if (state.posts.isEmpty()) {
                        ForumEmptyState(query = searchQuery)
                    } else {
                        LazyColumn(
                            contentPadding      = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 88.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(items = state.posts, key = { it.id }) { post ->
                                ForumPostCard(post = post)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForumLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AccentBlue)
    }
}

@Composable
fun ForumErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier              = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier         = Modifier.size(72.dp).background(Color(0xFFE8EAF6), shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = Icons.Outlined.WifiOff, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(36.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Algo salió mal", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A2E))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message, fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = AccentBlue), shape = RoundedCornerShape(12.dp)) {
            Text("Reintentar", color = Color.White)
        }
    }
}

@Composable
fun ForumEmptyState(query: String) {
    Column(
        modifier              = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier         = Modifier.size(72.dp).background(Color(0xFFE8EAF6), shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = Icons.Outlined.SentimentDissatisfied, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(36.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text       = if (query.isBlank()) "No hay publicaciones" else "Sin resultados",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF1A1A2E),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text      = if (query.isBlank()) "Sé el primero en publicar algo en el foro." else "No encontramos publicaciones para \"$query\".",
            fontSize  = 14.sp,
            color     = Color.Gray,
            textAlign = TextAlign.Center,
        )
    }
}