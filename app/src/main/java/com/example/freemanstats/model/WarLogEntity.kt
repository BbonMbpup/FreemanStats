package com.example.freemanstats.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "war_logs")
data class WarLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val warEndTime: String, // Дата окончания войны
    val playerTag: String,
    val playerName: String,
    val playerPosition: Int,
    val isAttack: Boolean,
    val opponentTag: String,
    val opponentName: String,
    val opponentPosition: Int,
    val stars: Int,
    val points: Int,
    val destructionPercentage: Int
)