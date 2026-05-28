package com.example.neupsipromovil.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.neupsiproMovil.R
import com.example.neupsipromovil.presentation.common.atoms.AppText
import com.example.neupsipromovil.presentation.common.molecules.PrimaryButton
import com.example.neupsipromovil.presentation.common.molecules.SecondaryButton
import com.example.neupsipromovil.presentation.common.molecules.HelpBadge
import com.example.neupsipromovil.presentation.common.molecules.InputTextField
import com.example.neupsipromovil.presentation.theme.AppTypography
import com.example.neupsipromovil.presentation.theme.Blue01
import com.example.neupsipromovil.presentation.theme.LightBlue
import com.example.neupsipromovil.presentation.theme.White

@Suppress("ktlint:standard:function-naming")
@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val loginState by viewModel.loginState.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            onNavigateToHome()
            viewModel.resetState()
        }
    }

    val isLoading = loginState is LoginUiState.Loading
    val isError = loginState is LoginUiState.Error
    val errorMessage = (loginState as? LoginUiState.Error)?.message

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(LightBlue.copy(alpha = 0.25f))
                .verticalScroll(rememberScrollState()),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(296.dp)
                    .background(Blue01),
        ) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_enes),
                    contentDescription = "ENES Juriquilla UNAM",
                    modifier = Modifier.size(72.dp),
                )
                Column {
                    AppText(
                        text = "Golondrina",
                        style = AppTypography.titleM,
                        color = White,
                    )
                    AppText(
                        text = "ENES Juriquilla - UNAM",
                        style = AppTypography.labelBase,
                        color = LightBlue,
                    )
                }
            }
            HelpBadge(
                onClick = { /* TODO: open help */ },
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp)
                        .offset(y = 25.dp),
            )
        }

        Spacer(Modifier.height(88.dp))

        // ── Form
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            InputTextField(
                label = "Usuario",
                value = username,
                onValueChange = viewModel::onUsernameChanged,
                placeholder = "Usuario",
                leadingIcon = Icons.Default.Person,
                isError = isError,
            )
            InputTextField(
                label = "Contraseña",
                value = password,
                onValueChange = viewModel::onPasswordChanged,
                placeholder = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                isError = isError,
                isPassword = true,
            )
            if (isError && !errorMessage.isNullOrBlank()) {
                AppText(
                    text = errorMessage,
                    style = AppTypography.caption,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            PrimaryButton(
                text = "Iniciar Sesión",
                leadingIcon = Icons.Default.Person,
                onClick = viewModel::login,
                loading = isLoading,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Spacer(Modifier.height(36.dp))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 48.dp))
        Spacer(Modifier.height(48.dp))

        // ── Accessibility action
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SecondaryButton(
                text = "Accesibilidad",
                onClick = { /* accessibility */ },
                leadingIcon = Icons.Default.Accessibility,
            )
        }

        Spacer(Modifier.height(40.dp))
    }
}
