package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.StampShape
import com.example.data.models.ToolType
import com.example.ui.canvas.CanvasUiState

data class StampOption(
    val shape: StampShape,
    val name: String,
    val emoji: String
)

val STAMP_OPTIONS = listOf(
    StampOption(StampShape.STAR, "Estrella", "⭐"),
    StampOption(StampShape.HEART, "Corazón", "💖"),
    StampOption(StampShape.SPARKLE, "Chispa", "✨"),
    StampOption(StampShape.FLOWER, "Flor", "🌸"),
    StampOption(StampShape.PAW, "Huella", "🐾"),
    StampOption(StampShape.SUN, "Sol", "☀️"),
    StampOption(StampShape.RAINBOW, "Arcoíris", "🌈")
)

@Composable
fun BrushSettingsDrawer(
    uiState: CanvasUiState,
    onSetStrokeWidth: (Float) -> Unit,
    onSetAlpha: (Float) -> Unit,
    onSetStampShape: (StampShape) -> Unit,
    onToggleDualBrushMirror: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFF0F5), // Light cute pink
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Stroke Width Adjuster
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Grosor:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF8B008B),
                    modifier = Modifier.width(60.dp)
                )

                // Live Circular Preview
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size((uiState.currentStrokeWidth * 0.5f).dp.coerceIn(4.dp, 32.dp))
                            .background(
                                if (uiState.isRainbowMode) Color(0xFFFFCA3A) else uiState.currentColor,
                                CircleShape
                            )
                    )
                }

                Slider(
                    value = uiState.currentStrokeWidth,
                    onValueChange = onSetStrokeWidth,
                    valueRange = 6f..70f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFF3366),
                        activeTrackColor = Color(0xFFFF70A6)
                    ),
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                )

                Text(
                    text = "${uiState.currentStrokeWidth.toInt()}px",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF555555)
                )
            }

            // Dual Brush Mode Selector if DUAL_BRUSH is active
            if (uiState.currentTool == ToolType.DUAL_BRUSH) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Modo de Pincel Doble:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF8B008B)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val isMirror = uiState.isDualBrushMirror

                    // Option 1: Mirror Symmetry
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isMirror) Color(0xFFFFD166) else Color.White)
                            .border(
                                width = if (isMirror) 2.5.dp else 1.dp,
                                color = if (isMirror) Color(0xFFFF924C) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { if (!isMirror) onToggleDualBrushMirror() }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🪞", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Espejo (Izq ↔ Der)",
                                fontSize = 12.sp,
                                fontWeight = if (isMirror) FontWeight.Bold else FontWeight.Medium,
                                color = Color(0xFF333333)
                            )
                        }
                    }

                    // Option 2: Parallel Dual
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (!isMirror) Color(0xFFFFD166) else Color.White)
                            .border(
                                width = if (!isMirror) 2.5.dp else 1.dp,
                                color = if (!isMirror) Color(0xFFFF924C) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { if (isMirror) onToggleDualBrushMirror() }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "✌️", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Doble Paralelo",
                                fontSize = 12.sp,
                                fontWeight = if (!isMirror) FontWeight.Bold else FontWeight.Medium,
                                color = Color(0xFF333333)
                            )
                        }
                    }
                }
            }

            // Stamp Picker if Stamp tool is active
            if (uiState.currentTool == ToolType.STAMP) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Elige tu Sello Favorito:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF8B008B)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(STAMP_OPTIONS) { option ->
                        val isSelected = uiState.currentStampShape == option.shape

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) Color(0xFFFFD166) else Color.White
                                )
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) Color(0xFFFF924C) else Color(0xFFE0E0E0),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onSetStampShape(option.shape) }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = option.emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = option.name,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color(0xFF333333)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
