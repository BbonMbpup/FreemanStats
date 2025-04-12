package com.example.freemanstats.data

import android.content.Context
import android.util.Log
import com.example.freemanstats.api.ClashOfClansApi
import com.example.freemanstats.model.ClanWar
import com.example.freemanstats.model.WarLogEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClanWarRepository(private val api: ClashOfClansApi, context: Context) {

    private val apiKey: String by lazy {
        SecurePrefs.getApiKey(context) ?: throw IllegalStateException("API key not set!")
    }
    private val warLogDao by lazy {
        AppDatabase.getDatabase(context).warLogDao()
    }


    suspend fun getCurrentWar(clanTag: String): ClanWar? {
        return try {
            val response = api.getClanWar(clanTag, apiKey)

            if (response.isSuccessful) {
                response.body()?.also { logWarData(it) }
            } else {
                Log.e("Repository", "API error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Repository", "Network error: ${e.message}")
            null
        }
    }


    private fun logWarData(clanWar: ClanWar) {
        Log.d("Repository", """
            War State: ${clanWar.state}
            Clan: ${clanWar.clan.name} (${clanWar.clan.tag})
            Opponent: ${clanWar.opponent.name}
            Members: ${clanWar.clan.members?.size ?: 0} vs ${clanWar.opponent.members?.size ?: 0}
        """.trimIndent())
    }

    suspend fun saveWarResults(war: ClanWar) {
        val endTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            .format(Date())

        war.clan.members?.forEach { member ->
            // Сохраняем атаки
            member.attacks?.forEach { attack ->
                val log = WarLogEntity(
                    warEndTime = endTime,
                    playerTag = member.tag,
                    playerName = member.name,
                    playerPosition = member.mapPosition,
                    isAttack = true,
                    opponentTag = attack.defenderTag,
                    opponentName = war.opponent.members?.find { it.tag == attack.defenderTag }?.name ?: "Unknown",
                    opponentPosition = war.opponent.members?.find { it.tag == attack.defenderTag }?.mapPosition ?: 0,
                    stars = attack.stars,
                    points = calculateAttackPoints(attack.stars),
                    destructionPercentage = attack.destructionPercentage.toInt()
                )
                warLogDao.insertWarLog(log)
            }

            // Сохраняем защиты
            member.bestOpponentAttack?.let { defense ->
                val log = WarLogEntity(
                    warEndTime = endTime,
                    playerTag = member.tag,
                    playerName = member.name,
                    playerPosition = member.mapPosition,
                    isAttack = false,
                    opponentTag = defense.attackerTag,
                    opponentName = war.opponent.members?.find { it.tag == defense.attackerTag }?.name ?: "Unknown",
                    opponentPosition = war.opponent.members?.find { it.tag == defense.attackerTag }?.mapPosition ?: 0,
                    stars = defense.stars,
                    points = calculateDefensePoints(defense.stars),
                    destructionPercentage = defense.destructionPercentage.toInt()
                )
                warLogDao.insertWarLog(log)
            }
        }
    }

    private fun calculateAttackPoints(stars: Int): Int {
        return when (stars) {
            3 -> 2
            2 -> 1
            1 -> 0
            else -> -1
        }
    }

    private fun calculateDefensePoints(stars: Int): Int {
        return when (stars) {
            0 -> 1
            1 -> 0
            else -> -1
        }
    }
}