package com.example.freemanstats.presentation.tableview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freemanstats.domain.usecase.GetStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsTableViewModel @Inject constructor(
    private val getStatsUseCase: GetStatsUseCase
) : ViewModel() {

    private val _state = MutableLiveData<StatsTableState>()
    val state: LiveData<StatsTableState> = _state

    // Используем утилиту для подготовки данных таблицы
    private val tableDataHelper = TableDataHelper()

    init {
        loadStats()
        Log.d("StatsViewModel", "StatsTableViewModel created")
    }

    fun loadStats() {
        _state.value = StatsTableState(isLoading = true)

        viewModelScope.launch {
            try {
                val players = getStatsUseCase()
                Log.d("StatsViewModel", "Получено записей: ${players.size}")
                Log.d("StatsViewModel", "Полученные записей: ${players}")

                tableDataHelper.generateListForTableView(players)

                _state.value = StatsTableState(
                    isLoading = false,
                    stats = players,
                    columnHeaders = tableDataHelper.getColumnHeaderModelList(),
                    rowHeaders = tableDataHelper.getRowHeaderModelList(),
                    cells = tableDataHelper.getCellModelList()
                )

            } catch (e: Exception) {
                _state.value = StatsTableState(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}"
                )
            }
        }
    }



    fun getCellItemViewType(column: Int): Int {
        return tableDataHelper.getCellItemViewType(column)
    }
}