package com.example.freemanstats.domain.model

data class WarLogItem(
    val playerTag: String,
    val playerName: String,
    val playerNumber: Int, // mapPosition
    val isAttack: Boolean, // true = атака, false = защита
    val opponentTag: String,
    val opponentName: String,
    val opponentNumber: Int,
    val stars: Int, // звёзды (0-3)
    val points: Int, // +2 за 3★ атаку, +1 за защиту без 3★ и т.д.
    val order: Int
)