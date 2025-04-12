package com.example.freemanstats.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freemanstats.data.ClanWarRepository
import com.example.freemanstats.model.Attack
import com.example.freemanstats.model.ClanWar
import com.example.freemanstats.model.ClanWarConverter
import com.example.freemanstats.model.ClanWarConverter.toWarLogItems
import com.example.freemanstats.model.Member
import com.example.freemanstats.model.WarLogEntity
import com.example.freemanstats.model.WarLogItem
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: ClanWarRepository) : ViewModel() {

    private val _warLogs = MutableLiveData<List<WarLogItem>>()
    val warLogs: LiveData<List<WarLogItem>> = _warLogs

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentWar = MutableLiveData<ClanWar?>()
    val currentWar: LiveData<ClanWar?> = _currentWar

    fun loadHistory(clanTag: String) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                // Загружаем текущую войну
                val currentWar = repository.getCurrentWar(clanTag)
                _currentWar.value = currentWar

                // Объединяем и преобразовываем данные
                val allLogs = mutableListOf<WarLogItem>().apply {
                    currentWar?.let { addAll(convertCurrentWarToLogItems(it)) }
                }

                _warLogs.value = mutableListOf<WarLogItem>().apply {
                    currentWar?.let { addAll(convertCurrentWarToLogItems(it)) }
                }.sortedByDescending { it.order }

            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.localizedMessage}"
                Log.e("HistoryViewModel", "Error loading history", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun convertCurrentWarToLogItems(currentWar: ClanWar): List<WarLogItem> {
        val clanMembers = currentWar.clan.members ?: return emptyList()
        val opponentMembers = currentWar.opponent.members ?: return emptyList()

        val logItems = mutableListOf<WarLogItem>()
        val currentTime = System.currentTimeMillis().toInt() // Для порядка событий

        clanMembers.forEach { member ->
            // Обрабатываем атаки
            member.attacks?.forEach { attack ->
                val defender = opponentMembers.find { it.tag == attack.defenderTag }
                logItems.add(
                    WarLogItem(
                        playerTag = member.tag,
                        playerName = member.name,
                        playerNumber = member.mapPosition,
                        isAttack = true,
                        opponentTag = attack.defenderTag,
                        opponentName = defender?.name ?: "Unknown",
                        opponentNumber = defender?.mapPosition ?: 0,
                        stars = attack.stars,
                        points = calculateAttackPoint(member, attack),
                        order = attack.order
                    )
                )
            }
        }

        opponentMembers.forEach { member ->
            // Обрабатываем защиты
            member.attacks?.forEach { attack ->
                val defender = clanMembers.find { it.tag == attack.defenderTag }
                logItems.add(
                    WarLogItem(
                        playerTag = defender?.tag ?: "?",
                        playerName = defender?.name ?: "Unknown",
                        playerNumber = defender?.mapPosition ?: 0,
                        isAttack = false,
                        opponentTag = member.tag,
                        opponentName = member.name,
                        opponentNumber = member.mapPosition,
                        stars = attack.stars,
                        points = if (attack.stars == 0) 1 else 0, // +1 за успешную защиту
                        order = attack.order
                    )
                )
            }
        }

        return logItems.sortedBy { it.order }
    }

    private fun calculateAttackPoint(member: Member, attack: Attack): Int {

        val defender = currentWar.value?.opponent?.members?.find { it.tag == attack.defenderTag } ?: return -1

        return when {
            // 3 звезды против зеркала или выше
            attack.stars == 3 && member.mapPosition >= defender.mapPosition -> 2

            // 3 звезды против ниже зеркала
            attack.stars == 3 && member.mapPosition < defender.mapPosition -> 1

            // 0-2 звезды против любого
            attack.stars in 0..2 -> -1

            // Все остальные случаи (на всякий случай)
            else -> 0
        }
    }
}