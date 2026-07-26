package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ToolType

data class ToolItemInfo(
    val type: ToolType,
    val name: String,
    val emoji: String,
    val activeColor: Color
)

val KIDS_TOOLS = listOf(
    ToolItemInfo(ToolType.PENCIL, "Lápiz", "✏️", Color(0xFFFF595E)),
    ToolItemInfo(ToolType.BRUSH, "Pincel", "🖌️", Color(0xFFFFCA3A)),
    ToolItemInfo(ToolType.DUAL_BRUSH, "Pincel Doble", "✌️", Color(0xFFFF3366)),
    ToolItemInfo(ToolType.CRAYON, "Cera", "🖍️", Color(0xFF8AC926)),
    ToolItemInfo(ToolType.GLOW, "Neón", "✨", Color(0xFF1982C4)),
    ToolItemInfo(ToolType.SPARKLE_BRUSH, "Estrellas", "⭐", Color(0xFFFFCA3A)),
    ToolItemInfo(ToolType.BUBBLE_BRUSH, "Burbujas", "🫧", Color(0xFF00C8FF)),
    ToolItemInfo(ToolType.GALAXY_BRUSH, "Galaxia", "🌌", Color(0xFF6A4C93)),
    ToolItemInfo(ToolType.STAMP, "Sellos", "🎨", Color(0xFF9D4EDD)),
    ToolItemInfo(ToolType.BUCKET, "Cubo", "🪣", Color(0xFFFF924C)),
    ToolItemInfo(ToolType.SELECT, "Selección", "✂️", Color(0xFF00C8FF)),
    ToolItemInfo(ToolType.ERASER, "Goma", "🧹", Color(0xFFFF70A6))
)

@Composable
fun ToolBar(
    selectedTool: ToolType,
    onSelectTool: (ToolType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF8F9FA),
        shadowElevation = 4.dp
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(KIDS_TOOLS) { tool ->
                val isSelected = tool.type == selectedTool

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) tool.activeColor.copy(alpha = 0.2f) else Color.Transparent
                        )
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) tool.activeColor else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onSelectTool(tool.type) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                if (isSelected) tool.activeColor else Color(0xFFEFEFEF),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tool.emoji,
                            fontSize = 24.sp
                        )
                    }

                    Text(
                        text = tool.name,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) tool.activeColor else Color(0xFF555555),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
