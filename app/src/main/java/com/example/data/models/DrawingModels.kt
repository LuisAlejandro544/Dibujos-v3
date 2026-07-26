package com.example.data.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.squareup.moshi.JsonClass
import java.util.UUID

enum class ToolType {
    PENCIL,
    BRUSH,
    DUAL_BRUSH,
    CRAYON,
    GLOW,
    STAMP,
    BUCKET,
    ERASER,
    SELECT
}

enum class StampShape {
    STAR,
    HEART,
    SPARKLE,
    RAINBOW,
    FLOWER,
    PAW,
    SUN
}

enum class StencilType(val title: String, val icon: String) {
    NONE("Lienzo en Blanco", "📄"),
    DINOSAUR("Dinosaurio", "🦖"),
    ROCKET("Cohete Espacial", "🚀"),
    UNICORN("Unicornio Mágico", "🦄"),
    PUPPY("Perrito Curioso", "🐶"),
    FISH("Pez bajo el Mar", "🐠"),
    CAKE("Pastel de Cumpleaños", "🎂")
}

data class PointData(
    val x: Float,
    val y: Float
) {
    fun toOffset() = Offset(x, y)
    companion object {
        fun fromOffset(offset: Offset) = PointData(offset.x, offset.y)
    }
}

data class DrawingPath(
    val id: String = UUID.randomUUID().toString(),
    val points: List<PointData>,
    val colorArgb: Int,
    val strokeWidth: Float,
    val alpha: Float = 1.0f,
    val toolType: ToolType,
    val stampShape: StampShape? = null,
    val isRainbow: Boolean = false,
    val isGlow: Boolean = false,
    val isDualBrushMirror: Boolean = true,
    // Transform parameters if path was modified via selection
    val translationX: Float = 0f,
    val translationY: Float = 0f,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val rotationDegrees: Float = 0f
) {
    fun getColor(): Color = Color(colorArgb)
}

data class DrawingLayer(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isVisible: Boolean = true,
    val isLocked: Boolean = false,
    val opacity: Float = 1.0f,
    val paths: List<DrawingPath> = emptyList(),
    val stencilType: StencilType = StencilType.NONE
)

data class SelectionState(
    val isActive: Boolean = false,
    val selectionBounds: Rect? = null,
    val selectedPathIds: Set<String> = emptySet(),
    val activeLayerId: String? = null,
    val translation: Offset = Offset.Zero,
    val scale: Float = 1.0f,
    val rotation: Float = 0.0f
)

data class CanvasSnapshot(
    val layers: List<DrawingLayer>,
    val activeLayerId: String
)
