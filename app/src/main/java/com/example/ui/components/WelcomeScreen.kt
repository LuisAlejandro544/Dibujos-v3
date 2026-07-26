package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class FeatureItem(
    val emoji: String,
    val title: String,
    val description: String,
    val voiceText: String,
    val badgeColor: Color
)

val KIDS_APP_FEATURES = listOf(
    FeatureItem(
        emoji = "✏️",
        title = "Lápiz Mágico",
        description = "Líneas finas y precisas para hacer bocetos o escribir.",
        voiceText = "El Lápiz sirve para hacer líneas finas y dibujar detalles precisos.",
        badgeColor = Color(0xFFFF595E)
    ),
    FeatureItem(
        emoji = "🖌️",
        title = "Pincel Suave",
        description = "Trazo fluido y suave ideal para pintar grandes figuras.",
        voiceText = "El Pincel pinta con trazos suaves e intensos de colores.",
        badgeColor = Color(0xFFFFCA3A)
    ),
    FeatureItem(
        emoji = "✌️",
        title = "Pincel Doble",
        description = "¡Dibuja dos líneas paralelas mágicas a la vez!",
        voiceText = "El Pincel Doble traza dos líneas paralelas brillantes al mismo tiempo.",
        badgeColor = Color(0xFFFF3366)
    ),
    FeatureItem(
        emoji = "🖍️",
        title = "Cera de Colorear",
        description = "Efecto de textura pastel como las ceras reales.",
        voiceText = "La Cera de colorear le da una textura divertida a tu dibujo.",
        badgeColor = Color(0xFF8AC926)
    ),
    FeatureItem(
        emoji = "✨",
        title = "Pincel Neón",
        description = "Trazos luminosos que brillan con resplandor mágico.",
        voiceText = "El Pincel Neón crea líneas brillantes y luminosas en el lienzo.",
        badgeColor = Color(0xFF1982C4)
    ),
    FeatureItem(
        emoji = "🌟",
        title = "Sellos Divertidos",
        description = "Estampa estrellas, corazones, soles y arcoíris con un toque.",
        voiceText = "Los Sellos te permiten pegar estrellas, corazones y soles mágicos.",
        badgeColor = Color(0xFF6A4C93)
    ),
    FeatureItem(
        emoji = "🪣",
        title = "Balde de Pintura",
        description = "Rellena el lienzo o figuras enteras de un solo toque.",
        voiceText = "El Balde de pintura llena de color todo el fondo o una figura.",
        badgeColor = Color(0xFFFF924C)
    ),
    FeatureItem(
        emoji = "🧽",
        title = "Goma Mágica",
        description = "Borra fácilmente trazos que quieras corregir.",
        voiceText = "La Goma de borrar limpia las partes que quieras cambiar.",
        badgeColor = Color(0xFF90E0EF)
    ),
    FeatureItem(
        emoji = "🥞",
        title = "Capas de Dibujo",
        description = "Dibuja en diferentes hojas transparentes sin estropear el resto.",
        voiceText = "Las Capas son hojas transparentes para dibujar sin borrar el fondo.",
        badgeColor = Color(0xFFB5179E)
    ),
    FeatureItem(
        emoji = "🔊",
        title = "Asistente de Voz",
        description = "Te lee en voz alta cada herramienta cuando la presionas.",
        voiceText = "El Asistente de Voz te habla para decirte qué hace cada botón.",
        badgeColor = Color(0xFFFF70A6)
    )
)

@Composable
fun WelcomeScreen(
    onStartDrawing: () -> Unit,
    onSpeak: (String) -> Unit,
    onBackToMenu: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onSpeak("¡Bienvenidos a KidsDraw! Toca cualquier herramienta para escuchar qué hace, o presiona el botón grande para empezar a dibujar.")
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
                            Color(0xFFFFF3D1),
                            Color(0xFFFFE8A3)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Header Card
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onBackToMenu,
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color(0xFFFFEEAA), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver al Menú",
                                tint = Color(0xFF4A2810)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Guía de Herramientas 🎨",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF4A2810)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Toca cualquier tarjeta para escuchar su función.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7A5C43)
                            )
                        }

                        IconButton(
                            onClick = {
                                onSpeak("¡Bienvenido a KidsDraw! Aquí tienes todas tus herramientas de arte. Toca cualquiera para escucharla.")
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFF70A6), CircleShape)
                                .testTag("welcome_speak_all_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Escuchar bienvenida",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tools Grid
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(KIDS_APP_FEATURES) { item ->
                        FeatureCard(
                            item = item,
                            onClick = { onSpeak(item.voiceText) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Start Drawing Big Kid-Friendly Button
                Button(
                    onClick = {
                        onSpeak("¡A crear arte!")
                        onStartDrawing()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(12.dp, RoundedCornerShape(32.dp))
                        .testTag("start_drawing_button"),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF595E),
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🎨 ¡EMPEZAR A DIBUJAR!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Comenzar",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun FeatureCard(
    item: FeatureItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("feature_card_${item.title}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(item.badgeColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.emoji,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B2D42),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Escuchar",
                    tint = item.badgeColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.description,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF555555)
            )
        }
    }
}
