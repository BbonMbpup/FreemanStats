package com.example.freemanstats.presentation.tableview.popup

import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.sort.SortState
import com.example.freemanstats.R
import com.example.freemanstats.presentation.tableview.holder.ColumnHeaderViewHolder

class ColumnHeaderLongPressPopup(
    private val viewHolder: ColumnHeaderViewHolder,
    private val tableView: ITableView
) : PopupMenu(viewHolder.itemView.context, viewHolder.itemView), PopupMenu.OnMenuItemClickListener {

    companion object {
        private const val ASCENDING = 1
        private const val DESCENDING = 2
        private const val ROW_HIDE = 3
        private const val ROW_SHOW = 4
        private const val TEST_ROW_INDEX = 4
    }

    private val context = viewHolder.itemView.context
    private val columnIndex = viewHolder.adapterPosition

    init {
        createMenuItems()
        updateMenuItemVisibility()
        setOnMenuItemClickListener(this)
    }

    private fun createMenuItems() {
        menu.add(Menu.NONE, ASCENDING, 0, context.getString(R.string.sort_ascending))
        menu.add(Menu.NONE, DESCENDING, 1, context.getString(R.string.sort_descending))
        menu.add(Menu.NONE, ROW_HIDE, 2, context.getString(R.string.row_hide))
        menu.add(Menu.NONE, ROW_SHOW, 3, context.getString(R.string.row_show))
    }

    private fun updateMenuItemVisibility() {
        when (tableView.getSortingStatus(columnIndex)) {
            SortState.ASCENDING -> menu.findItem(ASCENDING)?.isVisible = false
            SortState.DESCENDING -> menu.findItem(DESCENDING)?.isVisible = false
            SortState.UNSORTED -> {} // Show all
            else -> {}
        }

        if (tableView.isRowVisible(TEST_ROW_INDEX)) {
            menu.findItem(ROW_SHOW)?.isVisible = false
        } else {
            menu.findItem(ROW_HIDE)?.isVisible = false
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            ASCENDING -> tableView.sortColumn(columnIndex, SortState.ASCENDING)
            DESCENDING -> tableView.sortColumn(columnIndex, SortState.DESCENDING)
            ROW_HIDE -> tableView.hideRow(TEST_ROW_INDEX)
            ROW_SHOW -> tableView.showRow(TEST_ROW_INDEX)
        }

        // Перерасчёт ширины колонки (важно для обновления UI)
        tableView.remeasureColumnWidth(columnIndex)
        return true
    }
}