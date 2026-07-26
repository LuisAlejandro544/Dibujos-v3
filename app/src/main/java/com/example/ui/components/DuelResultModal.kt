package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.DrawingLayer
import com.example.data.models.DrawingPath
import com.example.data.models.SelectionState
import com.example.data.models.StampShape
import com.example.data.models.ToolType
import com.example.ui.canvas.CanvasUiState
import com.example.ui.canvas.Stencils
import com.example.utils.DrawingUtils

@Composable
fun DuelResultModal(
    uiState: CanvasUiState,
    onSetP1Rating: (Int) -> Unit,
    onSetP2Rating: (Int) -> Unit,
    onPlayAgain: () -> Unit,
    onExitDuel: () -> Unit,
    onSpeak: (String) -> Unit
) {
    Dialog(onDismissRequest = onExitDuel) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFFFFF9E6),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Celebration Banner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trofeo",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "¡DUELO FINALIZADO!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4A2810),
                            fontSize = 20.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tema: ${uiState.duelPrompt}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513)
                    ),
                    textAlign = TextAlign.Center
                )

                // AI Judge Banner / Verdict Card
                if (uiState.isAiEvaluating) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF2E7D32),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "🤖 El Juez Mágico AI está analizando los dibujos con visión...",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                fontSize = 12.sp
                            )
                        }
                    }
                } else if (uiState.aiJudgeResult != null) {
                    val result = uiState.aiJudgeResult
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🤖 Veredicto del Juez Mágico AI",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFE65100),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = result.verdictTitle,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${result.verdictAudioSummary}\"",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = Color(0xFF4E342E),
                                    fontSize = 12.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Side-By-Side Artwork Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Player 1 Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.duelPlayer1Name,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2196F3),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                            ) {
                                MiniLayerCanvas(layers = uiState.duelPlayer1Layers)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            StarRatingRow(
                                rating = uiState.duelP1Rating,
                                color = Color(0xFF2196F3),
                                onSelect = {
                                    onSetP1Rating(it)
                                    onSpeak("Niño 1 recibe $it estrellas")
                                }
                            )
                            if (uiState.aiJudgeResult != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🤖 ${uiState.aiJudgeResult.p1Feedback}",
                                    fontSize = 11.sp,
                                    color = Color(0xFF0D47A1),
                                    lineHeight = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Player 2 Card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.duelPlayer2Name,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF5722),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                            ) {
                                MiniLayerCanvas(layers = uiState.duelPlayer2Layers)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            StarRatingRow(
                                rating = uiState.duelP2Rating,
                                color = Color(0xFFFF5722),
                                onSelect = {
                                    onSetP2Rating(it)
                                    onSpeak("Niño 2 recibe $it estrellas")
                                }
                            )
                            if (uiState.aiJudgeResult != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🤖 ${uiState.aiJudgeResult.p2Feedback}",
                                    fontSize = 11.sp,
                                    color = Color(0xFFBF360C),
                                    lineHeight = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Otro Duelo 🚀", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onExitDuel,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Modo Libre 🎨", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StarRatingRow(
    rating: Int,
    color: Color,
    onSelect: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (star in 1..5) {
            Icon(
                imageVector = if (star <= rating) Icons.Default.Star else Icons.Outlined.Star,
                contentDescription = "$star estrellas",
                tint = if (star <= rating) Color(0xFFFFD700) else Color.LightGray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSelect(star) }
            )
        }
    }
}

@Composable
private fun MiniLayerCanvas(layers: List<DrawingLayer>) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    ) {
        layers.forEach { layer ->
            if (layer.isVisible) {
                layer.paths.forEach { pathModel ->
                    val pts = pathModel.points
                    if (pts.isNotEmpty()) {
                        val composePath = Path().apply {
                            moveTo(pts[0].x, pts[0].y)
                            for (i in 1 until pts.size) {
                                val prev = pts[i - 1]
                                val curr = pts[i]
                                quadraticTo(prev.x, prev.y, (prev.x + curr.x) / 2f, (prev.y + curr.y) / 2f)
                            }
                        }
                        drawPath(
                            path = composePath,
                            color = pathModel.getColor().copy(alpha = pathModel.alpha),
                            style = Stroke(width = (pathModel.strokeWidth * 0.4f).coerceAtLeast(4f))
                        )
                    }
                }
            }
        }
    }
}
