package com.example.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.example.data.models.SelectionState
import com.example.data.models.StencilType
import com.example.data.models.ToolType

@Composable
fun DrawingCanvas(
    uiState: CanvasUiState,
    onTouchStart: (Offset) -> Unit,
    onTouchMove: (Offset) -> Unit,
    onTouchEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(uiState.activeLayerId, uiState.currentTool) {
                detectDragGestures(
                    onDragStart = { offset -> onTouchStart(offset) },
                    onDrag = { change, _ ->
                        change.consume()
                        onTouchMove(change.position)
                    },
                    onDragEnd = { onTouchEnd() },
                    onDragCancel = { onTouchEnd() }
                )
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    clip = true
                }
        ) {
            val canvasSize = size

            // 1. Draw Stencil Template if chosen
            if (uiState.activeStencil != StencilType.NONE) {
                Stencils.drawStencil(this, uiState.activeStencil, canvasSize)
            }

            // 2. Draw Layers in order from bottom to top
            uiState.layers.forEach { layer ->
                if (layer.isVisible) {
                    CanvasPathRenderer.drawLayerContent(
                        drawScope = this,
                        layer = layer,
                        selectionState = uiState.selectionState,
                        activeLayerId = uiState.activeLayerId
                    )
                }
            }

            // 3. Draw In-Progress Path (Active Line being drawn)
            uiState.inProgressPath?.let { inProgress ->
                if (inProgress.toolType == ToolType.DUAL_BRUSH && inProgress.isDualBrushMirror) {
                    val centerX = canvasSize.width / 2f
                    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 14f), 0f)
                    drawLine(
                        color = Color(0xFFFF3366).copy(alpha = 0.5f),
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, canvasSize.height),
                        strokeWidth = 3f,
                        pathEffect = dashEffect
                    )
                }

                CanvasPathRenderer.drawSinglePath(
                    drawScope = this,
                    pathModel = inProgress,
                    isSelected = false,
                    selectionState = SelectionState()
                )
            }

            // 4. Draw Selection Bounding Box if active
            CanvasSelectionOverlay.drawSelectionOverlay(this, uiState.selectionState)
        }
    }
}
