package com.example.freemanstats.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.freemanstats.domain.repository.ClanWarRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TestWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: ClanWarRepository
) : Worker(appContext, params) {

    override fun doWork(): Result {
        Log.d("TestWorker", "TestWorker сработал!")
        return Result.success()
    }
}