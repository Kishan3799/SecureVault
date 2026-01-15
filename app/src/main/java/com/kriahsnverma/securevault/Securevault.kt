package com.kriahsnverma.securevault

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.kriahsnverma.securevault.core.util.AppLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SecureVaultApp : Application() {

    @Inject lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }


}