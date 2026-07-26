package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.StampShape
import com.example.data.models.ToolType
import com.example.ui.canvas.CanvasUiState

data class LandscapeToolItem(
    val type: ToolType,
    val name: String,
    val emoji: String
)

val LANDSCAPE_TOOLS = listOf(
    LandscapeToolItem(ToolType.PENCIL, "Lápiz", "✏️"),
    LandscapeToolItem(ToolType.BRUSH, "Pincel", "🖌️"),
    LandscapeToolItem(ToolType.DUAL_BRUSH, "Doble", "✌️"),
    LandscapeToolItem(ToolType.CRAYON, "Cera", "🖍️"),
    LandscapeToolItem(ToolType.GLOW, "Brillo", "✨"),
    LandscapeToolItem(ToolType.SPARKLE_BRUSH, "Estrellas", "⭐"),
    LandscapeToolItem(ToolType.BUBBLE_BRUSH, "Burbujas", "🫧"),
    LandscapeToolItem(ToolType.GALAXY_BRUSH, "Galaxia", "🌌"),
    LandscapeToolItem(ToolType.STAMP, "Sello", "🎨"),
    LandscapeToolItem(ToolType.BUCKET, "Relleno", "🪣"),
    LandscapeToolItem(ToolType.ERASER, "Borrador", "🧹"),
    LandscapeToolItem(ToolType.SELECT, "Selección", "👆")
)

val LANDSCAPE_PALETTE_COLORS = listOf(
    Color(0xFFFF3366), // Pink/Red
    Color(0xFFFF9900), // Orange
    Color(0xFFFFD700), // Yellow
    Color(0xFF00C853), // Green
    Color(0xFF00B0FF), // Light Blue
    Color(0xFF3F51B5), // Indigo
    Color(0xFFAA00FF), // Purple
    Color(0xFF795548), // Brown
    Color(0xFF000000), // Black
    Color(0xFFFFFFFF)  // White
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LandscapeQuickToolsRail(
    uiState: CanvasUiState,
    onSelectTool: (ToolType) -> Unit,
    onSelectColor: (Color) -> Unit,
    onSetStrokeWidth: (Float) -> Unit,
    onToggleRainbow: () -> Unit,
    onToggleGlow: () -> Unit,
    onOpenColorPicker: () -> Unit,
    onSetStampShape: (StampShape) -> Unit,
    onToggleDualBrushMirror: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(115.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFFFFF9E6), // Warm playful light yellow
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // --- SECTION 1: TOOL SELECTION ---
            Text(
                text = "HERRAMIENTAS",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8B4513),
                letterSpacing = 0.5.sp
            )

            LANDSCAPE_TOOLS.forEach { item ->
                val isSelected = uiState.currentTool == item.type

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) Color(0xFFFF70A6) else Color.White)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color(0xFFFF3366) else Color(0xFFE8E8E8),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onSelectTool(item.type) }
                        .padding(vertical = 6.dp, horizontal = 4.dp)
                        .testTag("landscape_tool_${item.type.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = item.emoji, fontSize = 18.sp)
                        Text(
                            text = item.name,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF444444),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // --- SECTION 1.5: CONTEXTUAL SETTINGS FOR ACTIVE TOOL ---
            if (uiState.currentTool == ToolType.DUAL_BRUSH) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "PINCEL DOBLE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8B008B)
                )

                val isMirror = uiState.isDualBrushMirror
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isMirror) Color(0xFFFFD166) else Color.White)
                        .border(
                            width = if (isMirror) 2.dp else 1.dp,
                            color = if (isMirror) Color(0xFFFF924C) else Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onToggleDualBrushMirror() }
                        .padding(vertical = 6.dp, horizontal = 4.dp)
                        .testTag("landscape_toggle_dual_mirror"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = if (isMirror) "🪞 Espejo" else "✌️ Paralelo", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                        Text(text = if (isMirror) "(Izq ↔ Der)" else "(Dos líneas)", fontSize = 9.sp, color = Color(0xFF666666))
                    }
                }
            }

            if (uiState.currentTool == ToolType.STAMP) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "SELLOS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8B008B)
                )

                val stamps = listOf(
                    StampShape.GOOGLY_EYES to "👀",
                    StampShape.GOOGLY_EYE_SINGLE to "👁️",
                    StampShape.STICKER_CAT to "🐱",
                    StampShape.STICKER_CROWN to "👑",
                    StampShape.STICKER_MAGIC_WAND to "🪄",
                    StampShape.STICKER_BOW to "🎀",
                    StampShape.STICKER_SUNGLASSES to "🕶️",
                    StampShape.STAR to "⭐",
                    StampShape.HEART to "💖",
                    StampShape.SPARKLE to "✨",
                    StampShape.FLOWER to "🌸",
                    StampShape.PAW to "🐾",
                    StampShape.SUN to "☀️",
                    StampShape.RAINBOW to "🌈"
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    stamps.forEach { (shape, emoji) ->
                        val isSel = uiState.currentStampShape == shape
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(if (isSel) Color(0xFFFFD166) else Color.White)
                                .border(if (isSel) 2.dp else 0.5.dp, if (isSel) Color(0xFFFF924C) else Color.LightGray, CircleShape)
                                .clickable { onSetStampShape(shape) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 14.sp)
                        }
                    }
                }
            }

            // --- SECTION 2: STROKE WIDTH PRESETS ---
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "GROSOR",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8B4513)
            )

            val strokeSizes = listOf(
                12f to "Fino",
                24f to "Medio",
                48f to "Grueso",
                70f to "Gigante"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                strokeSizes.forEach { (size, label) ->
                    val isSelected = kotlin.math.abs(uiState.currentStrokeWidth - size) < 5f
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFFFFD166) else Color.White)
                            .border(if (isSelected) 2.dp else 0.5.dp, if (isSelected) Color(0xFFFF924C) else Color.LightGray, CircleShape)
                            .clickable { onSetStrokeWidth(size) },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size((size / 5f).coerceIn(4f, 16f).dp)
                                .clip(CircleShape)
                                .background(if (uiState.isRainbowMode) Color(0xFFFFCA3A) else uiState.currentColor)
                        )
                    }
                }
            }

            // --- SECTION 3: COLORS & SPECIAL FX ---
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "COLORES",
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8B4513)
            )

            // Rainbow & Glow FX Quick Toggle Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Rainbow Toggle
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (uiState.isRainbowMode) Color(0xFFFFD166) else Color.White)
                        .border(if (uiState.isRainbowMode) 2.dp else 1.dp, if (uiState.isRainbowMode) Color(0xFFFF924C) else Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                        .clickable { onToggleRainbow() }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌈", fontSize = 14.sp)
                }

                // Glow Toggle
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (uiState.isGlowMode) Color(0xFFFFD166) else Color.White)
                        .border(if (uiState.isGlowMode) 2.dp else 1.dp, if (uiState.isGlowMode) Color(0xFFFF924C) else Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                        .clickable { onToggleGlow() }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✨", fontSize = 14.sp)
                }
            }

            // Palette Grid
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LANDSCAPE_PALETTE_COLORS.forEach { color ->
                    val isSelected = uiState.currentColor == color && !uiState.isRainbowMode
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color(0xFFFF3366) else Color(0xFFCCCCCC),
                                shape = CircleShape
                            )
                            .clickable { onSelectColor(color) }
                    )
                }

                // Custom Color Picker Icon
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, CircleShape)
                        .clickable { onOpenColorPicker() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Más colores",
                        tint = Color(0xFFFF70A6),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
