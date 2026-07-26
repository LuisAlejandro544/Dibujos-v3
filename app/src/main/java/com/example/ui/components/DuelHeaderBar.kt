package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.canvas.CanvasUiState

@Composable
fun DuelHeaderBar(
    uiState: CanvasUiState,
    onPauseResumeTimer: () -> Unit,
    onFinishTurnEarly: () -> Unit,
    onStartPlayer2Timer: () -> Unit,
    onExitDuel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerColor = if (uiState.duelCurrentTurn == 1) Color(0xFF2196F3) else Color(0xFFFF5722)
    val playerName = if (uiState.duelCurrentTurn == 1) uiState.duelPlayer1Name else uiState.duelPlayer2Name

    val progress = (uiState.duelTimeRemaining.toFloat() / uiState.duelTimerSeconds.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timerProgress")

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        color = playerColor.copy(alpha = 0.15f),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Exit Duel
                IconButton(
                    onClick = onExitDuel,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Salir del Duelo",
                        tint = Color.Red
                    )
                }

                // Active Player Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(playerColor)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "TURNO: $playerName",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // Timer Dial
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(44.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(44.dp),
                        color = playerColor,
                        strokeWidth = 4.dp,
                        trackColor = Color.LightGray.copy(alpha = 0.4f)
                    )
                    Text(
                        text = "${uiState.duelTimeRemaining}s",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = playerColor
                    )
                }

                // Control Timer Actions
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (uiState.duelCurrentTurn == 2 && !uiState.isDuelTimerRunning && uiState.duelTimeRemaining == uiState.duelTimerSeconds) {
                        // Start Player 2
                        IconButton(
                            onClick = onStartPlayer2Timer,
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Iniciar Turno Niño 2",
                                tint = Color.White
                            )
                        }
                    } else {
                        IconButton(
                            onClick = onPauseResumeTimer,
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color.White, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (uiState.isDuelTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Pausar/Reanudar",
                                tint = playerColor
                            )
                        }
                        IconButton(
                            onClick = onFinishTurnEarly,
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Terminar Turno",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            if (uiState.duelPrompt.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = uiState.duelPrompt,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        fontSize = 13.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
