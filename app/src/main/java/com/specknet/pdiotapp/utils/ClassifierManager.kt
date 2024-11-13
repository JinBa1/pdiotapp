package com.specknet.pdiotapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClassifierManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val buffer = mutableListOf<DataPacket>()

    fun initialize() {
        val filter = IntentFilter("ACTION_PROCESSED_DATA_BUFFER")
        context.registerReceiver(dataReceiver, filter)
    }

    // Broadcast receiver for processed data buffers
    private val dataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val dataBuffer = intent.getSerializableExtra("dataBuffer") as DataBuffer
            buffer.add(dataBuffer)
            if (bufferIsReady()) {
                scope.launch {
                    processAndBroadcastResult()
                }
            }
        }
    }

    private fun bufferIsReady(): Boolean {
        return buffer.size >= REQUIRED_BUFFER_SIZE
    }

    private suspend fun processAndBroadcastResult() {
        val result = classify(buffer.toList())
        buffer.clear()
        broadcastClassificationResult(result)
    }

    private fun classify(data: List<DataPacket>): String {
        // Classification logic
        return "classification_result"
    }

    private fun broadcastClassificationResult(result: String) {
        val intent = Intent("ACTION_CLASSIFICATION_RESULT")
        intent.putExtra("classificationResult", result)
        context.sendBroadcast(intent)
    }

    fun cleanup() {
        context.unregisterReceiver(dataReceiver)
        scope.cancel()  // Cancels any ongoing coroutines in this scope
    }
}