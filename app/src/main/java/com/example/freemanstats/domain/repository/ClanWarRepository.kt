package com.example.freemanstats.domain.repository

import android.content.Context
import android.util.Log
import com.example.freemanstats.api.ClashOfClansApi
import com.example.freemanstats.data.AppDatabase
import com.example.freemanstats.data.SecurePrefs
import com.example.freemanstats.data.WarLogDao
import com.example.freemanstats.domain.model.ClanWar
import com.example.freemanstats.domain.model.PlayerStats
import com.example.freemanstats.domain.model.WarLogEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ClanWarRepository @Inject constructor(
    private val api: ClashOfClansApi,
    private val warLogDao: WarLogDao,
    @ApplicationContext private val context: Context
) {

    suspend fun getCurrentWar(clanTag: String): ClanWar? {
        return try {
            val response = api.getClanWar(clanTag)
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


    suspend fun saveWarResultToDatabase(war: ClanWar): Boolean {
        Log.d("Repository", "Сохраняем данные войны с endTime: ${war.endTime}")
        return try {
            val endTime = war.endTime ?: return false
            val logs = mutableListOf<WarLogEntity>()

            // Логи атак твоей команды
            war.clan.members?.forEach { member ->
                member.attacks?.forEach { attack ->
                    val opponent = war.opponent.members?.find { it.tag == attack.defenderTag }
                    logs.add(
                        WarLogEntity(
                            warEndTime = endTime,
                            playerTag = member.tag,
                            playerName = member.name,
                            playerPosition = member.mapPosition,
                            isAttack = true,
                            opponentTag = attack.defenderTag,
                            opponentName = opponent?.name ?: "Unknown",
                            opponentPosition = opponent?.mapPosition ?: 0,
                            stars = attack.stars,
                            points = calculateAttackPoints(attack.stars),
                            destructionPercentage = attack.destructionPercentage.toInt()
                        )
                    )
                }
            }

            // Логи атак противника
            war.opponent.members?.forEach { member ->
                member.attacks?.forEach { attack ->
                    val opponent = war.clan.members?.find { it.tag == attack.defenderTag }
                    logs.add(
                        WarLogEntity(
                            warEndTime = endTime,
                            playerTag = member.tag,
                            playerName = member.name,
                            playerPosition = member.mapPosition,
                            isAttack = false,
                            opponentTag = attack.defenderTag,
                            opponentName = opponent?.name ?: "Unknown",
                            opponentPosition = opponent?.mapPosition ?: 0,
                            stars = attack.stars,
                            points = calculateDefensePoints(attack.stars),
                            destructionPercentage = attack.destructionPercentage.toInt()
                        )
                    )
                }
            }

            // Один раз вставляем все логи в БД
            warLogDao.insertWarLogs(logs)
            Log.d("Repository", "Сохранено ${logs.size} логов войны")

            true
        } catch (e: Exception) {
            Log.e("Repository", "Ошибка при сохранении результатов: ${e.message}", e)
            false
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