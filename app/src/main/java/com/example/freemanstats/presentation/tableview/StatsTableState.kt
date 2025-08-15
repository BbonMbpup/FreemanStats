package com.example.freemanstats.presentation.tableview

import com.example.freemanstats.domain.model.PlayerStats
import com.example.freemanstats.presentation.tableview.model.CellModel
import com.example.freemanstats.presentation.tableview.model.ColumnHeaderModel
import com.example.freemanstats.presentation.tableview.model.RowHeaderModel

data class StatsTableState(
    val isLoading: Boolean = false,
    val stats: List<PlayerStats>? = null,
    val columnHeaders: List<ColumnHeaderModel>? = null,
    val rowHeaders: List<RowHeaderModel>? = null,
    val cells: List<List<CellModel>>? = null,
    val error: String? = null
)