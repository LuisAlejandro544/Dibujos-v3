package com.example.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * Text-To-Speech Engine for Non-Reading Children and Accessibility.
 * Converts UI labels, tool names, color selections, and layer actions into friendly Spanish speech.
 */
object VoiceAssistant {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    fun init(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale("es", "ES"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.language = Locale.getDefault()
                    }
                    tts?.setPitch(1.15f) // Friendly kid-appealing voice pitch
                    tts?.setSpeechRate(0.95f) // Clear cadence for young listeners
                    isInitialized = true
                    Log.i("VoiceAssistant", "TextToSpeech initialized successfully for KidsDraw.")
                } else {
                    Log.w("VoiceAssistant", "TextToSpeech initialization failed.")
                }
            }
        }
    }

    fun speak(text: String, isEnabled: Boolean) {
        if (!isEnabled || text.isBlank()) return
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "KidsDrawVoiceID")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
