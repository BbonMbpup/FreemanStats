package com.example.freemanstats.domain.usecase

import com.example.freemanstats.domain.repository.ClanWarRepository
import com.example.freemanstats.domain.model.PlayerStats
import com.example.freemanstats.domain.repository.StatsRepositoryImpl
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val repository: StatsRepositoryImpl
) {
    suspend operator fun invoke(): List<PlayerStats> {
        return repository.getStats()
    }
}