package com.specknet.pdiotapp.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ActivityClassifier(private val context: Context) {

    private lateinit var interpreter: Interpreter

    init {
        loadModel()
    }

    private fun loadModel() {
        // Load the TFLite model from assets
        val assetFileDescriptor = context.assets.openFd("model_thingy.tflite")
        val fileInputStream = assetFileDescriptor.createInputStream()
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val modelByteBuffer = fileChannel.map(
            java.nio.channels.FileChannel.MapMode.READ_ONLY,
            startOffset,
            declaredLength
        )
        interpreter = Interpreter(modelByteBuffer)
    }

    // Accepts Float[] as data for easier use in Java
    fun classify(data: List<FloatArray>): String {
        val inputBuffer = convertToByteBuffer(data)
        val outputArray = Array(1) { FloatArray(NUM_CLASSES) }
        interpreter.run(inputBuffer, outputArray)
        val predictedClassIndex = outputArray[0].indexOfFirst { it == (outputArray[0].maxOrNull() ?: 0f) }
        return getLabelForClass(predictedClassIndex)
    }

    private fun convertToByteBuffer(data: List<FloatArray>): ByteBuffer {
        val windowSize = data.size
        val numFeatures = 3 // x, y, z
        val byteBuffer = ByteBuffer.allocateDirect(1 * windowSize * numFeatures * 4) // 4 bytes per float
        byteBuffer.order(ByteOrder.nativeOrder())

        data.forEach { array ->
            byteBuffer.putFloat(array[0]) // x
            byteBuffer.putFloat(array[1]) // y
            byteBuffer.putFloat(array[2]) // z
        }

        return byteBuffer
    }

    private fun getLabelForClass(index: Int): String {
        val labels = arrayOf(
            "ascending", "descending", "lyingBack",
            "lyingLeft", "lyingRight", "lyingStomach",
            "miscMovement", "normalWalking", "running",
            "shuffleWalking", "sittingStanding"
        )
        return if (index in labels.indices) labels[index] else "Unknown"
    }

    companion object {
        private const val NUM_CLASSES = 11  // Adjust to match the output classes of your model
    }
}