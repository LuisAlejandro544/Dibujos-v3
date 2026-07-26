package com.example.ui.canvas

import android.content.Context
import android.graphics.Bitmap
import com.example.data.db.AppDatabase
import com.example.data.db.DrawingEntity
import com.example.data.models.DrawingLayer
import com.example.utils.DrawingUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrawingPersistenceManager {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val layerListType = Types.newParameterizedType(List::class.java, DrawingLayer::class.java)
    private val jsonAdapter = moshi.adapter<List<DrawingLayer>>(layerListType)

    suspend fun saveDrawing(
        context: Context,
        title: String,
        currentDrawingId: Long?,
        layers: List<DrawingLayer>,
        bitmapThumbnail: Bitmap
    ): Long = withContext(Dispatchers.IO) {
        val time = System.currentTimeMillis()
        val fileName = "thumb_${time}.png"
        val thumbPath = DrawingUtils.saveBitmapToStorage(context, bitmapThumbnail, fileName)
        val layersJson = jsonAdapter.toJson(layers)

        val dao = AppDatabase.getDatabase(context).drawingDao()

        if (currentDrawingId != null) {
            dao.updateDrawing(
                DrawingEntity(
                    id = currentDrawingId,
                    title = title,
                    createdAt = time,
                    updatedAt = time,
                    thumbnailPath = thumbPath,
                    layersJson = layersJson
                )
            )
            currentDrawingId
        } else {
            dao.insertDrawing(
                DrawingEntity(
                    title = title,
                    createdAt = time,
                    updatedAt = time,
                    thumbnailPath = thumbPath,
                    layersJson = layersJson
                )
            )
        }
    }

    suspend fun loadDrawingLayers(entity: DrawingEntity): List<DrawingLayer> = withContext(Dispatchers.IO) {
        try {
            jsonAdapter.fromJson(entity.layersJson) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
