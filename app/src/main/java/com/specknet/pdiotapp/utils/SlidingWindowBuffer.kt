package com.specknet.pdiotapp.utils

class SlidingWindowBuffer<T>(
    private val windowSize: Int,
    private val stepSize: Int
) {

    private val buffer = ArrayDeque<T>(windowSize)
    private var dataCountSinceLastWindow = 0

    fun addData(data: T) {
        buffer.add(data)
        if (buffer.size > windowSize) {
            buffer.removeFirst()
        }
        dataCountSinceLastWindow++
    }

    fun isReady(): Boolean {
        return buffer.size == windowSize && dataCountSinceLastWindow >= stepSize
    }

    fun getData(): List<T> {
        return if (isReady()) {
            dataCountSinceLastWindow = 0 // Reset the counter after retrieving the window
            buffer.toList()
        } else {
            emptyList() // Return an empty list if the window is not ready
        }
    }
}