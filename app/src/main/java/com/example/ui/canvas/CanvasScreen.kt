package com.example.ui.canvas

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.BrushSettingsDrawer
import com.example.ui.components.ClearConfirmModal
import com.example.ui.components.ColorPaletteBar
import com.example.ui.components.ColorPickerModal
import com.example.ui.components.DuelHeaderBar
import com.example.ui.components.DuelResultModal
import com.example.ui.components.HelpGuideModal
import com.example.ui.components.LandscapeQuickToolsRail
import com.example.ui.components.LayerManagerSheet
import com.example.ui.components.SaveSuccessModal
import com.example.ui.components.SelectionActionOverlay
import com.example.ui.components.StencilPickerModal
import com.example.ui.components.ToolBar
import com.example.ui.components.TopBarControls
import com.example.utils.DrawingUtils

@Composable
fun CanvasScreen(
    canvasViewModel: CanvasViewModel,
    uiState: CanvasUiState,
    onNavigateBackToMenu: () -> Unit,
    onNavigateToGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLayersSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // 1. Top Bar Controls or 1v1 Duel Header Bar
            if (uiState.is1v1ModeActive) {
                DuelHeaderBar(
                    uiState = uiState,
                    onPauseResumeTimer = { canvasViewModel.pauseResumeDuelTimer() },
                    onFinishTurnEarly = { canvasViewModel.finishTurnEarly() },
                    onStartPlayer2Timer = { canvasViewModel.startPlayer2Timer() },
                    onExitDuel = { canvasViewModel.exit1v1Mode() }
                )
            } else {
                TopBarControls(
                    uiState = uiState,
                    onUndo = { canvasViewModel.undo() },
                    onRedo = { canvasViewModel.redo() },
                    onOpenLayers = { showLayersSheet = true },
                    onOpenStencils = { canvasViewModel.toggleStencilModal() },
                    onClearCanvas = { canvasViewModel.showClearDialog() },
                    onSaveDrawing = {
                        val thumbBitmap = DrawingUtils.renderLayersToBitmap(uiState.layers)
                        canvasViewModel.saveDrawing(context, thumbBitmap)
                    },
                    onOpenGallery = onNavigateBackToMenu,
                    onOpenHelp = onNavigateToGuide,
                    onToggleVoiceAssistant = { canvasViewModel.toggleVoiceAssistant() },
                    onToggleGpuAcceleration = { canvasViewModel.toggleGpuAcceleration() },
                    onStart1v1Duel = { canvasViewModel.start1v1Mode() }
                )
            }

            // 2. Interactive Drawing Canvas Viewport with Landscape Layout support
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (isLandscape) {
                    LandscapeQuickToolsRail(
                        uiState = uiState,
                        onSelectTool = { tool -> canvasViewModel.selectTool(tool) },
                        onSelectColor = { color -> canvasViewModel.setColor(color) },
                        onSetStrokeWidth = { w -> canvasViewModel.setStrokeWidth(w) },
                        onToggleRainbow = { canvasViewModel.toggleRainbowMode() },
                        onToggleGlow = { canvasViewModel.toggleGlowMode() },
                        onOpenColorPicker = { canvasViewModel.toggleColorPickerModal() },
                        onSetStampShape = { shape -> canvasViewModel.setStampShape(shape) },
                        onToggleDualBrushMirror = { canvasViewModel.toggleDualBrushMirrorMode() }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    DrawingCanvas(
                        uiState = uiState,
                        onTouchStart = { offset -> canvasViewModel.onTouchStart(offset) },
                        onTouchMove = { offset -> canvasViewModel.onTouchMove(offset) },
                        onTouchEnd = { canvasViewModel.onTouchEnd() },
                        modifier = Modifier.fillMaxSize()
                    )

                    SelectionActionOverlay(
                        uiState = uiState,
                        onDuplicate = { canvasViewModel.duplicateSelection() },
                        onDelete = { canvasViewModel.deleteSelection() },
                        onCommit = { canvasViewModel.commitSelectionTransform() },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            // 3. Bottom controls only shown when NOT in landscape mode
            if (!isLandscape) {
                ColorPaletteBar(
                    uiState = uiState,
                    onSelectColor = { color -> canvasViewModel.setColor(color) },
                    onToggleRainbow = { canvasViewModel.toggleRainbowMode() },
                    onToggleGlow = { canvasViewModel.toggleGlowMode() },
                    onOpenColorPicker = { canvasViewModel.toggleColorPickerModal() }
                )

                BrushSettingsDrawer(
                    uiState = uiState,
                    onSetStrokeWidth = { width -> canvasViewModel.setStrokeWidth(width) },
                    onSetAlpha = { alpha -> canvasViewModel.setAlpha(alpha) },
                    onSetStampShape = { shape -> canvasViewModel.setStampShape(shape) },
                    onToggleDualBrushMirror = { canvasViewModel.toggleDualBrushMirrorMode() }
                )

                ToolBar(
                    selectedTool = uiState.currentTool,
                    onSelectTool = { tool -> canvasViewModel.selectTool(tool) }
                )
            }
        }

        // Modals & Overlays
        AnimatedVisibility(
            visible = showLayersSheet,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            LayerManagerSheet(
                uiState = uiState,
                onSelectLayer = { id -> canvasViewModel.selectLayer(id) },
                onAddLayer = { canvasViewModel.addLayer() },
                onDeleteLayer = { id -> canvasViewModel.deleteLayer(id) },
                onToggleVisibility = { id -> canvasViewModel.toggleLayerVisibility(id) },
                onToggleLock = { id -> canvasViewModel.toggleLayerLock(id) },
                onSetOpacity = { id, opacity -> canvasViewModel.setLayerOpacity(id, opacity) },
                onDuplicateLayer = { id -> canvasViewModel.duplicateLayer(id) },
                onMoveUp = { id -> canvasViewModel.moveLayerUp(id) },
                onMoveDown = { id -> canvasViewModel.moveLayerDown(id) },
                onMergeDown = { id -> canvasViewModel.mergeLayerDown(id) },
                onClearLayer = { id -> canvasViewModel.clearLayer(id) },
                onRenameLayer = { id, name -> canvasViewModel.renameLayer(id, name) },
                onClose = { showLayersSheet = false }
            )
        }

        if (uiState.showStencilModal) {
            StencilPickerModal(
                activeStencil = uiState.activeStencil,
                onSelectStencil = { stencil -> canvasViewModel.setStencil(stencil) },
                onDismiss = { canvasViewModel.toggleStencilModal() }
            )
        }

        if (uiState.showClearConfirmDialog) {
            ClearConfirmModal(
                onClearLayer = { canvasViewModel.clearCurrentLayer() },
                onClearAll = { canvasViewModel.clearAllCanvas() },
                onDismiss = { canvasViewModel.dismissClearDialog() }
            )
        }

        if (uiState.showSaveSuccess) {
            SaveSuccessModal(
                onDismiss = { canvasViewModel.dismissSaveSuccess() }
            )
        }

        if (uiState.showHelpModal) {
            HelpGuideModal(
                onDismiss = { canvasViewModel.toggleHelpModal() }
            )
        }

        if (uiState.showColorPickerModal) {
            ColorPickerModal(
                initialColor = uiState.currentColor,
                onSelectColor = { color -> canvasViewModel.setColor(color) },
                onDismiss = { canvasViewModel.toggleColorPickerModal() }
            )
        }

        if (uiState.showDuelResultModal) {
            DuelResultModal(
                uiState = uiState,
                onSetP1Rating = { stars -> canvasViewModel.setDuelRating(1, stars) },
                onSetP2Rating = { stars -> canvasViewModel.setDuelRating(2, stars) },
                onPlayAgain = { canvasViewModel.start1v1Mode() },
                onExitDuel = { canvasViewModel.exit1v1Mode() },
                onSpeak = { text -> canvasViewModel.speak(text) }
            )
        }
    }
}
