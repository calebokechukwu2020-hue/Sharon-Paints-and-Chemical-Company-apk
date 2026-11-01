package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SharonVoiceSpeaker(private val context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = null
  private var isInitialized = false

  private val _isSpeaking = MutableStateFlow(false)
  val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

  private val _currentText = MutableStateFlow("")
  val currentText: StateFlow<String> = _currentText.asStateFlow()

  private val _statusMessage = MutableStateFlow("AI Voice Engine Ready")
  val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

  init {
    tts = TextToSpeech(context.applicationContext, this)
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val result = tts?.setLanguage(Locale.ENGLISH)
      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        _statusMessage.value = "Default English voice loaded"
      } else {
        _statusMessage.value = "Sharon AI Voice Consultant Ready"
      }
      tts?.setPitch(1.0f)
      tts?.setSpeechRate(0.95f) // Friendly natural cadence
      isInitialized = true

      tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
          _isSpeaking.value = true
        }

        override fun onDone(utteranceId: String?) {
          _isSpeaking.value = false
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
          _isSpeaking.value = false
        }
      })
    } else {
      _statusMessage.value = "TTS Initialization error"
    }
  }

  fun speak(text: String) {
    if (!isInitialized || tts == null) {
      _statusMessage.value = "Initializing Voice engine..."
      return
    }
    _currentText.value = text
    stop()
    val utteranceId = "sharon_${System.currentTimeMillis()}"
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
  }

  fun stop() {
    if (tts != null && tts?.isSpeaking == true) {
      tts?.stop()
    }
    _isSpeaking.value = false
  }

  fun shutdown() {
    try {
      tts?.stop()
      tts?.shutdown()
    } catch (_: Exception) {}
  }
}
