package com.example.freemanstats.presentation

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast

//class CustomTableViewListener(
//    private val tableView: TableView,
//    private val context: Context
//) : ITableViewListener {
//
//    companion object {
//        private const val TAG = "TableViewListener"
//    }
//
//    // 1. Обработка клика по ячейке таблицы
//    override fun onCellClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
//        Log.d(TAG, "Клик по ячейке: столбец $column, строка $row")
//
//        // Получаем данные из ячейки
//        val cellData = tableView.adapter?.getCellItem(column, row)
//        Toast.makeText(context, "Выбрано: ${cellData?.getContent()}", Toast.LENGTH_SHORT).show()
//    }
//
//    // 2. Долгое нажатие на ячейку
//    override fun onCellLongPressed(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
//        Log.d(TAG, "Долгое нажатие на ячейку ($column, $row)")
//        showContextMenu(column, row)
//    }
//
//    // 3. Клик по заголовку столбца
//    override fun onColumnHeaderClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
//        Log.d(TAG, "Клик по заголовку столбца $column")
//
//        // Сортировка при клике на заголовок
//        val currentState = tableView.columnSortHandler?.getSortingStatus(column)
//        val newState = when (currentState) {
//            SortState.ASCENDING -> SortState.DESCENDING
//            SortState.DESCENDING -> SortState.UNSORTED
//            else -> SortState.ASCENDING
//        }
//        tableView.columnSortHandler?.sort(column, newState)
//    }
//
//    // 4. Долгое нажатие на заголовок столбца
//    override fun onColumnHeaderLongPressed(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
//        Log.d(TAG, "Долгое нажатие на заголовок столбца $column")
//
//        // Показываем меню с действиями для столбца
//        showColumnMenu(columnHeaderView.itemView, column)
//    }
//
//    // 5-7. Остальные обязательные методы (можно оставить пустыми)
//    override fun onRowHeaderClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {}
//    override fun onRowHeaderLongPressed(rowHeaderView: RecyclerView.ViewHolder, row: Int) {}
//    override fun onColumnHeaderDoubleClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {}
//    override fun onCellDoubleClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {}
//
//    // Вспомогательные методы
//    private fun showContextMenu(column: Int, row: Int) {
//        val popup = PopupMenu(context, tableView)
//        popup.menu.add("Удалить строку $row").setOnMenuItemClickListener {
//            // Логика удаления строки
//            true
//        }
//        popup.show()
//    }
//
//    private fun showColumnMenu(anchor: View, column: Int) {
//        val popup = PopupMenu(context, anchor)
//        popup.menu.add("Сортировать по возрастанию").setOnMenuItemClickListener {
//            tableView.columnSortHandler?.sort(column, SortState.ASCENDING)
//            true
//        }
//        popup.menu.add("Скрыть столбец").setOnMenuItemClickListener {
//            tableView.columnHeaderLayoutManager?.setVisibility(column, false)
//            true
//        }
//        popup.show()
//    }
//
//    override fun onRowHeaderDoubleClicked(
//        rowHeaderView: RecyclerView.ViewHolder,
//        row: Int
//    ) {
//        TODO("Not yet implemented")
//    }
//}