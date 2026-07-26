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
    // Alegres & Neón
    Color(0xFFFF3366), // Vibrant Pink
    Color(0xFFFF0055), // Neon Fuego
    Color(0xFFFF595E), // Coral Red
    Color(0xFFFF924C), // Bright Orange
    Color(0xFFFFCA3A), // Sunny Yellow
    Color(0xFFCCFF00), // Neon Lime Green
    Color(0xFF8AC926), // Apple Green
    Color(0xFF06D6A0), // Emerald
    Color(0xFF00F0FF), // Electric Cyan
    Color(0xFF00C8FF), // Vivid Sky Blue
    Color(0xFF1982C4), // Royal Blue
    Color(0xFF6A4C93), // Purple
    Color(0xFF9D4EDD), // Neon Violet
    Color(0xFFFF70A6), // Bubblegum Pink
    // Pastel & Mágicos
    Color(0xFFFFC6FF), // Pastel Lavender Pink
    Color(0xFFBDB2FF), // Pastel Lilac
    Color(0xFFA0C4FF), // Pastel Baby Blue
    Color(0xFF9BF6FF), // Pastel Ice Blue
    Color(0xFFCAFFBF), // Pastel Mint
    Color(0xFFFDFFB6), // Pastel Cream
    Color(0xFFFFADAD), // Pastel Peach
    // Oscuros & BÁSICOS
    Color(0xFF8B4513), // Chocolate Brown
    Color(0xFF5A3E2B), // Deep Wood
    Color(0xFF2B2D42), // Midnight Slate
    Color(0xFF000000), // Pure Black
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
