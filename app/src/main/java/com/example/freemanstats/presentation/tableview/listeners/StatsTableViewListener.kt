package com.example.freemanstats.presentation.tableview.listeners

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.example.freemanstats.presentation.tableview.holder.ColumnHeaderViewHolder
import com.example.freemanstats.presentation.tableview.popup.ColumnHeaderLongPressPopup

class StatsTableViewListener(private val tableView: TableView) : ITableViewListener {

    companion object {
        private const val TAG = "StatsTableViewListener"
    }

    override fun onCellClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
        Log.d(TAG, "Cell clicked at column=$column, row=$row")
    }

    override fun onCellDoubleClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
        TODO("Not yet implemented")
    }

    override fun onCellLongPressed(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
        Log.d(TAG, "Cell long-pressed at column=$column, row=$row")
    }
    override fun onColumnHeaderClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
        Log.d(TAG, "Column header clicked at column=$column")
    }
    override fun onColumnHeaderDoubleClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
        TODO("Not yet implemented")
    }
    override fun onColumnHeaderLongPressed(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
        Log.d(TAG, "Column header long-pressed at column=$column")

        if (columnHeaderView is ColumnHeaderViewHolder) {
            val popup = ColumnHeaderLongPressPopup(columnHeaderView, tableView)
            popup.show()
        }
    }
    override fun onRowHeaderClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
        TODO("Not yet implemented")
    }
    override fun onRowHeaderDoubleClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
        TODO("Not yet implemented")
    }
    override fun onRowHeaderLongPressed(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
        TODO("Not yet implemented")
    }
}