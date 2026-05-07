package com.example.neupsipromovil.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.neupsipromovil.presentation.navegation.NeupsiproNavGraph
import com.example.neupsipromovil.presentation.theme.NeupsiproMovilTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeupsiproMovilTheme {
                NeupsiproNavGraph()
            }
        }
    }
}
