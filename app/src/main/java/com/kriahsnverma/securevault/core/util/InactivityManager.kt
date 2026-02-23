package com.kriahsnverma.securevault.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InactivityManager(
    private val timeoutProvider: () -> Long,
    private val onTimeout: () -> Unit,
) {
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun resetTimer() {
        job?.cancel()
        val timeout = timeoutProvider()

        // If timeout is -1, it means 'Never'
        if (timeout == -1L) return

        job = scope.launch {
            delay(timeout)
            onTimeout()
        }
    }

    fun stopTracking() {
        job?.cancel()
    }
}
