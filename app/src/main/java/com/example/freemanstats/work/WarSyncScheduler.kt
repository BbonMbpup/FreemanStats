package com.example.freemanstats.work

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit
import androidx.core.content.ContextCompat

object WarSyncScheduler {

    fun schedule(context: Context) {
        Log.d("WarSyncScheduler", "Планировщик войны запущен")
        val workRequest = PeriodicWorkRequestBuilder<WarSyncWorker>(
            1, TimeUnit.HOURS
        )
            .addTag("WarSyncWorker")
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        val operation = WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "WarSyncWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        operation.result.addListener({
            Log.d("WarSyncDebug", "Статус enqueue: ${operation.state}")
        }, ContextCompat.getMainExecutor(context))
    }
}