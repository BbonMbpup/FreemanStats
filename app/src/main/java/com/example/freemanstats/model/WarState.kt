package com.example.freemanstats.model

sealed class WarState {
    object Loading : WarState()
    data class Success(val clanWar: ClanWar) : WarState()
    data class Error(val message: String) : WarState()
}