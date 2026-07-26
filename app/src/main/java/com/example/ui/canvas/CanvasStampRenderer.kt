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
        }
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
