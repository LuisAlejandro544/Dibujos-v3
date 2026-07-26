package com.example.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.data.models.DrawingLayer
import com.example.data.models.DrawingPath
import com.example.data.models.PointData
import com.example.data.models.StampShape
import com.example.data.models.ToolType
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object DrawingUtils {

    fun getRainbowColor(index: Int, total: Int): Color {
        val hue = (index.toFloat() / maxOf(total, 1).toFloat() * 360f) % 360f
        return Color.hsv(hue, 0.85f, 0.95f)
    }

    fun getComplementaryColor(color: Color): Color {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(color.toArgb(), hsv)
        hsv[0] = (hsv[0] + 180f) % 360f
        return Color(android.graphics.Color.HSVToColor(hsv))
    }

    fun calculatePathBounds(points: List<PointData>): Rect? {
        if (points.isEmpty()) return null
        var minX = points[0].x
        var maxX = points[0].x
        var minY = points[0].y
        var maxY = points[0].y

        for (p in points) {
            if (p.x < minX) minX = p.x
            if (p.x > maxX) maxX = p.x
            if (p.y < minY) minY = p.y
            if (p.y > maxY) maxY = p.y
        }
        return Rect(minX, minY, maxX, maxY)
    }

    fun isPointInsideRect(point: Offset, rect: Rect, tolerance: Float = 20f): Boolean {
        return point.x >= rect.left - tolerance &&
                point.x <= rect.right + tolerance &&
                point.y >= rect.top - tolerance &&
                point.y <= rect.bottom + tolerance
    }

    fun renderLayersToBitmap(layers: List<DrawingLayer>, width: Int = 500, height: Int = 500): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            strokeCap = android.graphics.Paint.Cap.ROUND
            strokeJoin = android.graphics.Paint.Join.ROUND
        }

        layers.forEach { layer ->
            if (layer.isVisible) {
                paint.alpha = (layer.opacity * 255).toInt()
                layer.paths.forEach { pathModel ->
                    if (pathModel.points.isNotEmpty()) {
                        paint.color = pathModel.colorArgb
                        paint.strokeWidth = pathModel.strokeWidth * (width / 1000f)

                        val path = android.graphics.Path()
                        path.moveTo(pathModel.points[0].x * (width / 1000f), pathModel.points[0].y * (height / 1000f))
                        for (i in 1 until pathModel.points.size) {
                            val p = pathModel.points[i]
                            path.lineTo(p.x * (width / 1000f), p.y * (height / 1000f))
                        }
                        canvas.drawPath(path, paint)
                    }
                }
            }
        }
        return bitmap
    }

    fun saveBitmapToStorage(context: Context, bitmap: Bitmap, fileName: String): String {
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
        }
        return file.absolutePath
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }
}
