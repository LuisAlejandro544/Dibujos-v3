package com.example.ui.canvas

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.DrawingEntity
import com.example.data.models.CanvasSnapshot
import com.example.data.models.DrawingLayer
import com.example.data.models.DrawingPath
import com.example.data.models.PointData
import com.example.data.models.SelectionState
import com.example.data.models.StampShape
import com.example.data.models.StencilType
import com.example.data.models.ToolType
import com.example.utils.DrawingUtils
import com.example.utils.VoiceAssistant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CanvasViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CanvasUiState())
    val uiState: StateFlow<CanvasUiState> = _uiState.asStateFlow()

    private val undoStack = mutableListOf<CanvasSnapshot>()
    private val redoStack = mutableListOf<CanvasSnapshot>()

    private val persistenceManager = DrawingPersistenceManager()
    private var duelTimerJob: Job? = null

    init {
        saveSnapshotForUndo()
    }

    private fun saveSnapshotForUndo() {
        val currentState = _uiState.value
        val snapshot = CanvasSnapshot(
            layers = currentState.layers.map { layer ->
                layer.copy(paths = layer.paths.toList())
            },
            activeLayerId = currentState.activeLayerId
        )
        undoStack.add(snapshot)
        if (undoStack.size > 30) {
            undoStack.removeAt(0)
        }
        redoStack.clear()
        updateUndoRedoStatus()
    }

    private fun updateUndoRedoStatus() {
        _uiState.update {
            it.copy(
                canUndo = undoStack.size > 1,
                canRedo = redoStack.isNotEmpty()
            )
        }
    }

    fun undo() {
        if (undoStack.size > 1) {
            val currentSnapshot = undoStack.removeAt(undoStack.lastIndex)
            redoStack.add(currentSnapshot)
            val previousSnapshot = undoStack.last()

            _uiState.update {
                it.copy(
                    layers = previousSnapshot.layers,
                    activeLayerId = previousSnapshot.activeLayerId,
                    selectionState = SelectionState(),
                    inProgressPath = null
                )
            }
            updateUndoRedoStatus()
            speak("Deshacer")
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val snapshotToRestore = redoStack.removeAt(redoStack.lastIndex)
            undoStack.add(snapshotToRestore)

            _uiState.update {
                it.copy(
                    layers = snapshotToRestore.layers,
                    activeLayerId = snapshotToRestore.activeLayerId,
                    selectionState = SelectionState(),
                    inProgressPath = null
                )
            }
            updateUndoRedoStatus()
            speak("Rehacer")
        }
    }

    // Touch Drawing Handlers
    fun onTouchStart(offset: Offset) {
        val state = _uiState.value
        val activeLayer = state.layers.find { it.id == state.activeLayerId }

        if (activeLayer == null || !activeLayer.isVisible) {
            return
        }

        if (activeLayer.isLocked) {
            speak("Capa bloqueada. Desbloquéala para dibujar.")
            return
        }

        if (state.currentTool == ToolType.SELECT) {
            _uiState.update {
                it.copy(
                    selectionState = SelectionState(
                        isActive = true,
                        selectionBounds = Rect(offset.x, offset.y, offset.x + 1f, offset.y + 1f),
                        activeLayerId = state.activeLayerId
                    )
                )
            }
            return
        }

        if (state.currentTool == ToolType.BUCKET) {
            applyBucketFill(offset)
            return
        }

        val point = PointData.fromOffset(offset)
        val newPath = DrawingPath(
            points = listOf(point),
            colorArgb = state.currentColor.toArgb(),
            strokeWidth = state.currentStrokeWidth,
            alpha = if (state.currentTool == ToolType.ERASER) 1.0f else state.currentAlpha,
            toolType = state.currentTool,
            stampShape = if (state.currentTool == ToolType.STAMP) state.currentStampShape else null,
            isRainbow = state.isRainbowMode,
            isGlow = state.isGlowMode,
            isDualBrushMirror = state.isDualBrushMirror
        )

        _uiState.update {
            it.copy(inProgressPath = newPath)
        }
    }

    fun onTouchMove(offset: Offset) {
        val state = _uiState.value

        if (state.currentTool == ToolType.SELECT && state.selectionState.isActive) {
            val start = state.selectionState.selectionBounds?.topLeft ?: offset
            val minX = minOf(start.x, offset.x)
            val maxX = maxOf(start.x, offset.x)
            val minY = minOf(start.y, offset.y)
            val maxY = maxOf(start.y, offset.y)

            val newBounds = Rect(minX, minY, maxX, maxY)

            _uiState.update {
                it.copy(
                    selectionState = it.selectionState.copy(selectionBounds = newBounds)
                )
            }
            return
        }

        val inProgress = state.inProgressPath ?: return
        val updatedPoints = inProgress.points + PointData.fromOffset(offset)

        _uiState.update {
            it.copy(inProgressPath = inProgress.copy(points = updatedPoints))
        }
    }

    fun onTouchEnd() {
        val state = _uiState.value

        if (state.currentTool == ToolType.SELECT && state.selectionState.isActive) {
            val bounds = state.selectionState.selectionBounds
            val activeLayer = state.layers.find { it.id == state.activeLayerId }

            if (bounds != null && activeLayer != null && bounds.width > 10f && bounds.height > 10f) {
                val selectedIds = mutableSetOf<String>()
                activeLayer.paths.forEach { path ->
                    val pathBounds = DrawingUtils.calculatePathBounds(path.points)
                    if (pathBounds != null && bounds.overlaps(pathBounds)) {
                        selectedIds.add(path.id)
                    }
                }

                _uiState.update {
                    it.copy(
                        selectionState = it.selectionState.copy(selectedPathIds = selectedIds)
                    )
                }
            } else {
                _uiState.update { it.copy(selectionState = SelectionState()) }
            }
            return
        }

        val finishedPath = state.inProgressPath ?: return
        if (finishedPath.points.isEmpty()) {
            _uiState.update { it.copy(inProgressPath = null) }
            return
        }

        val updatedLayers = state.layers.map { layer ->
            if (layer.id == state.activeLayerId) {
                layer.copy(paths = layer.paths + finishedPath)
            } else {
                layer
            }
        }

        _uiState.update {
            it.copy(
                layers = updatedLayers,
                inProgressPath = null
            )
        }
        saveSnapshotForUndo()
    }

    private fun applyBucketFill(tapOffset: Offset) {
        val state = _uiState.value
        val fillPath = DrawingPath(
            points = listOf(
                PointData(0f, 0f),
                PointData(3000f, 0f),
                PointData(3000f, 3000f),
                PointData(0f, 3000f)
            ),
            colorArgb = state.currentColor.toArgb(),
            strokeWidth = 1f,
            alpha = state.currentAlpha,
            toolType = ToolType.BUCKET
        )

        val updatedLayers = state.layers.map { layer ->
            if (layer.id == state.activeLayerId) {
                layer.copy(paths = layer.paths + fillPath)
            } else {
                layer
            }
        }

        _uiState.update { it.copy(layers = updatedLayers) }
        saveSnapshotForUndo()
    }

    fun speak(text: String) {
        VoiceAssistant.speak(text, _uiState.value.isVoiceAssistantEnabled)
    }

    fun toggleVoiceAssistant() {
        val newState = !_uiState.value.isVoiceAssistantEnabled
        _uiState.update { it.copy(isVoiceAssistantEnabled = newState) }
        VoiceAssistant.speak(
            if (newState) "Modo de lectura de voz activado" else "Modo de lectura de voz desactivado",
            true
        )
    }

    // Tool Selection
    fun selectTool(tool: ToolType) {
        _uiState.update {
            it.copy(
                currentTool = tool,
                selectionState = if (tool != ToolType.SELECT) SelectionState() else it.selectionState
            )
        }
        val phrase = when (tool) {
            ToolType.PENCIL -> "Lápiz"
            ToolType.BRUSH -> "Pincel"
            ToolType.DUAL_BRUSH -> "Pincel Doble"
            ToolType.CRAYON -> "Cera de colorear"
            ToolType.GLOW -> "Pincel de Neón y Brillo"
            ToolType.STAMP -> "Sellos de figuras"
            ToolType.BUCKET -> "Balde de pintura"
            ToolType.ERASER -> "Goma de borrar"
            ToolType.SELECT -> "Herramienta de selección Lazo"
        }
        speak(phrase)
    }

    fun setColor(color: Color) {
        _uiState.update { it.copy(currentColor = color) }
        speak("Color cambiado")
    }

    fun setStrokeWidth(width: Float) {
        _uiState.update { it.copy(currentStrokeWidth = width) }
    }

    fun setAlpha(alpha: Float) {
        _uiState.update { it.copy(currentAlpha = alpha) }
    }

    fun setStampShape(shape: StampShape) {
        _uiState.update { it.copy(currentStampShape = shape, currentTool = ToolType.STAMP) }
        val shapeName = when (shape) {
            StampShape.STAR -> "Estrella"
            StampShape.HEART -> "Corazón"
            StampShape.SPARKLE -> "Chispa mágica"
            StampShape.RAINBOW -> "Arcoíris"
            StampShape.FLOWER -> "Flor"
            StampShape.PAW -> "Huellita"
            StampShape.SUN -> "Sol brillante"
        }
        speak("Sello de $shapeName")
    }

    fun toggleRainbowMode() {
        val next = !_uiState.value.isRainbowMode
        _uiState.update { it.copy(isRainbowMode = next) }
        speak(if (next) "Efecto Arcoíris activado" else "Efecto Arcoíris desactivado")
    }

    fun toggleGlowMode() {
        val next = !_uiState.value.isGlowMode
        _uiState.update { it.copy(isGlowMode = next) }
        speak(if (next) "Efecto de Brillo activado" else "Efecto de Brillo desactivado")
    }

    fun toggleDualBrushMirrorMode() {
        val next = !_uiState.value.isDualBrushMirror
        _uiState.update { it.copy(isDualBrushMirror = next) }
        speak(if (next) "Modo Espejo Simétrico activado" else "Modo Doble Paralelo activado")
    }

    fun setStencil(type: StencilType) {
        _uiState.update {
            it.copy(
                activeStencil = type,
                showStencilModal = false
            )
        }
        speak("Plantilla ${type.title} seleccionada")
    }

    // Layer Management
    fun addLayer() {
        val state = _uiState.value
        if (state.layers.size >= 8) return

        val newId = "layer_${UUID.randomUUID().toString().take(6)}"
        val newLayer = DrawingLayer(
            id = newId,
            name = "Capa ${state.layers.size + 1}"
        )

        val updatedLayers = state.layers + newLayer
        _uiState.update {
            it.copy(
                layers = updatedLayers,
                activeLayerId = newId
            )
        }
        saveSnapshotForUndo()
        speak("Nueva capa añadida")
    }

    fun deleteLayer(layerId: String) {
        val state = _uiState.value
        if (state.layers.size <= 1) return

        val updatedLayers = state.layers.filter { it.id != layerId }
        val newActiveId = if (state.activeLayerId == layerId) updatedLayers.last().id else state.activeLayerId

        _uiState.update {
            it.copy(
                layers = updatedLayers,
                activeLayerId = newActiveId
            )
        }
        saveSnapshotForUndo()
        speak("Capa eliminada")
    }

    fun selectLayer(layerId: String) {
        _uiState.update { it.copy(activeLayerId = layerId) }
        val layerName = _uiState.value.layers.find { it.id == layerId }?.name ?: "Capa"
        speak("Seleccionada $layerName")
    }

    fun toggleLayerVisibility(layerId: String) {
        var isVis = true
        val updatedLayers = _uiState.value.layers.map { layer ->
            if (layer.id == layerId) {
                isVis = !layer.isVisible
                layer.copy(isVisible = isVis)
            } else layer
        }
        _uiState.update { it.copy(layers = updatedLayers) }
        speak(if (isVis) "Capa visible" else "Capa oculta")
    }

    fun toggleLayerLock(layerId: String) {
        var isLocked = false
        val updatedLayers = _uiState.value.layers.map { layer ->
            if (layer.id == layerId) {
                isLocked = !layer.isLocked
                layer.copy(isLocked = isLocked)
            } else layer
        }
        _uiState.update { it.copy(layers = updatedLayers) }
        speak(if (isLocked) "Capa bloqueada" else "Capa desbloqueada")
    }

    fun setLayerOpacity(layerId: String, opacity: Float) {
        val updatedLayers = _uiState.value.layers.map { layer ->
            if (layer.id == layerId) layer.copy(opacity = opacity) else layer
        }
        _uiState.update { it.copy(layers = updatedLayers) }
    }

    fun duplicateLayer(layerId: String) {
        val state = _uiState.value
        val layerToDup = state.layers.find { it.id == layerId } ?: return
        val newId = "layer_${UUID.randomUUID().toString().take(6)}"
        val newLayer = layerToDup.copy(
            id = newId,
            name = "${layerToDup.name} (Copia)",
            paths = layerToDup.paths.map { it.copy(id = UUID.randomUUID().toString()) }
        )

        val updatedLayers = state.layers + newLayer
        _uiState.update {
            it.copy(
                layers = updatedLayers,
                activeLayerId = newId
            )
        }
        saveSnapshotForUndo()
    }

    fun moveLayerUp(layerId: String) {
        val state = _uiState.value
        val index = state.layers.indexOfFirst { it.id == layerId }
        if (index < state.layers.size - 1) {
            val mutable = state.layers.toMutableList()
            val item = mutable.removeAt(index)
            mutable.add(index + 1, item)
            _uiState.update { it.copy(layers = mutable) }
            saveSnapshotForUndo()
        }
    }

    fun moveLayerDown(layerId: String) {
        val state = _uiState.value
        val index = state.layers.indexOfFirst { it.id == layerId }
        if (index > 0) {
            val mutable = state.layers.toMutableList()
            val item = mutable.removeAt(index)
            mutable.add(index - 1, item)
            _uiState.update { it.copy(layers = mutable) }
            saveSnapshotForUndo()
        }
    }

    fun renameLayer(layerId: String, newName: String) {
        if (newName.isBlank()) return
        val updatedLayers = _uiState.value.layers.map { layer ->
            if (layer.id == layerId) layer.copy(name = newName.trim()) else layer
        }
        _uiState.update { it.copy(layers = updatedLayers) }
        speak("Capa renombrada a ${newName.trim()}")
    }

    fun clearLayer(layerId: String) {
        val updatedLayers = _uiState.value.layers.map { layer ->
            if (layer.id == layerId) layer.copy(paths = emptyList()) else layer
        }
        _uiState.update { it.copy(layers = updatedLayers) }
        saveSnapshotForUndo()
        speak("Contenido de capa borrado")
    }

    fun mergeLayerDown(layerId: String) {
        val state = _uiState.value
        val index = state.layers.indexOfFirst { it.id == layerId }
        if (index <= 0) return // Cannot merge bottom layer down

        val topLayer = state.layers[index]
        val targetLayer = state.layers[index - 1]

        // Combine paths with current layer opacity baked into alpha
        val mergedPaths = targetLayer.paths + topLayer.paths.map { path ->
            if (topLayer.opacity < 1.0f) {
                path.copy(alpha = path.alpha * topLayer.opacity)
            } else path
        }

        val updatedTarget = targetLayer.copy(paths = mergedPaths)
        val mutableLayers = state.layers.toMutableList()
        mutableLayers[index - 1] = updatedTarget
        mutableLayers.removeAt(index)

        _uiState.update {
            it.copy(
                layers = mutableLayers,
                activeLayerId = updatedTarget.id
            )
        }
        saveSnapshotForUndo()
        speak("Capas combinadas")
    }

    // Selection Actions
    fun transformSelection(translation: Offset, scaleDelta: Float, rotationDelta: Float) {
        val sel = _uiState.value.selectionState
        if (!sel.isActive) return

        _uiState.update {
            it.copy(
                selectionState = sel.copy(
                    translation = sel.translation + translation,
                    scale = (sel.scale * scaleDelta).coerceIn(0.2f, 5.0f),
                    rotation = sel.rotation + rotationDelta
                )
            )
        }
    }

    fun commitSelectionTransform() {
        val state = _uiState.value
        val sel = state.selectionState
        if (!sel.isActive || sel.selectedPathIds.isEmpty()) {
            _uiState.update { it.copy(selectionState = SelectionState()) }
            return
        }

        val updatedLayers = state.layers.map { layer ->
            if (layer.id == state.activeLayerId) {
                val updatedPaths = layer.paths.map { path ->
                    if (path.id in sel.selectedPathIds) {
                        path.copy(
                            translationX = path.translationX + sel.translation.x,
                            translationY = path.translationY + sel.translation.y,
                            scaleX = path.scaleX * sel.scale,
                            scaleY = path.scaleY * sel.scale,
                            rotationDegrees = path.rotationDegrees + sel.rotation
                        )
                    } else {
                        path
                    }
                }
                layer.copy(paths = updatedPaths)
            } else {
                layer
            }
        }

        _uiState.update {
            it.copy(
                layers = updatedLayers,
                selectionState = SelectionState()
            )
        }
        saveSnapshotForUndo()
    }

    fun deleteSelection() {
        val state = _uiState.value
        val sel = state.selectionState
        if (!sel.isActive) return

        val updatedLayers = state.layers.map { layer ->
            if (layer.id == state.activeLayerId) {
                layer.copy(paths = layer.paths.filterNot { it.id in sel.selectedPathIds })
            } else {
                layer
            }
        }

        _uiState.update {
            it.copy(
                layers = updatedLayers,
                selectionState = SelectionState()
            )
        }
        saveSnapshotForUndo()
    }

    fun duplicateSelection() {
        val state = _uiState.value
        val sel = state.selectionState
        if (!sel.isActive || sel.selectedPathIds.isEmpty()) return

        val activeLayer = state.layers.find { it.id == state.activeLayerId } ?: return
        val newPaths = mutableListOf<DrawingPath>()

        activeLayer.paths.forEach { path ->
            if (path.id in sel.selectedPathIds) {
                val dup = path.copy(
                    id = UUID.randomUUID().toString(),
                    translationX = path.translationX + sel.translation.x + 30f,
                    translationY = path.translationY + sel.translation.y + 30f,
                    scaleX = path.scaleX * sel.scale,
                    scaleY = path.scaleY * sel.scale,
                    rotationDegrees = path.rotationDegrees + sel.rotation
                )
                newPaths.add(dup)
            }
        }

        val updatedLayers = state.layers.map { layer ->
            if (layer.id == state.activeLayerId) {
                layer.copy(paths = layer.paths + newPaths)
            } else {
                layer
            }
        }

        _uiState.update {
            it.copy(
                layers = updatedLayers,
                selectionState = SelectionState()
            )
        }
        saveSnapshotForUndo()
    }

    // Canvas Clear
    fun showClearDialog() {
        _uiState.update { it.copy(showClearConfirmDialog = true) }
    }

    fun dismissClearDialog() {
        _uiState.update { it.copy(showClearConfirmDialog = false) }
    }

    fun clearCurrentLayer() {
        val state = _uiState.value
        val updatedLayers = state.layers.map { layer ->
            if (layer.id == state.activeLayerId) layer.copy(paths = emptyList()) else layer
        }
        _uiState.update {
            it.copy(
                layers = updatedLayers,
                showClearConfirmDialog = false
            )
        }
        saveSnapshotForUndo()
        speak("Capa limpiada")
    }

    fun clearAllCanvas() {
        val state = _uiState.value
        val updatedLayers = state.layers.map { layer ->
            layer.copy(paths = emptyList())
        }
        _uiState.update {
            it.copy(
                layers = updatedLayers,
                activeStencil = StencilType.NONE,
                showClearConfirmDialog = false,
                selectionState = SelectionState()
            )
        }
        saveSnapshotForUndo()
        speak("Lienzo borrado por completo")
    }

    // Modals & Dialogs
    fun toggleStencilModal() {
        _uiState.update { it.copy(showStencilModal = !it.showStencilModal) }
    }

    fun toggleHelpModal() {
        _uiState.update { it.copy(showHelpModal = !it.showHelpModal) }
    }

    fun toggleColorPickerModal() {
        _uiState.update { it.copy(showColorPickerModal = !it.showColorPickerModal) }
    }

    fun setDrawingTitle(title: String) {
        _uiState.update { it.copy(drawingTitle = title) }
    }

    // Save & Load Database
    fun saveDrawing(context: Context, bitmapThumbnail: Bitmap) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val state = _uiState.value
            val id = persistenceManager.saveDrawing(
                context = context,
                title = state.drawingTitle,
                currentDrawingId = state.currentDrawingId,
                layers = state.layers,
                bitmapThumbnail = bitmapThumbnail
            )
            _uiState.update {
                it.copy(
                    currentDrawingId = id,
                    isSaving = false,
                    showSaveSuccess = true
                )
            }
        }
    }

    fun dismissSaveSuccess() {
        _uiState.update { it.copy(showSaveSuccess = false) }
    }

    fun loadDrawingFromEntity(entity: DrawingEntity) {
        viewModelScope.launch {
            val loadedLayers = persistenceManager.loadDrawingLayers(entity)
            if (loadedLayers.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        layers = loadedLayers,
                        activeLayerId = loadedLayers.first().id,
                        currentDrawingId = entity.id,
                        drawingTitle = entity.title,
                        selectionState = SelectionState()
                    )
                }
                undoStack.clear()
                redoStack.clear()
                saveSnapshotForUndo()
            }
        }
    }

    fun createNewDrawing() {
        _uiState.update {
            CanvasUiState(
                layers = listOf(DrawingLayer(id = "layer_1", name = "Capa 1")),
                activeLayerId = "layer_1",
                currentDrawingId = null,
                drawingTitle = "Mi Obra Maestra"
            )
        }
        undoStack.clear()
        redoStack.clear()
        saveSnapshotForUndo()
    }

    // GPU Acceleration Control
    fun toggleGpuAcceleration() {
        val nextState = !_uiState.value.isGpuAccelerated
        _uiState.update {
            it.copy(
                isGpuAccelerated = nextState,
                gpuInfoText = if (nextState) "Aceleración por GPU Activa (60 FPS)" else "Renderizado por Software / Canvas Standard"
            )
        }
        speak(if (nextState) "Aceleración por GPU activada" else "Aceleración por GPU desactivada")
    }

    // 1v1 Local Kid Drawing Duel Mode Logic
    fun start1v1Mode(timeSeconds: Int = 30) {
        duelTimerJob?.cancel()

        _uiState.update {
            it.copy(
                is1v1ModeActive = true,
                duelCurrentTurn = 1,
                duelTimerSeconds = timeSeconds,
                duelTimeRemaining = timeSeconds,
                isDuelTimerRunning = false,
                aiPromptLoading = true,
                duelPrompt = "Obteniendo tema mágico...",
                duelPlayer1Layers = emptyList(),
                duelPlayer2Layers = emptyList(),
                showDuelResultModal = false,
                aiJudgeResult = null,
                duelP1Rating = 5,
                duelP2Rating = 5,
                layers = listOf(DrawingLayer(id = "layer_1", name = "Niño 1")),
                activeLayerId = "layer_1"
            )
        }

        viewModelScope.launch {
            val aiPrompt = com.example.data.remote.AiJudgeService.generateCreativePrompt()
            _uiState.update {
                it.copy(
                    duelPrompt = aiPrompt,
                    aiPromptLoading = false,
                    isDuelTimerRunning = true
                )
            }
            speak("Duelo 1 contra 1 iniciado. Turno de Niño 1. Tienes $timeSeconds segundos. Tema: $aiPrompt")
            runDuelTimer()
        }
    }

    fun pauseResumeDuelTimer() {
        val currentState = _uiState.value.isDuelTimerRunning
        _uiState.update { it.copy(isDuelTimerRunning = !currentState) }
        speak(if (!currentState) "Tiempo reanudado" else "Tiempo pausado")
    }

    fun finishTurnEarly() {
        _uiState.update { it.copy(duelTimeRemaining = 0) }
        handleTurnEnd()
    }

    private fun runDuelTimer() {
        duelTimerJob = viewModelScope.launch(Dispatchers.Main) {
            while (_uiState.value.is1v1ModeActive && !_uiState.value.showDuelResultModal) {
                delay(1000L)
                val state = _uiState.value
                if (state.isDuelTimerRunning && state.duelTimeRemaining > 0) {
                    val newTime = state.duelTimeRemaining - 1
                    _uiState.update { it.copy(duelTimeRemaining = newTime) }

                    if (newTime == 5) {
                        speak("¡Quedan 5 segundos!")
                    } else if (newTime == 0) {
                        handleTurnEnd()
                    }
                }
            }
        }
    }

    private fun handleTurnEnd() {
        val state = _uiState.value
        if (state.duelCurrentTurn == 1) {
            val player1Layers = state.layers.toList()
            _uiState.update {
                it.copy(
                    duelPlayer1Layers = player1Layers,
                    duelCurrentTurn = 2,
                    duelTimeRemaining = state.duelTimerSeconds,
                    isDuelTimerRunning = false,
                    layers = listOf(DrawingLayer(id = "layer_1", name = "Niño 2")),
                    activeLayerId = "layer_1"
                )
            }
            speak("¡Tiempo agotado para Niño 1! Es el turno del Niño 2. Presiona Iniciar cuando estés listo.")
        } else {
            val player2Layers = state.layers.toList()
            _uiState.update {
                it.copy(
                    duelPlayer2Layers = player2Layers,
                    isDuelTimerRunning = false,
                    isAiEvaluating = true,
                    showDuelResultModal = true
                )
            }
            speak("¡Duelo finalizado! El Juez Mágico AI está analizando ambos dibujos.")

            viewModelScope.launch {
                val aiResult = try {
                    val p1Bitmap = com.example.utils.DrawingUtils.renderLayersToBitmap(state.duelPlayer1Layers)
                    val p2Bitmap = com.example.utils.DrawingUtils.renderLayersToBitmap(player2Layers)
                    com.example.data.remote.AiJudgeService.evaluateDrawingsWithAi(
                        p1Bitmap = p1Bitmap,
                        p2Bitmap = p2Bitmap,
                        prompt = state.duelPrompt
                    )
                } catch (e: Exception) {
                    android.util.Log.e("CanvasViewModel", "Error evaluating duel drawings", e)
                    com.example.data.remote.AiJudgeResult(
                        p1Feedback = "¡Hermoso dibujo con colores muy expresivos!",
                        p2Feedback = "¡Gran trabajo de imaginación y creatividad!",
                        p1Stars = 5,
                        p2Stars = 5,
                        verdictTitle = "¡EMPATE MÁGICO DE ARTISTAS! 🏆🎨",
                        verdictAudioSummary = "¡Felicidades a ambos niños por sus maravillosos dibujos!"
                    )
                }

                _uiState.update {
                    it.copy(
                        isAiEvaluating = false,
                        aiJudgeResult = aiResult,
                        duelP1Rating = aiResult.p1Stars,
                        duelP2Rating = aiResult.p2Stars
                    )
                }

                speak(aiResult.verdictAudioSummary)
            }
        }
    }

    fun startPlayer2Timer() {
        _uiState.update { it.copy(isDuelTimerRunning = true) }
        speak("¡A dibujar Niño 2!")
    }

    fun setDuelRating(player: Int, stars: Int) {
        if (player == 1) {
            _uiState.update { it.copy(duelP1Rating = stars) }
        } else {
            _uiState.update { it.copy(duelP2Rating = stars) }
        }
    }

    fun exit1v1Mode() {
        duelTimerJob?.cancel()
        _uiState.update {
            it.copy(
                is1v1ModeActive = false,
                isDuelTimerRunning = false,
                showDuelResultModal = false,
                layers = listOf(DrawingLayer(id = "layer_1", name = "Capa 1")),
                activeLayerId = "layer_1"
            )
        }
        speak("Modo libre reactivado")
    }
}
