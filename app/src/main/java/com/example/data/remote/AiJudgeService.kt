package com.example.data.remote

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

data class AiJudgeResult(
    val p1Feedback: String,
    val p2Feedback: String,
    val p1Stars: Int,
    val p2Stars: Int,
    val verdictTitle: String,
    val verdictAudioSummary: String
)

object AiJudgeService {
    private const val TAG = "AiJudgeService"
    private const val GEMINI_MODEL = "gemini-2.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun Bitmap.toBase64Jpeg(): String {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 75, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }

    /**
     * Determines the API endpoint (Cloudflare Worker proxy or direct Gemini API endpoint)
     */
    private fun getApiEndpoint(): String? {
        val workerUrl = try {
            val field = BuildConfig::class.java.getField("GEMINI_WORKER_URL")
            field.get(null)?.toString() ?: ""
        } catch (e: Exception) { "" }

        if (workerUrl.isNotBlank() && workerUrl.startsWith("http") && !workerUrl.contains("my-gemini-proxy.workers.dev")) {
            return workerUrl.trimEnd('/')
        }

        // Default Cloudflare worker proxy
        val defaultWorker = "https://dibujo.luisalejandrososacamacho9.workers.dev"
        if (defaultWorker.isNotBlank()) {
            return defaultWorker
        }

        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY" && apiKey != "UNUSED_PROXY_MODE") {
            return "$BASE_URL/$GEMINI_MODEL:generateContent?key=$apiKey"
        }

        return null
    }

    /**
     * Generates a creative drawing prompt using Gemini API or fallback
     */
    suspend fun generateCreativePrompt(): String = withContext(Dispatchers.IO) {
        val endpoint = getApiEndpoint()
        if (endpoint == null) {
            return@withContext listOf(
                "¡Dibuja un dinosaurio astronauta comiendo helado en Marte! 🦕🍦🚀",
                "¡Dibuja un gatito superhéroe volando sobre un arcoíris! 🐱⚡🌈",
                "¡Dibuja un pulpo chef preparando la pizza más grande del mundo! 🐙🍕",
                "¡Dibuja un castillo mágico volador hecho de galletas y caramelos! 🏰🍪🍬",
                "¡Dibuja un robot bailarín tocando la guitarra en una fiesta de alienígenas! 🤖🎸👾"
            ).random()
        }

        try {
            val partObj = JSONObject().apply {
                put("text", "Genera una frase corta, súper divertida e imaginativa en español para que dos niños dibujen en un duelo de 30 segundos. Incluye 2 emojis. Responde SOLO con la frase.")
            }
            val contentObj = JSONObject().apply {
                put("parts", JSONArray().put(partObj))
            }
            val requestJson = JSONObject().apply {
                put("contents", JSONArray().put(contentObj))
            }

            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(endpoint).post(body).build()

            val response = okHttpClient.newCall(request).execute()
            val responseText = response.body?.string() ?: ""

            if (response.isSuccessful && responseText.isNotBlank()) {
                val parsed = JSONObject(responseText)
                val candidates = parsed.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text", "").trim()
                        if (text.isNotBlank()) {
                            return@withContext text
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating AI prompt", e)
        }

        return@withContext "¡Dibuja un superhéroe o criatura fantástica! 🦸✨"
    }

    /**
     * Multimodal AI Judge using Gemini Vision & Reasoning
     */
    suspend fun evaluateDrawingsWithAi(
        p1Bitmap: Bitmap?,
        p2Bitmap: Bitmap?,
        prompt: String
    ): AiJudgeResult = withContext(Dispatchers.IO) {
        val endpoint = getApiEndpoint()
        if (endpoint == null || (p1Bitmap == null && p2Bitmap == null)) {
            return@withContext AiJudgeResult(
                p1Feedback = "¡Excelente uso de colores y trazos creativos!",
                p2Feedback = "¡Gran imaginación y composición dinámica!",
                p1Stars = 5,
                p2Stars = 5,
                verdictTitle = "¡EMPATE MÁGICO DE ARTISTAS! 🏆🎨",
                verdictAudioSummary = "¡Felicidades a ambos niños! Sus dos dibujos son increíbles obras de arte llenas de alegría."
            )
        }

        try {
            val partsArray = JSONArray()
            partsArray.put(JSONObject().apply {
                put("text", "Aquí están los dos dibujos para el tema del duelo: '$prompt'.\nImagen 1 es del Niño 1. Imagen 2 es del Niño 2. Por favor analiza ambos dibujos como el Juez Mágico AI.")
            })

            p1Bitmap?.let {
                partsArray.put(JSONObject().apply {
                    put("inlineData", JSONObject().apply {
                        put("mimeType", "image/jpeg")
                        put("data", it.toBase64Jpeg())
                    })
                })
            }

            p2Bitmap?.let {
                partsArray.put(JSONObject().apply {
                    put("inlineData", JSONObject().apply {
                        put("mimeType", "image/jpeg")
                        put("data", it.toBase64Jpeg())
                    })
                })
            }

            val systemInstructionJson = JSONObject().apply {
                put("parts", JSONArray().put(JSONObject().apply {
                    put("text", """
                        Eres el 'Juez Mágico AI' 🎨🌟, un juez de arte súper entusiasta, amoroso, divertido y constructivo para un duelo de dibujo infantil.
                        Analiza visualmente la Imagen 1 (Niño 1) y la Imagen 2 (Niño 2).
                        Debes responder EXCLUSIVAMENTE en formato JSON válido con este esquema exacto:
                        {
                          "p1Feedback": "Comentario alegre y detallado sobre el dibujo del Niño 1",
                          "p2Feedback": "Comentario alegre y detallado sobre el dibujo del Niño 2",
                          "p1Stars": 5,
                          "p2Stars": 5,
                          "verdictTitle": "Título celebratorio del veredicto",
                          "verdictAudioSummary": "Frase corta y entusiasta para leer en voz alta a los niños"
                        }
                        Sé siempre muy positivo y motivador con ambos niños.
                    """.trimIndent())
                }))
            }

            val generationConfig = JSONObject().apply {
                put("temperature", 0.4)
                put("responseMimeType", "application/json")
            }

            val requestJson = JSONObject().apply {
                put("systemInstruction", systemInstructionJson)
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", partsArray)
                }))
                put("generationConfig", generationConfig)
            }

            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(endpoint).post(body).build()

            val response = okHttpClient.newCall(request).execute()
            val responseText = response.body?.string() ?: ""

            if (response.isSuccessful && responseText.isNotBlank()) {
                val parsed = JSONObject(responseText)
                val candidates = parsed.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val content = candidates.getJSONObject(0).optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val rawJsonText = parts.getJSONObject(0).optString("text", "")
                        if (rawJsonText.isNotBlank()) {
                            val cleanJson = rawJsonText.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                            val resultObj = JSONObject(cleanJson)
                            return@withContext AiJudgeResult(
                                p1Feedback = resultObj.optString("p1Feedback", "¡Gran dibujo!"),
                                p2Feedback = resultObj.optString("p2Feedback", "¡Hermosa creatividad!"),
                                p1Stars = resultObj.optInt("p1Stars", 5).coerceIn(1, 5),
                                p2Stars = resultObj.optInt("p2Stars", 5).coerceIn(1, 5),
                                verdictTitle = resultObj.optString("verdictTitle", "¡Duelo Fantástico! 🏆"),
                                verdictAudioSummary = resultObj.optString("verdictAudioSummary", "¡Increíble trabajo de ambos mini artistas!")
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error calling Gemini Vision AI Judge", e)
        }

        return@withContext AiJudgeResult(
            p1Feedback = "¡Excelente dibujo lleno de energía y color!",
            p2Feedback = "¡Increíble trazo y creatividad mágica!",
            p1Stars = 5,
            p2Stars = 5,
            verdictTitle = "¡EMPATE MÁGICO DE ARTISTAS! 🏆🎨",
            verdictAudioSummary = "¡Felicidades a ambos niños! Los dos dibujos quedaron espectaculares."
        )
    }
}
