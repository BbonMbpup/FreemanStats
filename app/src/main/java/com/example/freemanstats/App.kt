package com.example.freemanstats

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.freemanstats.data.SecurePrefs
import com.example.freemanstats.work.WarSyncScheduler
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.COC_API_KEY.isNotBlank()) {
            SecurePrefs.saveApiKey(this, "Bearer ${BuildConfig.COC_API_KEY}")
            Log.d("App", "API key saved from BuildConfig")
        } else {
            Log.w("App", "COC_API_KEY is empty. Network calls to Clash of Clans API will fail until it is configured")
        }

        WarSyncScheduler.schedule(this)

        val context = this
        val workInfosFuture = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork("WarSyncWork")
        workInfosFuture.addListener({
            try {
                val infos = workInfosFuture.get()
                Log.d("WarSyncDebug", "Всего задач: ${infos.size}")
                infos.forEach {
                    Log.d("WarSyncDebug", "State=${it.state}, ID=${it.id}, Tags=${it.tags}")
                }
            } catch (e: Exception) {
                Log.e("WarSyncDebug", "Ошибка при получении workInfos", e)
            }
        }, Executors.newSingleThreadExecutor())
    }
}
