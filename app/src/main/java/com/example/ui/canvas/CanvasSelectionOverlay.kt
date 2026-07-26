package com.example.ui.canvas

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.models.SelectionState

object CanvasSelectionOverlay {

    fun drawSelectionOverlay(drawScope: DrawScope, selectionState: SelectionState) {
        if (!selectionState.isActive || selectionState.selectionBounds == null) return

        val bounds = selectionState.selectionBounds
        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)

        drawScope.drawRoundRect(
            color = Color(0xFF0088FF),
            topLeft = bounds.topLeft,
            size = bounds.size,
            cornerRadius = CornerRadius(12f, 12f),
            style = Stroke(width = 4f, pathEffect = dashEffect)
        )

        val handleR = 14f
        val corners = listOf(
            bounds.topLeft,
            bounds.topRight,
            bounds.bottomLeft,
            bounds.bottomRight
        )

        corners.forEach { pos ->
            drawScope.drawCircle(color = Color.White, radius = handleR, center = pos)
            drawScope.drawCircle(color = Color(0xFF0088FF), radius = handleR, center = pos, style = Stroke(width = 4f))
        }
    }
}
