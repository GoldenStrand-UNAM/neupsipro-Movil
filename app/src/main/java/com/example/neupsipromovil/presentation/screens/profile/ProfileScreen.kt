package com.example.neupsipromovil.presentation.screens.profile

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.content.res.Configuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.neupsipromovil.presentation.common.atoms.AccessibilityButton
import com.example.neupsipromovil.presentation.common.molecules.HeaderIconButton
import com.example.neupsipromovil.presentation.common.molecules.profile.ProfileHeader
import com.example.neupsipromovil.presentation.common.organisms.AppointmentCard
import com.example.neupsipromovil.presentation.common.organisms.ClinicalInfoCard
import com.example.neupsipromovil.presentation.common.organisms.LogoutConfirmationModal
import com.example.neupsipromovil.presentation.common.organisms.MainAppBottomBar
import com.example.neupsipromovil.presentation.common.organisms.StaffMemberCard
import com.example.neupsipromovil.presentation.navegation.Screen
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ly.com.tahaben.showcase_layout_compose.model.Gravity
import ly.com.tahaben.showcase_layout_compose.model.ShowcaseMsg
import ly.com.tahaben.showcase_layout_compose.ui.ShowcaseLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// ── Paleta compartida con ForumScreen ─────────────────────────────────────────
private val BackgroundColor = Color(0xFFF5F6FA)   // mismo que foro
private val AccentBlue      = Color(0xFF3F51B5)   // mismo que foro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: String,
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state          by viewModel.state.collectAsState()
    val context        = LocalContext.current
    var showLogoutModal by remember { mutableStateOf(false) }
    var isShowcasing    by remember { mutableStateOf(false) }
    val lazyListState   = rememberLazyListState()
    val coroutineScope  = rememberCoroutineScope()
    var isRefreshing    by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape   = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
        if (isShowcasing) lazyListState.animateScrollToItem(index = 0)
    }

    LaunchedEffect(key1 = userId) {
        viewModel.getProfile(userId)
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
            text      = "Bienvenido a tu perfil. Presiona en cualquier lado para iniciar el recorrido.",
            textStyle = TextStyle(color = Color.White),
        ),
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh    = {
                coroutineScope.launch {
                    isRefreshing = true
                    viewModel.getProfile(userId)
                    delay(1500)
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                // ── TopAppBar igual que ForumScreen ───────────────────────────
                topBar = {
                    AnimatedVisibility(
                        visible = headerVisible,
                        enter   = expandVertically(),
                        exit    = shrinkVertically(),
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text       = "Mi Perfil",
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
                                    modifier           = Modifier.size(24.dp),
                                )
                                Spacer(modifier = Modifier.size(4.dp))
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
                    AccessibilityButton(onClick = {
                        Toast.makeText(context, "Funcionalidad por implementar", Toast.LENGTH_SHORT).show()
                    })
                },
                bottomBar = {
                    MainAppBottomBar(
                        currentScreen = "perfil",
                        onNavigate    = { screen ->
                            if (screen == "foro") {
                                navController.navigate(Screen.Forum.createRoute(userId)) {
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

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color    = AccentBlue,
                            )
                        }

                        state.error != null -> {
                            Text(
                                text     = "Error: ${state.error}",
                                modifier = Modifier.align(Alignment.Center),
                                color    = Color.Red,
                            )
                        }

                        state.user != null -> {
                            val user             = state.user!!
                            val hasAppointment   = user.nextAppointmentDate != null
                            val appointmentIndex = if (hasAppointment) 2 else -1
                            val staffIndex       = if (hasAppointment) 3 else 2

                            val translatedStage = when (user.stage?.lowercase(Locale.ROOT)) {
                                "evaluation"   -> "Evaluación"
                                "intervention" -> "Intervención"
                                "graduation"   -> "Graduación"
                                null           -> ""
                                else           -> user.stage
                            }

                            LazyColumn(
                                state          = lazyListState,
                                modifier       = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 24.dp),
                            ) {
                                // ── Avatar centrado bajo el TopAppBar ─────────
                                item {
                                    // Padding vertical reducido en landscape para no ocupar
                                    // demasiado espacio vertical con el avatar
                                    val headerPadding = if (isLandscape) 10.dp else 20.dp
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = AccentBlue,
                                                shape = RoundedCornerShape(
                                                    bottomStart = 28.dp,
                                                    bottomEnd   = 28.dp,
                                                ),
                                            )
                                            // En landscape usamos padding horizontal mayor para
                                            // centrar visualmente el contenido en pantalla ancha
                                            .padding(
                                                horizontal = if (isLandscape) 80.dp else 16.dp,
                                                vertical   = headerPadding,
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        ProfileHeader(
                                            fullName = user.fullName,
                                            image    = user.profilePhoto,
                                            stage    = translatedStage,
                                        )
                                    }
                                }

                                // ── Contenido principal ───────────────────────
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                horizontal = if (isLandscape) 80.dp else 16.dp,
                                            ),
                                    ) {
                                        Spacer(modifier = Modifier.height(16.dp))

                                        ClinicalInfoCard(
                                            age           = user.age,
                                            unitEntryDate = user.registrationDate,
                                            neuroEntryDate = user.neuroEntryDate,
                                            modifier = Modifier.showcase(
                                                index   = 1,
                                                message = ShowcaseMsg(
                                                    text      = "Aquí puedes revisar tu información clínica registrada.",
                                                    textStyle = TextStyle(color = Color.White),
                                                    gravity   = Gravity.Bottom,
                                                ),
                                            ),
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Text(
                                            text  = "Próximas citas",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF1A1A2E),   // TextPrimary igual que ForumPostCard
                                            modifier = Modifier.padding(bottom = 8.dp),
                                        )

                                        if (hasAppointment) {
                                            user.nextAppointmentDate?.let { dateString ->
                                                val datePart       = dateString.take(10)
                                                val date           = LocalDate.parse(datePart, DateTimeFormatter.ISO_LOCAL_DATE)
                                                val spanishFormatter = DateTimeFormatter.ofPattern("MMM", Locale("es", "MX"))
                                                val monthName      = date.format(spanishFormatter).uppercase().replace(".", "")
                                                val dayOfMonth     = date.dayOfMonth.toString().padStart(2, '0')
                                                AppointmentCard(
                                                    month    = monthName,
                                                    day      = dayOfMonth,
                                                    title    = "Cita próxima",
                                                    time     = user.nextAppointmentTime?.take(5) ?: "--:--",
                                                    modifier = Modifier.showcase(
                                                        index   = appointmentIndex,
                                                        message = ShowcaseMsg(
                                                            text      = "Esta es la fecha y hora de tu siguiente cita agendada.",
                                                            textStyle = TextStyle(color = Color.White),
                                                            gravity   = Gravity.Top,
                                                        ),
                                                    ),
                                                )
                                            }
                                        } else {
                                            Text(
                                                text     = "No tienes citas programadas.",
                                                style    = MaterialTheme.typography.bodyMedium,
                                                color    = Color(0xFF6B7280),  // TextSecondary igual que cards
                                                modifier = Modifier.padding(vertical = 8.dp),
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Box(
                                            modifier = Modifier.showcase(
                                                index   = staffIndex,
                                                message = ShowcaseMsg(
                                                    text      = "Aquí encuentras los especialistas asignados a tu caso.",
                                                    textStyle = TextStyle(color = Color.White),
                                                    gravity   = Gravity.Top,
                                                ),
                                            ),
                                        ) {
                                            Column {
                                                StaffMemberCard(roleTitle = "Psicólogo",  staffName = user.assignedClinic)
                                                Spacer(modifier = Modifier.height(12.dp))
                                                StaffMemberCard(roleTitle = "Prostesista", staffName = user.prosthetist)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(100.dp))
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
                                viewModel.logout(onSuccessLogout = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                })
                            },
                        )
                    }
                }
            }
        }
    }
}