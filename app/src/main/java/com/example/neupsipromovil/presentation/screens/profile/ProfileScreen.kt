package com.example.neupsipromovil.presentation.screens.profile

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun ProfileScreen(
    userId: String,
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showLogoutModal by remember { mutableStateOf(false) }
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

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    Scaffold(
        floatingActionButton = {
            AccessibilityButton(onClick = {
                Toast.makeText(context, "Funcionalidad por implementar", Toast.LENGTH_SHORT).show()
            })
        },
        bottomBar = {
            MainAppBottomBar(
                currentScreen = "perfil",
                onNavigate = { screen ->
                    if (screen == "foro") {
                        navController.navigate(Screen.Forum.createRoute(userId)) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5)),
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red,
                    )
                }

                state.user != null -> {
                    val user = state.user!!

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp),
                    ) {
                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = Color(0xFF3F51B5),
                                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                                        ).padding(top = statusBarPadding.calculateTopPadding())
                                        .padding(horizontal = 16.dp, vertical = 24.dp),
                            ) {
                                ProfileHeader(
                                    fullName = user.fullName,
                                    image = user.profilePhoto,
                                    stage = user.stage,
                                )
                                Row(
                                    modifier =
                                        Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(top = 0.dp, end = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    HeaderIconButton(
                                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                                        contentDescription = "Ayuda",
                                        onClick = {
                                            Toast.makeText(context, "Funcionalidad por implementar", Toast.LENGTH_SHORT).show()
                                        },
                                    )
                                    HeaderIconButton(
                                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Cerrar sesión",
                                        onClick = { showLogoutModal = true },
                                    )
                                }
                            }
                        }
                        item {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))

                                ClinicalInfoCard(
                                    age = user.age,
                                    unitEntryDate = user.registrationDate,
                                    neuroEntryDate = user.neuroEntryDate,
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Próximas citas",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                )
                                user.nextAppointmentDate?.let { dateString ->
                                    val datePart = dateString.take(10)
                                    val date = LocalDate.parse(datePart, DateTimeFormatter.ISO_LOCAL_DATE)
                                    val monthName =
                                        date.month
                                            .getDisplayName(
                                                java.time.format.TextStyle.SHORT,
                                                Locale("es", "MX"),
                                            ).uppercase()
                                            .replace(".", "")
                                    val dayOfMonth = date.dayOfMonth.toString().padStart(2, '0')
                                    AppointmentCard(
                                        month = monthName,
                                        day = dayOfMonth,
                                        title = "Cita proxima",
                                        time = user.nextAppointmentTime?.take(5) ?: "--:--",
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                StaffMemberCard(roleTitle = "Psicólogo", staffName = user.assignedClinic)
                                Spacer(modifier = Modifier.height(16.dp))
                                StaffMemberCard(roleTitle = "Prostesista", staffName = user.prosthetist)
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
