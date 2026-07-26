package com.example.ui.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import com.example.data.models.CanvasSnapshot
import com.example.data.models.DrawingLayer
import com.example.data.models.DrawingPath
import com.example.data.models.SelectionState
import com.example.data.models.StampShape
import com.example.data.models.StencilType
import com.example.data.models.ToolType

data class CanvasUiState(
    val layers: List<DrawingLayer> = listOf(
        DrawingLayer(id = "layer_1", name = "Capa 1")
    ),
    val activeLayerId: String = "layer_1",
    val currentTool: ToolType = ToolType.PENCIL,
    val currentColor: Color = Color(0xFFFF3366), // Vibrant Pink/Red
    val currentStrokeWidth: Float = 24f,
    val currentAlpha: Float = 1.0f,
    val currentStampShape: StampShape = StampShape.STAR,
    val isRainbowMode: Boolean = false,
    val isGlowMode: Boolean = false,
    val isDualBrushMirror: Boolean = true,
    val activeStencil: StencilType = StencilType.NONE,
    val selectionState: SelectionState = SelectionState(),
    val inProgressPath: DrawingPath? = null,
    val isEyedropperActive: Boolean = false,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val currentDrawingId: Long? = null,
    val drawingTitle: String = "Mi Obra Maestra",
    val isSaving: Boolean = false,
    val showSaveSuccess: Boolean = false,
    val showClearConfirmDialog: Boolean = false,
    val showStencilModal: Boolean = false,
    val showHelpModal: Boolean = false,
    val isVoiceAssistantEnabled: Boolean = true,
    val showColorPickerModal: Boolean = false,
    val zoomScale: Float = 1.0f,
    val panOffset: Offset = Offset.Zero,

    // GPU Acceleration State
    val isGpuAccelerated: Boolean = true,
    val gpuInfoText: String = "Aceleración por GPU Activa (60 FPS)",

    // 1v1 Local Kid Drawing Duel State
    val is1v1ModeActive: Boolean = false,
    val duelPlayer1Name: String = "Niño 1 🎨",
    val duelPlayer2Name: String = "Niño 2 🚀",
    val duelCurrentTurn: Int = 1, // 1 for Player 1, 2 for Player 2
    val duelTimerSeconds: Int = 30,
    val duelTimeRemaining: Int = 30,
    val isDuelTimerRunning: Boolean = false,
    val duelPrompt: String = "",
    val duelPlayer1Thumbnail: String? = null,
    val duelPlayer2Thumbnail: String? = null,
    val duelPlayer1Layers: List<DrawingLayer> = emptyList(),
    val duelPlayer2Layers: List<DrawingLayer> = emptyList(),
    val showDuelResultModal: Boolean = false,
    val duelP1Rating: Int = 5,
    val duelP2Rating: Int = 5,
    val isAiEvaluating: Boolean = false,
    val aiPromptLoading: Boolean = false,
    val aiJudgeResult: com.example.data.remote.AiJudgeResult? = null
)
