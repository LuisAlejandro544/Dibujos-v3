package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.AppDatabase
import com.example.ui.canvas.CanvasScreen
import com.example.ui.canvas.CanvasViewModel
import com.example.ui.components.GalleryScreen
import com.example.ui.components.MainMenuScreen
import com.example.ui.components.WelcomeScreen
import com.example.ui.theme.KidsDrawTheme
import com.example.utils.VoiceAssistant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class AppScreen {
    MAIN_MENU,
    CANVAS,
    GALLERY,
    TOOLS_GUIDE
}

class MainActivity : ComponentActivity() {

    private val canvasViewModel: CanvasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        VoiceAssistant.init(this)

        setContent {
            KidsDrawTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.MAIN_MENU) }
                val uiState by canvasViewModel.uiState.collectAsStateWithLifecycle()
                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFFFFDF5)
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentScreen) {
                            AppScreen.MAIN_MENU -> {
                                MainMenuScreen(
                                    onStartFreeDrawing = {
                                        canvasViewModel.createNewDrawing()
                                        currentScreen = AppScreen.CANVAS
                                    },
                                    onStart1v1Duel = {
                                        canvasViewModel.start1v1Mode()
                                        currentScreen = AppScreen.CANVAS
                                    },
                                    onOpenGallery = {
                                        currentScreen = AppScreen.GALLERY
                                    },
                                    onOpenToolsGuide = {
                                        currentScreen = AppScreen.TOOLS_GUIDE
                                    },
                                    onSpeak = { text -> canvasViewModel.speak(text) }
                                )
                            }

                            AppScreen.TOOLS_GUIDE -> {
                                WelcomeScreen(
                                    onStartDrawing = {
                                        canvasViewModel.createNewDrawing()
                                        currentScreen = AppScreen.CANVAS
                                    },
                                    onSpeak = { text -> canvasViewModel.speak(text) },
                                    onBackToMenu = { currentScreen = AppScreen.MAIN_MENU }
                                )
                            }

                            AppScreen.GALLERY -> {
                                GalleryScreen(
                                    onNewDrawing = {
                                        canvasViewModel.createNewDrawing()
                                        currentScreen = AppScreen.CANVAS
                                    },
                                    onOpenDrawing = { drawingEntity ->
                                        canvasViewModel.loadDrawingFromEntity(drawingEntity)
                                        currentScreen = AppScreen.CANVAS
                                    },
                                    onDeleteDrawing = { drawingEntity ->
                                        scope.launch(Dispatchers.IO) {
                                            AppDatabase.getDatabase(context).drawingDao().deleteDrawing(drawingEntity)
                                        }
                                    },
                                    onBackToMenu = { currentScreen = AppScreen.MAIN_MENU }
                                )
                            }

                            AppScreen.CANVAS -> {
                                CanvasScreen(
                                    canvasViewModel = canvasViewModel,
                                    uiState = uiState,
                                    onNavigateBackToMenu = { currentScreen = AppScreen.MAIN_MENU },
                                    onNavigateToGuide = { currentScreen = AppScreen.TOOLS_GUIDE }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VoiceAssistant.shutdown()
    }
}
