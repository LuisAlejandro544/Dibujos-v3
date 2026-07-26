package com.example.ui.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import com.example.data.models.DrawingLayer
import com.example.data.models.DrawingPath
import com.example.data.models.PointData
import com.example.data.models.SelectionState
import com.example.data.models.ToolType
import com.example.utils.DrawingUtils

import androidx.compose.ui.graphics.toArgb

object CanvasPathRenderer {

    fun drawLayerContent(
        drawScope: DrawScope,
        layer: DrawingLayer,
        selectionState: SelectionState,
        activeLayerId: String
    ) {
        layer.paths.forEach { pathModel ->
            val isSelected = selectionState.isActive &&
                    selectionState.activeLayerId == layer.id &&
                    pathModel.id in selectionState.selectedPathIds

            val effectivePath = if (layer.opacity < 1.0f) {
                pathModel.copy(alpha = pathModel.alpha * layer.opacity)
            } else {
                pathModel
            }

            drawSinglePath(
                drawScope = drawScope,
                pathModel = effectivePath,
                isSelected = isSelected,
                selectionState = if (isSelected) selectionState else SelectionState()
            )
        }
    }

    fun drawSinglePath(
        drawScope: DrawScope,
        pathModel: DrawingPath,
        isSelected: Boolean,
        selectionState: SelectionState
    ) {
        if (pathModel.points.isEmpty()) return

        val tx = pathModel.translationX + (if (isSelected) selectionState.translation.x else 0f)
        val ty = pathModel.translationY + (if (isSelected) selectionState.translation.y else 0f)
        val sc = pathModel.scaleX * (if (isSelected) selectionState.scale else 1.0f)
        val rot = pathModel.rotationDegrees + (if (isSelected) selectionState.rotation else 0.0f)

        drawScope.translate(left = tx, top = ty) {
            val bounds = DrawingUtils.calculatePathBounds(pathModel.points)
            val center = bounds?.center ?: Offset.Zero

            drawScope.rotate(degrees = rot, pivot = center) {
                drawScope.scale(scale = sc, pivot = center) {
                    when (pathModel.toolType) {
                        ToolType.ERASER -> drawEraserPath(drawScope, pathModel)
                        ToolType.STAMP -> CanvasStampRenderer.drawStampPath(drawScope, pathModel)
                        ToolType.GLOW -> drawGlowPath(drawScope, pathModel)
                        ToolType.SPARKLE_BRUSH -> drawSparkleBrushPath(drawScope, pathModel)
                        ToolType.BUBBLE_BRUSH -> drawBubbleBrushPath(drawScope, pathModel)
                        ToolType.GALAXY_BRUSH -> drawGalaxyBrushPath(drawScope, pathModel)
                        ToolType.CRAYON -> drawCrayonPath(drawScope, pathModel)
                        ToolType.DUAL_BRUSH -> drawDualBrushPath(drawScope, pathModel)
                        ToolType.BUCKET -> drawBucketPath(drawScope, pathModel)
                        else -> drawStandardPath(drawScope, pathModel)
                    }
                }
            }
        }
    }

    private fun drawStandardPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        if (points.size == 1) {
            val color = if (pathModel.isRainbow) DrawingUtils.getRainbowColor(0, 1) else pathModel.getColor()
            drawScope.drawCircle(
                color = color.copy(alpha = pathModel.alpha),
                radius = pathModel.strokeWidth / 2f,
                center = points[0].toOffset()
            )
            return
        }

        if (pathModel.isRainbow) {
            for (i in 0 until points.size - 1) {
                val rainbowColor = DrawingUtils.getRainbowColor(i, points.size)
                drawScope.drawLine(
                    color = rainbowColor.copy(alpha = pathModel.alpha),
                    start = points[i].toOffset(),
                    end = points[i + 1].toOffset(),
                    strokeWidth = pathModel.strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        } else {
            val path = buildSmoothPath(points)
            val stroke = Stroke(
                width = pathModel.strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
            val color = pathModel.getColor().copy(alpha = pathModel.alpha)
            drawScope.drawPath(path = path, color = color, style = stroke)
        }
    }

    private fun drawGlowPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        val path = buildSmoothPath(points)
        val color = pathModel.getColor()

        drawScope.drawPath(
            path = path,
            color = color.copy(alpha = 0.25f),
            style = Stroke(
                width = pathModel.strokeWidth * 2.2f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        drawScope.drawPath(
            path = path,
            color = color.copy(alpha = 0.5f),
            style = Stroke(
                width = pathModel.strokeWidth * 1.5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
        drawScope.drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.9f),
            style = Stroke(
                width = pathModel.strokeWidth * 0.6f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }

    private fun drawSparkleBrushPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        if (points.isEmpty()) return

        // 1. Base smooth line
        drawStandardPath(drawScope, pathModel)

        // 2. Scatter sparkles along points
        val size = pathModel.strokeWidth * 1.2f
        val interval = (points.size / 12).coerceAtLeast(1)
        points.forEachIndexed { index, pt ->
            if (index % interval == 0) {
                val offset = pt.toOffset()
                val color = if (pathModel.isRainbow) DrawingUtils.getRainbowColor(index, points.size) else Color(0xFFFFCA3A)
                CanvasStampRenderer.drawStampPath(
                    drawScope,
                    DrawingPath(
                        points = listOf(pt),
                        strokeWidth = size,
                        colorArgb = color.toArgb(),
                        stampShape = com.example.data.models.StampShape.SPARKLE,
                        toolType = ToolType.STAMP
                    )
                )
            }
        }
    }

    private fun drawBubbleBrushPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        if (points.isEmpty()) return

        val radius = pathModel.strokeWidth * 1.1f
        val baseColor = pathModel.getColor().copy(alpha = 0.35f)
        val strokeColor = pathModel.getColor().copy(alpha = 0.8f)

        var lastPt: Offset? = null
        val minDist = radius * 0.7f

        points.forEach { pt ->
            val offset = pt.toOffset()
            if (lastPt == null || (offset - lastPt!!).getDistance() >= minDist) {
                // Bubble fill
                drawScope.drawCircle(color = baseColor, radius = radius, center = offset)
                // Bubble rim
                drawScope.drawCircle(color = strokeColor, radius = radius, center = offset, style = Stroke(width = 3f))
                // Shiny highlight arc
                drawScope.drawCircle(
                    color = Color.White.copy(alpha = 0.85f),
                    radius = radius * 0.3f,
                    center = Offset(offset.x - radius * 0.35f, offset.y - radius * 0.35f)
                )
                lastPt = offset
            }
        }
    }

    private fun drawGalaxyBrushPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        if (points.isEmpty()) return

        val path = buildSmoothPath(points)

        // Cosmic Outer Glow
        drawScope.drawPath(
            path = path,
            color = Color(0xFF9D4EDD).copy(alpha = 0.4f),
            style = Stroke(width = pathModel.strokeWidth * 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // Cosmic Inner Path
        drawScope.drawPath(
            path = path,
            color = Color(0xFF240046).copy(alpha = 0.9f),
            style = Stroke(width = pathModel.strokeWidth * 1.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Twinkling Star Dust
        val interval = (points.size / 15).coerceAtLeast(1)
        points.forEachIndexed { idx, pt ->
            if (idx % interval == 0) {
                val offset = pt.toOffset()
                val starColor = if (idx % 2 == 0) Color.White else Color(0xFFFFD166)
                drawScope.drawCircle(color = starColor, radius = pathModel.strokeWidth * 0.25f, center = offset)
            }
        }
    }

    private fun drawCrayonPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        val path = buildSmoothPath(points)
        val color = pathModel.getColor().copy(alpha = pathModel.alpha * 0.85f)

        val dashEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                pathModel.strokeWidth * 0.8f,
                pathModel.strokeWidth * 0.4f
            ),
            phase = 0f
        )

        drawScope.drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = pathModel.strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = dashEffect
            )
        )
    }

    private fun drawDualBrushPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        if (points.isEmpty()) return

        val path1 = buildSmoothPath(points)
        val stroke = Stroke(
            width = pathModel.strokeWidth * 0.85f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val color1 = pathModel.getColor().copy(alpha = pathModel.alpha)
        drawScope.drawPath(path = path1, color = color1, style = stroke)

        if (pathModel.isDualBrushMirror) {
            val canvasWidth = drawScope.size.width
            val mirroredPoints = points.map { pt ->
                PointData(canvasWidth - pt.x, pt.y)
            }
            val path2 = buildSmoothPath(mirroredPoints)
            val secondaryColor = if (pathModel.isRainbow) {
                Color(0xFF00C8FF)
            } else {
                DrawingUtils.getComplementaryColor(pathModel.getColor())
            }
            val color2 = secondaryColor.copy(alpha = pathModel.alpha)
            drawScope.drawPath(path = path2, color = color2, style = stroke)
        } else {
            val offsetDist = pathModel.strokeWidth * 0.75f
            val secondaryPoints = points.map { pt ->
                PointData(pt.x + offsetDist, pt.y + offsetDist)
            }
            val path2 = buildSmoothPath(secondaryPoints)
            val secondaryColor = if (pathModel.isRainbow) {
                Color(0xFF00C8FF)
            } else {
                DrawingUtils.getComplementaryColor(pathModel.getColor())
            }
            val color2 = secondaryColor.copy(alpha = pathModel.alpha)
            drawScope.drawPath(path = path2, color = color2, style = stroke)
        }
    }

    private fun drawEraserPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val points = pathModel.points
        val path = buildSmoothPath(points)

        drawScope.drawPath(
            path = path,
            color = Color.Transparent,
            style = Stroke(
                width = pathModel.strokeWidth * 1.4f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            ),
            blendMode = BlendMode.Clear
        )
    }

    private fun drawBucketPath(drawScope: DrawScope, pathModel: DrawingPath) {
        val color = pathModel.getColor().copy(alpha = pathModel.alpha)
        drawScope.drawRect(color = color, topLeft = Offset.Zero, size = drawScope.size)
    }

    fun buildSmoothPath(points: List<PointData>): Path {
        val path = Path()
        if (points.isEmpty()) return path

        path.moveTo(points[0].x, points[0].y)
        if (points.size == 1) {
            path.lineTo(points[0].x + 0.1f, points[0].y + 0.1f)
            return path
        }

        for (i in 1 until points.size) {
            val p0 = points[i - 1]
            val p1 = points[i]
            val midX = (p0.x + p1.x) / 2f
            val midY = (p0.y + p1.y) / 2f
            path.quadraticTo(p0.x, p0.y, midX, midY)
        }
        path.lineTo(points.last().x, points.last().y)
        return path
    }
}
