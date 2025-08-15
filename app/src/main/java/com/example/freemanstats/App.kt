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
        SecurePrefs.saveApiKey(this, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjRkMTM3ZGNjLTRhZjUtNDJmMy05MzEyLTgxYzUxYjU4OTkyNCIsImlhdCI6MTc1MjQ4MDg3Miwic3ViIjoiZGV2ZWxvcGVyLzBmOTZhY2JlLWQzNTYtNDI1OS1kY2E0LWU2MTY1YWJhOGZhNSIsInNjb3BlcyI6WyJjbGFzaCJdLCJsaW1pdHMiOlt7InRpZXIiOiJkZXZlbG9wZXIvc2lsdmVyIiwidHlwZSI6InRocm90dGxpbmcifSx7ImNpZHJzIjpbIjUuMzUuMzMuMTgxIl0sInR5cGUiOiJjbGllbnQifV19.YGoHEvDRwUgbG8oRhf68pXd-K6q_jeTRm6GrnH4i0mlUXq2NzHeky9nNFWSITruUG-p989_cQRXKmejmM-28kA")
        Log.d("App", "API key saved: ${SecurePrefs.getApiKey(this) != null}")

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