package com.example.ui.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.data.models.DrawingPath
import com.example.data.models.StampShape
import com.example.utils.DrawingUtils
import kotlin.math.cos
import kotlin.math.sin

object CanvasStampRenderer {

    fun drawStampPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val shape = pathModel.stampShape ?: StampShape.STAR
        val points = pathModel.points
        val size = pathModel.strokeWidth * 1.5f
        val color = pathModel.getColor()

        var lastOffset: Offset? = null
        val minDistance = size * 0.8f

        points.forEachIndexed { idx, point ->
            val offset = point.toOffset()
            if (lastOffset == null || (offset - lastOffset!!).getDistance() >= minDistance) {
                val stampColor = if (pathModel.isRainbow) DrawingUtils.getRainbowColor(idx, points.size) else color
                drawSingleStampShape(drawScope, shape, offset, size, stampColor)
                lastOffset = offset
            }
        }
    }

    private fun drawSingleStampShape(
        drawScope: DrawScope,
        shape: StampShape,
        center: Offset,
        size: Float,
        color: Color
    ) {
        when (shape) {
            StampShape.STAR -> drawStar(drawScope, center, size, color)
            StampShape.HEART -> drawHeart(drawScope, center, size, color)
            StampShape.SPARKLE -> drawSparkle(drawScope, center, size, color)
            StampShape.FLOWER -> drawFlower(drawScope, center, size, color)
            StampShape.PAW -> drawPaw(drawScope, center, size, color)
            StampShape.SUN -> drawSun(drawScope, center, size, color)
            StampShape.RAINBOW -> drawRainbowArc(drawScope, center, size)
            StampShape.GOOGLY_EYES -> drawGooglyEyes(drawScope, center, size)
            StampShape.GOOGLY_EYE_SINGLE -> drawGooglyEyeSingle(drawScope, center, size)
            StampShape.STICKER_CAT -> drawCatSticker(drawScope, center, size, color)
            StampShape.STICKER_CROWN -> drawCrownSticker(drawScope, center, size)
            StampShape.STICKER_MAGIC_WAND -> drawMagicWandSticker(drawScope, center, size, color)
            StampShape.STICKER_BOW -> drawBowSticker(drawScope, center, size, color)
            StampShape.STICKER_SUNGLASSES -> drawSunglassesSticker(drawScope, center, size)
        }
    }

    private fun drawGooglyEyes(drawScope: DrawScope, center: Offset, size: Float) {
        val eyeRadius = size * 0.35f
        val pupilRadius = eyeRadius * 0.45f
        val leftEyeCenter = Offset(center.x - eyeRadius * 0.9f, center.y)
        val rightEyeCenter = Offset(center.x + eyeRadius * 0.9f, center.y)

        // Draw Eye 1 (Left)
        drawScope.drawCircle(color = Color.White, radius = eyeRadius, center = leftEyeCenter)
        drawScope.drawCircle(color = Color(0xFF333333), radius = eyeRadius, center = leftEyeCenter, style = Stroke(width = 3f))
        val leftPupilCenter = Offset(leftEyeCenter.x + pupilRadius * 0.3f, leftEyeCenter.y - pupilRadius * 0.2f)
        drawScope.drawCircle(color = Color.Black, radius = pupilRadius, center = leftPupilCenter)
        drawScope.drawCircle(color = Color.White, radius = pupilRadius * 0.35f, center = Offset(leftPupilCenter.x - pupilRadius * 0.3f, leftPupilCenter.y - pupilRadius * 0.3f))

        // Draw Eye 2 (Right)
        drawScope.drawCircle(color = Color.White, radius = eyeRadius, center = rightEyeCenter)
        drawScope.drawCircle(color = Color(0xFF333333), radius = eyeRadius, center = rightEyeCenter, style = Stroke(width = 3f))
        val rightPupilCenter = Offset(rightEyeCenter.x + pupilRadius * 0.2f, rightEyeCenter.y - pupilRadius * 0.3f)
        drawScope.drawCircle(color = Color.Black, radius = pupilRadius, center = rightPupilCenter)
        drawScope.drawCircle(color = Color.White, radius = pupilRadius * 0.35f, center = Offset(rightPupilCenter.x - pupilRadius * 0.3f, rightPupilCenter.y - pupilRadius * 0.3f))
    }

    private fun drawGooglyEyeSingle(drawScope: DrawScope, center: Offset, size: Float) {
        val eyeRadius = size * 0.5f
        val pupilRadius = eyeRadius * 0.45f

        drawScope.drawCircle(color = Color.White, radius = eyeRadius, center = center)
        drawScope.drawCircle(color = Color(0xFF333333), radius = eyeRadius, center = center, style = Stroke(width = 4f))
        val pupilCenter = Offset(center.x + pupilRadius * 0.2f, center.y - pupilRadius * 0.2f)
        drawScope.drawCircle(color = Color.Black, radius = pupilRadius, center = pupilCenter)
        drawScope.drawCircle(color = Color.White, radius = pupilRadius * 0.35f, center = Offset(pupilCenter.x - pupilRadius * 0.3f, pupilCenter.y - pupilRadius * 0.3f))
    }

    private fun drawCatSticker(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val r = size * 0.4f
        val faceColor = if (color == Color.White) Color(0xFFFFB700) else color

        // Ears
        val leftEar = Path().apply {
            moveTo(center.x - r * 0.8f, center.y - r * 0.3f)
            lineTo(center.x - r * 1.1f, center.y - r * 1.1f)
            lineTo(center.x - r * 0.2f, center.y - r * 0.8f)
            close()
        }
        val rightEar = Path().apply {
            moveTo(center.x + r * 0.8f, center.y - r * 0.3f)
            lineTo(center.x + r * 1.1f, center.y - r * 1.1f)
            lineTo(center.x + r * 0.2f, center.y - r * 0.8f)
            close()
        }
        drawScope.drawPath(leftEar, color = faceColor)
        drawScope.drawPath(rightEar, color = faceColor)

        // Head
        drawScope.drawCircle(color = faceColor, radius = r, center = center)

        // Eyes
        drawScope.drawCircle(color = Color.Black, radius = r * 0.15f, center = Offset(center.x - r * 0.4f, center.y - r * 0.1f))
        drawScope.drawCircle(color = Color.Black, radius = r * 0.15f, center = Offset(center.x + r * 0.4f, center.y - r * 0.1f))

        // Nose & Whiskers
        drawScope.drawCircle(color = Color(0xFFFF3366), radius = r * 0.1f, center = Offset(center.x, center.y + r * 0.15f))
        drawScope.drawLine(color = Color.Black, start = Offset(center.x - r * 0.3f, center.y + r * 0.15f), end = Offset(center.x - r * 0.9f, center.y + r * 0.05f), strokeWidth = 3f)
        drawScope.drawLine(color = Color.Black, start = Offset(center.x - r * 0.3f, center.y + r * 0.25f), end = Offset(center.x - r * 0.9f, center.y + r * 0.35f), strokeWidth = 3f)
        drawScope.drawLine(color = Color.Black, start = Offset(center.x + r * 0.3f, center.y + r * 0.15f), end = Offset(center.x + r * 0.9f, center.y + r * 0.05f), strokeWidth = 3f)
        drawScope.drawLine(color = Color.Black, start = Offset(center.x + r * 0.3f, center.y + r * 0.25f), end = Offset(center.x + r * 0.9f, center.y + r * 0.35f), strokeWidth = 3f)
    }

    private fun drawCrownSticker(drawScope: DrawScope, center: Offset, size: Float) {
        val w = size * 0.9f
        val h = size * 0.7f
        val gold = Color(0xFFFFCA3A)
        val ruby = Color(0xFFFF0055)

        val crownPath = Path().apply {
            moveTo(center.x - w / 2f, center.y + h / 2f)
            lineTo(center.x - w / 2f, center.y - h / 3f)
            lineTo(center.x - w / 4f, center.y)
            lineTo(center.x, center.y - h / 2f)
            lineTo(center.x + w / 4f, center.y)
            lineTo(center.x + w / 2f, center.y - h / 3f)
            lineTo(center.x + w / 2f, center.y + h / 2f)
            close()
        }
        drawScope.drawPath(crownPath, color = gold)

        // Jewels
        drawScope.drawCircle(color = ruby, radius = size * 0.08f, center = Offset(center.x - w / 2f, center.y - h / 3f))
        drawScope.drawCircle(color = ruby, radius = size * 0.1f, center = Offset(center.x, center.y - h / 2f))
        drawScope.drawCircle(color = ruby, radius = size * 0.08f, center = Offset(center.x + w / 2f, center.y - h / 3f))
    }

    private fun drawMagicWandSticker(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val r = size * 0.5f
        // Stick
        drawScope.drawLine(
            color = Color(0xFF333333),
            start = Offset(center.x - r * 0.7f, center.y + r * 0.7f),
            end = Offset(center.x + r * 0.2f, center.y - r * 0.2f),
            strokeWidth = 8f
        )
        // Tip star
        drawStar(drawScope, Offset(center.x + r * 0.35f, center.y - r * 0.35f), size * 0.5f, Color(0xFFFFCA3A))
    }

    private fun drawBowSticker(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val bowColor = if (color == Color.White) Color(0xFFFF70A6) else color
        val r = size * 0.4f

        val leftLoop = Path().apply {
            moveTo(center.x, center.y)
            cubicTo(
                center.x - r * 1.2f, center.y - r * 0.8f,
                center.x - r * 1.2f, center.y + r * 0.8f,
                center.x, center.y
            )
            close()
        }
        val rightLoop = Path().apply {
            moveTo(center.x, center.y)
            cubicTo(
                center.x + r * 1.2f, center.y - r * 0.8f,
                center.x + r * 1.2f, center.y + r * 0.8f,
                center.x, center.y
            )
            close()
        }
        drawScope.drawPath(leftLoop, color = bowColor)
        drawScope.drawPath(rightLoop, color = bowColor)
        // Center knot
        drawScope.drawCircle(color = Color(0xFFFF3366), radius = r * 0.3f, center = center)
    }

    private fun drawSunglassesSticker(drawScope: DrawScope, center: Offset, size: Float) {
        val w = size * 0.9f
        val h = size * 0.4f

        // Frames
        drawScope.drawRoundRect(
            color = Color(0xFF1E1E1E),
            topLeft = Offset(center.x - w / 2f, center.y - h / 2f),
            size = androidx.compose.ui.geometry.Size(w / 2.2f, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
        )
        drawScope.drawRoundRect(
            color = Color(0xFF1E1E1E),
            topLeft = Offset(center.x + w / 20f, center.y - h / 2f),
            size = androidx.compose.ui.geometry.Size(w / 2.2f, h),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
        )
        // Bridge
        drawScope.drawLine(
            color = Color(0xFF1E1E1E),
            start = Offset(center.x - w / 20f, center.y - h / 4f),
            end = Offset(center.x + w / 20f, center.y - h / 4f),
            strokeWidth = 6f
        )
        // Glare sheen
        drawScope.drawLine(
            color = Color.White.copy(alpha = 0.6f),
            start = Offset(center.x - w / 2.5f, center.y - h / 3f),
            end = Offset(center.x - w / 4f, center.y + h / 3f),
            strokeWidth = 4f
        )
        drawScope.drawLine(
            color = Color.White.copy(alpha = 0.6f),
            start = Offset(center.x + w / 7f, center.y - h / 3f),
            end = Offset(center.x + w / 3f, center.y + h / 3f),
            strokeWidth = 4f
        )
    }

    private fun drawStar(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val path = Path()
        val outerR = size / 2f
        val innerR = outerR * 0.4f
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) outerR else innerR
            val angle = Math.toRadians((i * 36 - 90).toDouble())
            val x = center.x + (r * cos(angle)).toFloat()
            val y = center.y + (r * sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawScope.drawPath(path, color = color)
    }

    private fun drawHeart(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val path = Path()
        val r = size / 2f
        path.moveTo(center.x, center.y + r * 0.6f)
        path.cubicTo(
            center.x - r * 1.2f, center.y,
            center.x - r * 0.8f, center.y - r,
            center.x, center.y - r * 0.4f
        )
        path.cubicTo(
            center.x + r * 0.8f, center.y - r,
            center.x + r * 1.2f, center.y,
            center.x, center.y + r * 0.6f
        )
        path.close()
        drawScope.drawPath(path, color = color)
    }

    private fun drawSparkle(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val path = Path()
        val r = size / 2f
        path.moveTo(center.x, center.y - r)
        path.quadraticTo(center.x, center.y, center.x + r, center.y)
        path.quadraticTo(center.x, center.y, center.x, center.y + r)
        path.quadraticTo(center.x, center.y, center.x - r, center.y)
        path.quadraticTo(center.x, center.y, center.x, center.y - r)
        path.close()
        drawScope.drawPath(path, color = color)
    }

    private fun drawFlower(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val petalR = size / 4f
        for (i in 0 until 5) {
            val angle = Math.toRadians((i * 72).toDouble())
            val px = center.x + (petalR * 1.2f * cos(angle)).toFloat()
            val py = center.y + (petalR * 1.2f * sin(angle)).toFloat()
            drawScope.drawCircle(color = color, radius = petalR, center = Offset(px, py))
        }
        drawScope.drawCircle(color = Color(0xFFFFD700), radius = petalR * 0.9f, center = center)
    }

    private fun drawPaw(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val padR = size / 3f
        drawScope.drawCircle(color = color, radius = padR, center = Offset(center.x, center.y + padR * 0.3f))
        drawScope.drawCircle(color = color, radius = padR * 0.4f, center = Offset(center.x - padR * 0.8f, center.y - padR * 0.6f))
        drawScope.drawCircle(color = color, radius = padR * 0.45f, center = Offset(center.x - padR * 0.3f, center.y - padR * 0.9f))
        drawScope.drawCircle(color = color, radius = padR * 0.45f, center = Offset(center.x + padR * 0.3f, center.y - padR * 0.9f))
        drawScope.drawCircle(color = color, radius = padR * 0.4f, center = Offset(center.x + padR * 0.8f, center.y - padR * 0.6f))
    }

    private fun drawSun(drawScope: DrawScope, center: Offset, size: Float, color: Color) {
        val r = size / 3f
        val sunColor = Color(0xFFFFB700)
        drawScope.drawCircle(color = sunColor, radius = r, center = center)
        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45).toDouble())
            val x1 = center.x + ((r + 4f) * cos(angle)).toFloat()
            val y1 = center.y + ((r + 4f) * sin(angle)).toFloat()
            val x2 = center.x + ((r + size / 4f) * cos(angle)).toFloat()
            val y2 = center.y + ((r + size / 4f) * sin(angle)).toFloat()
            drawScope.drawLine(color = sunColor, start = Offset(x1, y1), end = Offset(x2, y2), strokeWidth = 6f)
        }
    }

    private fun drawRainbowArc(drawScope: DrawScope, center: Offset, size: Float) {
        val colors = listOf(Color.Red, Color(0xFFFFA500), Color.Yellow, Color.Green, Color.Cyan, Color.Magenta)
        var r = size / 2f
        val strokeW = size / 10f
        colors.forEach { c ->
            drawScope.drawCircle(color = c, radius = r, center = center, style = Stroke(width = strokeW))
            r -= strokeW
        }
    }
}
