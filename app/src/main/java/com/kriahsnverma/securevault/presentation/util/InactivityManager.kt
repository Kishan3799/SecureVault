package com.kriahsnverma.securevault.presentation.util


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InactivityManager(
    private val onTimeout: () -> Unit,
) {
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun resetTimer() {
        job?.cancel()
        job = scope.launch {
            delay(60_000) // 1 Minute
            onTimeout()
        }
    }

    fun stopTracking() {
        job?.cancel()
    }
}