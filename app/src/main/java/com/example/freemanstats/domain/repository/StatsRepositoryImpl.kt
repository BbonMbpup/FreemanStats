package com.example.freemanstats.domain.repository

import android.util.Log
import com.example.freemanstats.data.WarLogDao
import com.example.freemanstats.domain.model.PlayerStats
import javax.inject.Inject

class StatsRepositoryImpl @Inject constructor(
    private val warLogDao: WarLogDao
) {
    suspend fun getStats(): List<PlayerStats> {
        val result = warLogDao.getPlayerStats()
        Log.d("StatsRepositoryImpl", "Получено из БД: ${result.size}")
        return result
    }
}