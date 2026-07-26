package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    onStartFreeDrawing: () -> Unit,
    onStart1v1Duel: () -> Unit,
    onOpenGallery: () -> Unit,
    onOpenToolsGuide: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onSpeak("¡Bienvenido al Menú Principal de KidsDraw! Elige una opción para empezar a divertirte.")
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFFFFDF5)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF9E6),
                            Color(0xFFFFF0C2),
                            Color(0xFFFFE599)
                        )
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Title Header Card
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "🎨",
                                    fontSize = 42.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "KidsDraw Canvas",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF4A2810)
                                    )
                                    Text(
                                        text = "¡Mundo Mágico de Arte y Dibujo!",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF595E)
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    onSpeak("¡Bienvenido al Menú Principal de KidsDraw! Toca cualquier opción para empezar.")
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFFF70A6), CircleShape)
                                    .testTag("menu_speak_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Escuchar menú",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 1. FREE DRAWING MAIN CARD (EMPEZAR A DIBUJAR)
                item {
                    MainMenuOptionCard(
                        title = "🎨 Dibujo Libre",
                        subtitle = "Lienzo mágico con pinceles, capas y sellos divertidos",
                        badgeText = "¡RECOMENDADO!",
                        badgeColor = Color(0xFFFF595E),
                        backgroundColor = Color(0xFFFFE8E8),
                        borderColor = Color(0xFFFF595E),
                        icon = Icons.Default.Brush,
                        iconTint = Color(0xFFFF595E),
                        onClick = {
                            onSpeak("¡A dibujar en el lienzo libre!")
                            onStartFreeDrawing()
                        },
                        testTag = "menu_free_drawing_button"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 2. 1v1 DUEL MAIN CARD (DUELO 1VS1)
                item {
                    MainMenuOptionCard(
                        title = "⚔️ Duelo 1vs1 Mágico",
                        subtitle = "Compite con un amigo en 30s. ¡El Juez AI calificará sus dibujos!",
                        badgeText = "🤖 CON AI JUDGE",
                        badgeColor = Color(0xFFFF5722),
                        backgroundColor = Color(0xFFFFF0EB),
                        borderColor = Color(0xFFFF5722),
                        icon = Icons.Default.SportsEsports,
                        iconTint = Color(0xFFFF5722),
                        onClick = {
                            onSpeak("¡Iniciando Duelo de Dibujo 1vs1 con Juez de Inteligencia Artificial!")
                            onStart1v1Duel()
                        },
                        testTag = "menu_1v1_duel_button"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 3. GALLERY CARD (MIS DIBUJOS)
                item {
                    MainMenuOptionCard(
                        title = "📁 Mis Dibujos",
                        subtitle = "Ver, continuar y compartir tus obras de arte guardadas",
                        badgeText = "GALERÍA",
                        badgeColor = Color(0xFF6A4C93),
                        backgroundColor = Color(0xFFF3E8FF),
                        borderColor = Color(0xFF6A4C93),
                        icon = Icons.Default.Folder,
                        iconTint = Color(0xFF6A4C93),
                        onClick = {
                            onSpeak("Abriendo tu galería de arte guardada.")
                            onOpenGallery()
                        },
                        testTag = "menu_gallery_button"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 4. TOOLS GUIDE CARD (CONOCER HERRAMIENTAS)
                item {
                    MainMenuOptionCard(
                        title = "📖 Guía de Herramientas",
                        subtitle = "Aprende qué hace cada lápiz, pincel y efecto especial",
                        badgeText = "APRENDER",
                        badgeColor = Color(0xFF06D6A0),
                        backgroundColor = Color(0xFFE6F9F4),
                        borderColor = Color(0xFF06D6A0),
                        icon = Icons.Default.HelpOutline,
                        iconTint = Color(0xFF06D6A0),
                        onClick = {
                            onSpeak("Abriendo la guía interactiva de herramientas.")
                            onOpenToolsGuide()
                        },
                        testTag = "menu_tools_guide_button"
                    )
                }
            }
        }
    }
}

@Composable
private fun MainMenuOptionCard(
    title: String,
    subtitle: String,
    badgeText: String,
    badgeColor: Color,
    backgroundColor: Color,
    borderColor: Color,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2B2D42)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF555555),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(borderColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Entrar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
