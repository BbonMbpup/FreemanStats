package com.example.freemanstats.domain.model

data class PlayerStats(
    val number: Int,
    val playerName: String,
    val warsParticipated: Int,
    val totalAttacks: Int,
    val totalStars: Int,
    val avgDestruction: Float,
    val totalUtility: Int
)