package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MergeType
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.DrawingLayer
import com.example.data.models.SelectionState
import com.example.ui.canvas.CanvasPathRenderer
import com.example.ui.canvas.CanvasUiState

@Composable
fun LayerManagerSheet(
    uiState: CanvasUiState,
    onSelectLayer: (String) -> Unit,
    onAddLayer: () -> Unit,
    onDeleteLayer: (String) -> Unit,
    onToggleVisibility: (String) -> Unit,
    onToggleLock: (String) -> Unit,
    onSetOpacity: (String, Float) -> Unit,
    onDuplicateLayer: (String) -> Unit,
    onMoveUp: (String) -> Unit,
    onMoveDown: (String) -> Unit,
    onMergeDown: (String) -> Unit,
    onClearLayer: (String) -> Unit,
    onRenameLayer: (String, String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var layerToRename by remember { mutableStateOf<DrawingLayer?>(null) }

    Surface(
        modifier = modifier
            .fillMaxWidth(0.92f)
            .fillMaxHeight(0.88f),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFFAF0CA), // Warm kid cream card
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🥞", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gestor de Capas Avanzado",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D3B66)
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.background(Color(0xFFEE9B00), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "Crea, combina y organiza tus dibujos por capas independientes.",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            // Add Layer Button
            Button(
                onClick = onAddLayer,
                enabled = uiState.layers.size < 8,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D3B66),
                    disabledContainerColor = Color.Gray
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (uiState.layers.size < 8) "Añadir Nueva Capa (+)" else "Límite de capas alcanzado (8)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Layers List (Top of list is top of stack)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(uiState.layers.reversed()) { idx, layer ->
                    val originalIndex = uiState.layers.size - 1 - idx
                    val isSelected = layer.id == uiState.activeLayerId

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color(0xFF0D3B66) else Color(0xFFE0E0E0),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelectLayer(layer.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFE0F2FE) else Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Mini Live Preview Thumbnail
                                    LayerThumbnail(layer = layer)

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = layer.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = Color(0xFF333333)
                                            )
                                            IconButton(
                                                onClick = { layerToRename = layer },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Renombrar capa",
                                                    tint = Color(0xFF0D3B66),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = if (isSelected) "✏️ Capa activa (${layer.paths.size} trazos)" else "Capa ${originalIndex + 1} (${layer.paths.size} trazos)",
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color(0xFF0D3B66) else Color(0xFF666666)
                                        )
                                    }
                                }

                                // Layer Action Buttons Grid
                                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    // Visibility
                                    IconButton(
                                        onClick = { onToggleVisibility(layer.id) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (layer.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Visibilidad",
                                            tint = if (layer.isVisible) Color(0xFF0D3B66) else Color.Gray
                                        )
                                    }

                                    // Lock
                                    IconButton(
                                        onClick = { onToggleLock(layer.id) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (layer.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                            contentDescription = "Bloquear",
                                            tint = if (layer.isLocked) Color(0xFFCA6702) else Color.Gray
                                        )
                                    }

                                    // Move Up
                                    IconButton(
                                        onClick = { onMoveUp(layer.id) },
                                        enabled = originalIndex < uiState.layers.size - 1,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowUpward,
                                            contentDescription = "Subir capa",
                                            tint = if (originalIndex < uiState.layers.size - 1) Color(0xFF0D3B66) else Color.LightGray
                                        )
                                    }

                                    // Move Down
                                    IconButton(
                                        onClick = { onMoveDown(layer.id) },
                                        enabled = originalIndex > 0,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = "Bajar capa",
                                            tint = if (originalIndex > 0) Color(0xFF0D3B66) else Color.LightGray
                                        )
                                    }

                                    // Merge Down
                                    if (originalIndex > 0) {
                                        IconButton(
                                            onClick = { onMergeDown(layer.id) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MergeType,
                                                contentDescription = "Combinar con capa inferior",
                                                tint = Color(0xFF2A9D8F)
                                            )
                                        }
                                    }

                                    // Clear Layer
                                    IconButton(
                                        onClick = { onClearLayer(layer.id) },
                                        enabled = layer.paths.isNotEmpty(),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CleaningServices,
                                            contentDescription = "Limpiar capa",
                                            tint = if (layer.paths.isNotEmpty()) Color(0xFFE76F51) else Color.LightGray
                                        )
                                    }

                                    // Duplicate
                                    IconButton(
                                        onClick = { onDuplicateLayer(layer.id) },
                                        enabled = uiState.layers.size < 8,
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Duplicar capa",
                                            tint = if (uiState.layers.size < 8) Color(0xFF0D3B66) else Color.LightGray
                                        )
                                    }

                                    // Delete
                                    if (uiState.layers.size > 1) {
                                        IconButton(
                                            onClick = { onDeleteLayer(layer.id) },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar capa",
                                                tint = Color(0xFFEE9B00)
                                            )
                                        }
                                    }
                                }
                            }

                            // Layer Opacity Slider
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Opacidad:",
                                    fontSize = 11.sp,
                                    color = Color(0xFF666666),
                                    modifier = Modifier.width(65.dp)
                                )
                                Slider(
                                    value = layer.opacity,
                                    onValueChange = { opacity -> onSetOpacity(layer.id, opacity) },
                                    valueRange = 0.0f..1.0f,
                                    colors = SliderDefaults.colors(thumbColor = Color(0xFF0D3B66)),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "${(layer.opacity * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog for renaming layer
    if (layerToRename != null) {
        var textValue by remember { mutableStateOf(layerToRename!!.name) }
        AlertDialog(
            onDismissRequest = { layerToRename = null },
            title = {
                Text(
                    text = "Renombrar Capa ✏️",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D3B66)
                )
            },
            text = {
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = { Text("Nombre de la capa") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRenameLayer(layerToRename!!.id, textValue)
                        layerToRename = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D3B66))
                ) {
                    Text("Guardar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { layerToRename = null }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
private fun LayerThumbnail(
    layer: DrawingLayer,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            CanvasPathRenderer.drawLayerContent(
                drawScope = this,
                layer = layer,
                selectionState = SelectionState(),
                activeLayerId = ""
            )
        }
    }
}
