

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostBanner
import com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostSubmitButton
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.CreatePostUiState
import com.example.neupsipromovil.presentation.screens.forum.publiDetail.CreatePostViewModel
import com.example.neupsipromovil.presentation.screens.forum.PostForum.CreatePostImagePicker
import java.io.File

private val AccentBlue      = Color(0xFF3F51B5)
private val BackgroundColor = Color(0xFFF5F6FA)
private val SurfaceWhite    = Color(0xFFFFFFFF)
private val TextPrimary     = Color(0xFF1A1A2E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavHostController,
    viewModel: CreatePostViewModel = hiltViewModel(),
) {
    val uiState   by viewModel.uiState.collectAsState()
    val titulo    by viewModel.titulo.collectAsState()
    val contenido by viewModel.contenido.collectAsState()
    val imagenUri by viewModel.imagenUri.collectAsState()
    val canSubmit by viewModel.canSubmit.collectAsState()


    val context           = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading         = uiState is CreatePostUiState.Loading

    val tempUri = remember {
        val file = File(context.cacheDir, "foto_post_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }



    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            val size = context.contentResolver
                .openAssetFileDescriptor(tempUri, "r")?.use { it.length } ?: 0L
            viewModel.onImageSelected(tempUri, size)
        }
    }


    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(tempUri)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val size = context.contentResolver
                .openAssetFileDescriptor(uri, "r")?.use { it.length } ?: 0L
            viewModel.onImageSelected(uri, size)
        }
    }

    LaunchedEffect(uiState) {
        when (val s = uiState) {
            is CreatePostUiState.Success -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("post_created", true)
                navController.popBackStack()
            }
            is CreatePostUiState.Error -> {
                snackbarHostState.showSnackbar(s.message)
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "Nueva Publicación",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = Color.White,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector        = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint               = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AccentBlue),
            )
        },
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundColor,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CreatePostBanner()

            PostFieldLabel("Título de la discusión")
            OutlinedTextField(
                value         = titulo,
                onValueChange = viewModel::onTituloChanged,
                placeholder   = { Text("Ej: Avances en terapia", color = Color(0xFFBBBBBB)) },
                singleLine    = true,
                enabled       = !isLoading,
                shape         = RoundedCornerShape(14.dp),
                colors        = postTextFieldColors(),
                modifier      = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text  = "${titulo.length}/${CreatePostViewModel.MAX_TITULO}",
                        color = if (titulo.length >= CreatePostViewModel.MAX_TITULO)
                            Color.Red else Color(0xFF888888),
                    )
                },
            )

            PostFieldLabel("Contenido")
            OutlinedTextField(
                value         = contenido,
                onValueChange = viewModel::onContenidoChanged,
                placeholder   = { Text("Escribe aquí los detalles...", color = Color(0xFFBBBBBB)) },
                minLines      = 5,
                maxLines      = 10,
                enabled       = !isLoading,
                shape         = RoundedCornerShape(14.dp),
                colors        = postTextFieldColors(),
                modifier      = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text  = "${contenido.length}/${CreatePostViewModel.MAX_CONTENIDO}",
                        color = if (contenido.length >= CreatePostViewModel.MAX_CONTENIDO)
                            Color.Red else Color(0xFF888888),
                    )
                },
            )

            PostFieldLabel("Agregar imagen (opcional)")
            CreatePostImagePicker(
                imagenUri = imagenUri,
                enabled   = !isLoading,
                onPick    = { galleryLauncher.launch("image/*") },
                onCamera  = {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                },
                onRemove  = viewModel::removeImage,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CreatePostSubmitButton(
                isLoading = isLoading,
                enabled   = canSubmit && !isLoading,
                onClick   = viewModel::submit,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PostFieldLabel(text: String) {
    Text(
        text       = text,
        fontSize   = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color      = Color(0xFF1A1A2E),
    )
}

@Composable
fun postTextFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor    = Color(0xFFE0E0E0),
    focusedBorderColor      = AccentBlue,
    unfocusedContainerColor = SurfaceWhite,
    focusedContainerColor   = SurfaceWhite,
)
