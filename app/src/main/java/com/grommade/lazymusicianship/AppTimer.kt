package com.grommade.lazymusicianship

import kotlinx.coroutines.delay

class AppTimer(private val timeRefresh: Long = TIME_REFRESH) {

    private var running = false

    private var _milli = 0L
    val milli get() = _milli

    private var startMilli = 0L

    suspend fun startStop(
        changeValue: (Long) -> Unit,
        initialValue: Long = 0L
    ) = when (running) {
        true -> stop()
        false -> start(initialValue, changeValue)
    }

    private suspend fun start(
        initialValue: Long,
        changeValue: (Long) -> Unit,
    ) {
        running = true
        _milli = initialValue
        startMilli = System.currentTimeMillis() - _milli

        while (running) {
            delay(timeRefresh)
            _milli = System.currentTimeMillis() - startMilli
            changeValue(_milli)
        }
    }

    private fun stop() {
        running = false
        _milli = System.currentTimeMillis() - startMilli
    }

    fun restore() {
        if (!running) {
            _milli = 0L
            startMilli = 0L
        }
    }

    companion object {
        const val TIME_REFRESH = 100L
    }
}