package com.example.neupsipromovil.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.neupsipromovil.presentation.common.atoms.AccessibilityButton
import com.example.neupsipromovil.presentation.common.organisms.AppointmentCard
import com.example.neupsipromovil.presentation.common.organisms.ClinicalInfoCard
import com.example.neupsipromovil.presentation.common.organisms.StaffMemberCard

@Composable
fun ProfileScreen(
    userId: String,
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = userId) {
        viewModel.getProfile(userId)
    }

    Scaffold(
        floatingActionButton = {
            AccessibilityButton(onClick = { /*Logica de accesibilidad*/ })
        },
        bottomBar = {
            // Implementar el Navbar aqui cuando este  completo
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        color = Color(0xFF3F51B5),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
            )
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Text(
                        text = "Error: ${state.error}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.user != null -> {
                    val user = state.user!!

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            ClinicalInfoCard(
                                age = user.age,
                                unitEntryDate = user.unitEntryDate,
                                neuroEntryDate = user.neuroEntryDate
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        item {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Próximas citas",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                user.nextAppointmentDate?.let { date ->
                                    AppointmentCard(
                                        month = "FEB",
                                        day = date.takeLast(2),
                                        title = "Cita proxima",
                                        time = user.nextAppointmentTime ?: "--:--"
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        item {
                            StaffMemberCard(roleTitle = "Psicólogo", staffName = user.assignedClinic)
                            Spacer(modifier = Modifier.height(16.dp))
                            StaffMemberCard(roleTitle = "Prostesista", staffName = user.prosthetist)
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}