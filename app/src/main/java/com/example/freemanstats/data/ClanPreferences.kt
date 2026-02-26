package com.example.freemanstats.data

interface ClanPreferences {
    fun saveClanTag(clanTag: String)
    fun getClanTag(): String?
    fun getClanTagFlow(): kotlinx.coroutines.flow.Flow<String?>
}