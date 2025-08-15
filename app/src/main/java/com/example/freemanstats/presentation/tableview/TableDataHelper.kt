package com.example.freemanstats.presentation.tableview

import com.example.freemanstats.domain.model.PlayerStats
import com.example.freemanstats.presentation.tableview.model.CellModel
import com.example.freemanstats.presentation.tableview.model.ColumnHeaderModel
import com.example.freemanstats.presentation.tableview.model.RowHeaderModel

class TableDataHelper {

    // Типы ячеек (если нужно кастомное отображение, например формат %, иконки и т.д.)
    companion object {
        const val DEFAULT_TYPE = 0
        const val PERCENT_TYPE = 1
    }

    private lateinit var columnHeaderModelList: List<ColumnHeaderModel>
    private lateinit var rowHeaderModelList: List<RowHeaderModel>
    private lateinit var cellModelList: List<List<CellModel>>

    fun getCellItemViewType(column: Int): Int {
        return when (column) {
            5 -> PERCENT_TYPE // "Разрушение"
            else -> DEFAULT_TYPE
        }
    }

    fun getColumnHeaderModelList(): List<ColumnHeaderModel> = columnHeaderModelList

    fun getRowHeaderModelList(): List<RowHeaderModel> = rowHeaderModelList

    fun getCellModelList(): List<List<CellModel>> = cellModelList

    fun generateListForTableView(players: List<PlayerStats>) {
        columnHeaderModelList = createColumnHeaderModelList()
        cellModelList = createCellModelList(players)
        rowHeaderModelList = createRowHeaderModelList(players.size)
    }

    private fun createColumnHeaderModelList(): List<ColumnHeaderModel> {
        return listOf(
            ColumnHeaderModel("#"),
            ColumnHeaderModel("Игрок"),
            ColumnHeaderModel("Войны"),
            ColumnHeaderModel("Атаки"),
            ColumnHeaderModel("Звёзды"),
            ColumnHeaderModel("Разрушение"),
            ColumnHeaderModel("Польза")
        )
    }

    private fun createCellModelList(players: List<PlayerStats>): List<List<CellModel>> {
        val lists = mutableListOf<List<CellModel>>()

        players.forEachIndexed { index, player ->
            val row = listOf(
                CellModel("number_${index}", player.number),
                CellModel("name_${index}", player.playerName),
                CellModel("wars_${index}", player.warsParticipated),
                CellModel("attacks_${index}", player.totalAttacks),
                CellModel("stars_${index}", player.totalStars),
                CellModel("destruction_${index}", "%.1f%%".format(player.avgDestruction)),
                CellModel("utility_${index}", player.totalUtility)
            )
            lists.add(row)
        }

        return lists
    }

    private fun createRowHeaderModelList(size: Int): List<RowHeaderModel> {
        return List(size) { index ->
            RowHeaderModel((index + 1).toString())
        }
    }
}