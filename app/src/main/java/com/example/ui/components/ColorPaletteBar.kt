package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.canvas.CanvasUiState

val PALETTE_COLORS = listOf(
    Color(0xFFFF3366), // Vibrant Pink
    Color(0xFFFF595E), // Coral Red
    Color(0xFFFF924C), // Orange
    Color(0xFFFFCA3A), // Bright Yellow
    Color(0xFF8AC926), // Lime Green
    Color(0xFF06D6A0), // Emerald
    Color(0xFF00C8FF), // Cyan
    Color(0xFF1982C4), // Royal Blue
    Color(0xFF6A4C93), // Purple
    Color(0xFFFF70A6), // Light Pink
    Color(0xFF8B4513), // Chocolate Brown
    Color(0xFF2B2D42), // Dark Slate
    Color(0xFFFFFFFF)  // Pure White
)

@Composable
fun ColorPaletteBar(
    uiState: CanvasUiState,
    onSelectColor: (Color) -> Unit,
    onToggleRainbow: () -> Unit,
    onToggleGlow: () -> Unit,
    onOpenColorPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF1F3F5),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rainbow Mode Toggle Button
            FilterChip(
                selected = uiState.isRainbowMode,
                onClick = onToggleRainbow,
                label = { Text(" Arcoíris 🌈", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFFCA3A),
                    selectedLabelColor = Color(0xFF333333)
                ),
                modifier = Modifier.padding(start = 8.dp)
            )

            // Glow Mode Toggle Button
            FilterChip(
                selected = uiState.isGlowMode,
                onClick = onToggleGlow,
                label = { Text(" Brillo ✨", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF1982C4),
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.padding(start = 4.dp, end = 8.dp)
            )

            // Swatches
            LazyRow(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(PALETTE_COLORS) { color ->
                    val isSelected = uiState.currentColor == color && !uiState.isRainbowMode

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFF333333) else Color(0x33000000),
                                shape = CircleShape
                            )
                            .clickable { onSelectColor(color) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(
                                        if (color == Color.White) Color.Black else Color.White,
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            // More Colors Wheel Picker Button
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE9ECEF))
                    .clickable { onOpenColorPicker() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Más Colores",
                    tint = Color(0xFF495057),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
