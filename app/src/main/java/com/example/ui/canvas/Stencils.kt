package com.example.ui.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.models.StencilType

object Stencils {

    fun drawStencil(drawScope: DrawScope, type: StencilType, size: Size) {
        if (type == StencilType.NONE) return

        val width = size.width
        val height = size.height
        val cx = width / 2f
        val cy = height / 2f
        val minDim = minOf(width, height)
        val scale = minDim / 400f

        val path = Path()
        val stroke = Stroke(
            width = 6f * scale,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val darkColor = Color(0xFF2C3E50)

        when (type) {
            StencilType.DINOSAUR -> drawDinosaur(drawScope, path, cx, cy, scale, darkColor, stroke)
            StencilType.ROCKET -> drawRocket(drawScope, path, cx, cy, scale, darkColor, stroke)
            StencilType.UNICORN -> drawUnicorn(drawScope, path, cx, cy, scale, darkColor, stroke)
            StencilType.PUPPY -> drawPuppy(drawScope, path, cx, cy, scale, darkColor, stroke)
            StencilType.FISH -> drawFish(drawScope, path, cx, cy, scale, darkColor, stroke)
            StencilType.CAKE -> drawCake(drawScope, path, cx, cy, scale, darkColor, stroke)
            StencilType.NONE -> {}
        }
    }

    private fun drawDinosaur(
        drawScope: DrawScope,
        path: Path,
        cx: Float,
        cy: Float,
        scale: Float,
        color: Color,
        stroke: Stroke
    ) {
        // Body & Head outline for a cute Dino
        path.reset()
        // Head & snout
        path.moveTo(cx - 50 * scale, cy - 120 * scale)
        path.cubicTo(
            cx + 80 * scale, cy - 140 * scale,
            cx + 100 * scale, cy - 60 * scale,
            cx + 40 * scale, cy - 40 * scale
        )
        // Neck & Back
        path.cubicTo(
            cx + 80 * scale, cy + 20 * scale,
            cx + 90 * scale, cy + 100 * scale,
            cx + 140 * scale, cy + 110 * scale // Tail tip
        )
        // Tail bottom & legs
        path.cubicTo(
            cx + 80 * scale, cy + 130 * scale,
            cx + 40 * scale, cy + 120 * scale,
            cx + 30 * scale, cy + 140 * scale // Back leg
        )
        path.lineTo(cx + 10 * scale, cy + 140 * scale)
        path.lineTo(cx + 15 * scale, cy + 90 * scale)
        path.lineTo(cx - 30 * scale, cy + 140 * scale) // Front leg
        path.lineTo(cx - 50 * scale, cy + 140 * scale)
        // Belly & throat
        path.cubicTo(
            cx - 35 * scale, cy + 80 * scale,
            cx - 40 * scale, cy + 20 * scale,
            cx - 80 * scale, cy - 60 * scale
        )
        path.close()

        drawScope.drawPath(path, color = color, style = stroke)

        // Spikes on back
        val spikesPath = Path()
        spikesPath.moveTo(cx - 20 * scale, cy - 100 * scale)
        spikesPath.lineTo(cx - 10 * scale, cy - 125 * scale)
        spikesPath.lineTo(cx, cy - 90 * scale)

        spikesPath.lineTo(cx + 20 * scale, cy - 105 * scale)
        spikesPath.lineTo(cx + 35 * scale, cy - 70 * scale)

        spikesPath.lineTo(cx + 55 * scale, cy - 35 * scale)
        spikesPath.lineTo(cx + 65 * scale, cy + 10 * scale)

        drawScope.drawPath(spikesPath, color = color, style = stroke)

        // Dino Eye & Smile
        drawScope.drawCircle(color = color, radius = 8f * scale, center = Offset(cx, cy - 90 * scale))
        drawScope.drawCircle(color = Color.White, radius = 3f * scale, center = Offset(cx - 2 * scale, cy - 92 * scale))

        val smile = Path().apply {
            moveTo(cx + 15 * scale, cy - 70 * scale)
            quadraticTo(cx + 30 * scale, cy - 60 * scale, cx + 40 * scale, cy - 75 * scale)
        }
        drawScope.drawPath(smile, color = color, style = stroke)
    }

    private fun drawRocket(
        drawScope: DrawScope,
        path: Path,
        cx: Float,
        cy: Float,
        scale: Float,
        color: Color,
        stroke: Stroke
    ) {
        path.reset()
        // Main Body
        path.moveTo(cx, cy - 140 * scale)
        path.cubicTo(
            cx + 70 * scale, cy - 60 * scale,
            cx + 60 * scale, cy + 80 * scale,
            cx + 50 * scale, cy + 100 * scale
        )
        path.lineTo(cx - 50 * scale, cy + 100 * scale)
        path.cubicTo(
            cx - 60 * scale, cy + 80 * scale,
            cx - 70 * scale, cy - 60 * scale,
            cx, cy - 140 * scale
        )
        drawScope.drawPath(path, color = color, style = stroke)

        // Wings
        val wingLeft = Path().apply {
            moveTo(cx - 55 * scale, cy + 40 * scale)
            lineTo(cx - 100 * scale, cy + 110 * scale)
            lineTo(cx - 50 * scale, cy + 100 * scale)
        }
        val wingRight = Path().apply {
            moveTo(cx + 55 * scale, cy + 40 * scale)
            lineTo(cx + 100 * scale, cy + 110 * scale)
            lineTo(cx + 50 * scale, cy + 100 * scale)
        }
        drawScope.drawPath(wingLeft, color = color, style = stroke)
        drawScope.drawPath(wingRight, color = color, style = stroke)

        // Porthole Window
        drawScope.drawCircle(color = color, radius = 30f * scale, center = Offset(cx, cy - 20 * scale), style = stroke)
        drawScope.drawCircle(color = color, radius = 22f * scale, center = Offset(cx, cy - 20 * scale), style = stroke)

        // Flame thrust
        val flame = Path().apply {
            moveTo(cx - 30 * scale, cy + 102 * scale)
            lineTo(cx - 20 * scale, cy + 150 * scale)
            lineTo(cx, cy + 125 * scale)
            lineTo(cx + 20 * scale, cy + 150 * scale)
            lineTo(cx + 30 * scale, cy + 102 * scale)
        }
        drawScope.drawPath(flame, color = color, style = stroke)
    }

    private fun drawUnicorn(
        drawScope: DrawScope,
        path: Path,
        cx: Float,
        cy: Float,
        scale: Float,
        color: Color,
        stroke: Stroke
    ) {
        path.reset()
        // Head & Snout
        path.moveTo(cx - 40 * scale, cy - 40 * scale)
        path.cubicTo(
            cx - 70 * scale, cy - 20 * scale,
            cx - 90 * scale, cy + 30 * scale,
            cx - 50 * scale, cy + 50 * scale
        )
        path.cubicTo(
            cx - 20 * scale, cy + 60 * scale,
            cx + 20 * scale, cy + 20 * scale,
            cx + 30 * scale, cy - 20 * scale
        )
        // Neck & Mane
        path.lineTo(cx + 50 * scale, cy + 80 * scale)
        path.lineTo(cx + 10 * scale, cy + 90 * scale)
        path.lineTo(cx - 20 * scale, cy - 20 * scale)
        drawScope.drawPath(path, color = color, style = stroke)

        // Magic Horn
        val horn = Path().apply {
            moveTo(cx - 30 * scale, cy - 45 * scale)
            lineTo(cx - 50 * scale, cy - 130 * scale)
            lineTo(cx - 10 * scale, cy - 50 * scale)
            close()
        }
        drawScope.drawPath(horn, color = color, style = stroke)

        // Cute Eye with lashes
        drawScope.drawCircle(color = color, radius = 6f * scale, center = Offset(cx - 30 * scale, cy - 5 * scale))

        // Stars around horn
        val star1 = Offset(cx - 70 * scale, cy - 100 * scale)
        val star2 = Offset(cx + 20 * scale, cy - 110 * scale)
        drawScope.drawCircle(color = color, radius = 4f * scale, center = star1)
        drawScope.drawCircle(color = color, radius = 4f * scale, center = star2)
    }

    private fun drawPuppy(
        drawScope: DrawScope,
        path: Path,
        cx: Float,
        cy: Float,
        scale: Float,
        color: Color,
        stroke: Stroke
    ) {
        // Head circle
        drawScope.drawCircle(color = color, radius = 80f * scale, center = Offset(cx, cy - 20 * scale), style = stroke)

        // Ears
        val leftEar = Path().apply {
            moveTo(cx - 60 * scale, cy - 70 * scale)
            cubicTo(
                cx - 120 * scale, cy - 60 * scale,
                cx - 130 * scale, cy + 30 * scale,
                cx - 70 * scale, cy + 10 * scale
            )
        }
        val rightEar = Path().apply {
            moveTo(cx + 60 * scale, cy - 70 * scale)
            cubicTo(
                cx + 120 * scale, cy - 60 * scale,
                cx + 130 * scale, cy + 30 * scale,
                cx + 70 * scale, cy + 10 * scale
            )
        }
        drawScope.drawPath(leftEar, color = color, style = stroke)
        drawScope.drawPath(rightEar, color = color, style = stroke)

        // Eyes
        drawScope.drawCircle(color = color, radius = 10f * scale, center = Offset(cx - 30 * scale, cy - 30 * scale))
        drawScope.drawCircle(color = color, radius = 10f * scale, center = Offset(cx + 30 * scale, cy - 30 * scale))

        // Nose
        drawScope.drawCircle(color = color, radius = 14f * scale, center = Offset(cx, cy))

        // Tongue / Mouth
        val mouth = Path().apply {
            moveTo(cx - 20 * scale, cy + 15 * scale)
            quadraticTo(cx, cy + 30 * scale, cx + 20 * scale, cy + 15 * scale)
        }
        drawScope.drawPath(mouth, color = color, style = stroke)

        // Body outline
        val body = Path().apply {
            moveTo(cx - 50 * scale, cy + 50 * scale)
            lineTo(cx - 60 * scale, cy + 130 * scale)
            lineTo(cx + 60 * scale, cy + 130 * scale)
            lineTo(cx + 50 * scale, cy + 50 * scale)
        }
        drawScope.drawPath(body, color = color, style = stroke)
    }

    private fun drawFish(
        drawScope: DrawScope,
        path: Path,
        cx: Float,
        cy: Float,
        scale: Float,
        color: Color,
        stroke: Stroke
    ) {
        path.reset()
        // Oval Body
        path.moveTo(cx - 120 * scale, cy)
        path.cubicTo(
            cx - 60 * scale, cy - 100 * scale,
            cx + 60 * scale, cy - 100 * scale,
            cx + 100 * scale, cy
        )
        // Tail
        path.lineTo(cx + 150 * scale, cy - 70 * scale)
        path.lineTo(cx + 130 * scale, cy)
        path.lineTo(cx + 150 * scale, cy + 70 * scale)
        path.lineTo(cx + 100 * scale, cy)
        // Lower body
        path.cubicTo(
            cx + 60 * scale, cy + 100 * scale,
            cx - 60 * scale, cy + 100 * scale,
            cx - 120 * scale, cy
        )
        drawScope.drawPath(path, color = color, style = stroke)

        // Eye
        drawScope.drawCircle(color = color, radius = 12f * scale, center = Offset(cx - 60 * scale, cy - 25 * scale))
        drawScope.drawCircle(color = Color.White, radius = 4f * scale, center = Offset(cx - 64 * scale, cy - 28 * scale))

        // Bubbles
        drawScope.drawCircle(color = color, radius = 8f * scale, center = Offset(cx - 140 * scale, cy - 50 * scale), style = stroke)
        drawScope.drawCircle(color = color, radius = 12f * scale, center = Offset(cx - 160 * scale, cy - 100 * scale), style = stroke)
    }

    private fun drawCake(
        drawScope: DrawScope,
        path: Path,
        cx: Float,
        cy: Float,
        scale: Float,
        color: Color,
        stroke: Stroke
    ) {
        // Bottom tier
        drawScope.drawRoundRect(
            color = color,
            topLeft = Offset(cx - 110 * scale, cy + 30 * scale),
            size = Size(220 * scale, 90 * scale),
            style = stroke
        )
        // Top tier
        drawScope.drawRoundRect(
            color = color,
            topLeft = Offset(cx - 80 * scale, cy - 40 * scale),
            size = Size(160 * scale, 70 * scale),
            style = stroke
        )

        // Candles
        val candlePositions = listOf(cx - 40 * scale, cx, cx + 40 * scale)
        for (xPos in candlePositions) {
            drawScope.drawRect(
                color = color,
                topLeft = Offset(xPos - 6 * scale, cy - 80 * scale),
                size = Size(12 * scale, 40 * scale),
                style = stroke
            )
            // Flame
            val flame = Path().apply {
                moveTo(xPos, cy - 105 * scale)
                quadraticTo(xPos + 10 * scale, cy - 90 * scale, xPos, cy - 80 * scale)
                quadraticTo(xPos - 10 * scale, cy - 90 * scale, xPos, cy - 105 * scale)
            }
            drawScope.drawPath(flame, color = color, style = stroke)
        }
    }
}
