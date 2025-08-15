package com.example.freemanstats.presentation.currentwarpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freemanstats.domain.repository.ClanWarRepository
import com.example.freemanstats.domain.model.ClanWar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWarViewModel @Inject constructor(
    private val repository: ClanWarRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentWar = MutableLiveData<ClanWar?>()
    val currentWar: LiveData<ClanWar?> get() = _currentWar

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadCurrentWar(clanTag: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val clanWar = repository.getCurrentWar(clanTag)
                if (clanWar != null) {

                    if (clanWar.state == "notInWar") {
                        _error.value = "Клан не находится в состоянии войны"
                        Log.d("CurrentWarViewModel", "War State: ${error}")
                    } else {
                        _error.value = null
                    }
                    _currentWar.value = clanWar

                    // Логирование данных
                    Log.d("CurrentWarViewModel", "War State: ${clanWar.state}")
                    Log.d("CurrentWarViewModel", "Members count: ${clanWar.clan.members?.size ?: 0}")
                } else {
                    _error.value = "Данные о войне не найдены"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Произошла неизвестная ошибка"
                Log.e("CurrentWarViewModel", "Error loading current war: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}