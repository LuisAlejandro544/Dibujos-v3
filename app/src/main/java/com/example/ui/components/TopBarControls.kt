package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.canvas.CanvasUiState

@Composable
fun TopBarControls(
    uiState: CanvasUiState,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onOpenLayers: () -> Unit,
    onOpenStencils: () -> Unit,
    onClearCanvas: () -> Unit,
    onSaveDrawing: () -> Unit,
    onOpenGallery: () -> Unit,
    onOpenHelp: () -> Unit,
    onToggleVoiceAssistant: () -> Unit,
    onToggleGpuAcceleration: () -> Unit = {},
    onStart1v1Duel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFF9E6), // Warm playful background
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
            ) {
                // 1. Home Menu Button
                IconButton(
                    onClick = onOpenGallery,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFFFEEAA), CircleShape)
                        .testTag("topbar_home_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Menú Principal",
                        tint = Color(0xFF8B4513),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 2. Voice Toggle
                IconButton(
                    onClick = onToggleVoiceAssistant,
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (uiState.isVoiceAssistantEnabled) Color(0xFFFF70A6) else Color(0xFFE0E0E0),
                            CircleShape
                        )
                        .testTag("topbar_voice_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isVoiceAssistantEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Modo Voz",
                        tint = if (uiState.isVoiceAssistantEnabled) Color.White else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 3. Compact GPU Badge Toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (uiState.isGpuAccelerated) Color(0xFF00C853) else Color(0xFF9E9E9E))
                        .clickable { onToggleGpuAcceleration() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("topbar_gpu_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (uiState.isGpuAccelerated) "⚡ GPU" else "⚡ CPU",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 4. 1v1 Duel Mode Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF5722))
                        .clickable { onStart1v1Duel() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("topbar_duel_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚔️ Duelo 1v1",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 5. Undo Button
                IconButton(
                    onClick = onUndo,
                    enabled = uiState.canUndo,
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (uiState.canUndo) Color(0xFFFFD166) else Color(0xFFE0E0E0),
                            CircleShape
                        )
                        .testTag("topbar_undo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Deshacer",
                        tint = if (uiState.canUndo) Color(0xFF333333) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 6. Redo Button
                IconButton(
                    onClick = onRedo,
                    enabled = uiState.canRedo,
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (uiState.canRedo) Color(0xFFFFD166) else Color(0xFFE0E0E0),
                            CircleShape
                        )
                        .testTag("topbar_redo_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Redo,
                        contentDescription = "Rehacer",
                        tint = if (uiState.canRedo) Color(0xFF333333) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 7. Stencils Button
                IconButton(
                    onClick = onOpenStencils,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFF06D6A0), CircleShape)
                        .testTag("topbar_stencils_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Plantillas",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 8. Layer Manager Button with badge
                Box {
                    IconButton(
                        onClick = onOpenLayers,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFF118AB2), CircleShape)
                            .testTag("topbar_layers_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Capas",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(16.dp)
                            .background(Color(0xFFEF476F), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${uiState.layers.size}",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 9. Clear Canvas Button
                IconButton(
                    onClick = onClearCanvas,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFEF476F), CircleShape)
                        .testTag("topbar_clear_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Limpiar Canvas",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 10. Save Drawing Button
                IconButton(
                    onClick = onSaveDrawing,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFFF595E), CircleShape)
                        .testTag("topbar_save_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Guardar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
