package com.example.neupsipromovil.presentation.common.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.common.molecules.navBar.BottomNavItem

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainAppBottomBar(
    currentScreen: String,
    onNavigate: (String) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(Color.White),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BottomNavItem(
            icon = Icons.Default.Forum,
            label = "Foro",
            isSelected = currentScreen == "foro",
            onClick = { onNavigate("foro") },
            modifier = Modifier.weight(1f),
        )
        BottomNavItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            isSelected = currentScreen == "perfil",
            onClick = { onNavigate("perfil") },
            modifier = Modifier.weight(1f),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(name = "Atoms & Molecules Preview", showBackground = true)
@Composable
fun ComponentsPreview() {
    MainAppBottomBar(currentScreen = "perfil", onNavigate = {})
}
