package com.specknet.pdiotapp.utils

class SlidingWindowBuffer<T>(private val windowSize: Int, private val stepSize: Int) {
    private val buffer = ArrayDeque<T>(windowSize)

    fun addData(data: T) {
        buffer.add(data)
        if (buffer.size > windowSize) {
            buffer.removeFirst()
        }
    }

    fun isReady(): Boolean {
        return buffer.size == windowSize
    }

    fun getData(): List<T> {
        return buffer.toList()
    }
}