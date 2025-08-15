package com.example.freemanstats.presentation.tableview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.example.freemanstats.R
import com.example.freemanstats.domain.model.PlayerStats
import com.example.freemanstats.presentation.tableview.holder.CellViewHolder
import com.example.freemanstats.presentation.tableview.holder.ColumnHeaderViewHolder
import com.example.freemanstats.presentation.tableview.holder.RowHeaderViewHolder
import com.example.freemanstats.presentation.tableview.model.CellModel
import com.example.freemanstats.presentation.tableview.model.ColumnHeaderModel
import com.example.freemanstats.presentation.tableview.model.RowHeaderModel


class TableAdapter(private val context: Context) : AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel, CellModel>() {

    private val tableViewModel = TableDataHelper()

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.cell_item, parent, false)
        return CellViewHolder(layout)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: CellModel?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val cell = cellItemModel as CellModel
        if (holder is CellViewHolder) {
            holder.setCellModel(cell, columnPosition)
        }
    }

    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractSorterViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.colomn_header_item, parent, false)
        return ColumnHeaderViewHolder(layout, tableView)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeaderModel?,
        xPosition: Int
    ) {
        val columnHeader = columnHeaderItemModel as ColumnHeaderModel
        (holder as ColumnHeaderViewHolder).setColumnHeaderModel(columnHeader, xPosition)
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.row_header_item, parent, false)
        return RowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeaderModel?,
        yPosition: Int
    ) {
        val rowHeader = rowHeaderItemModel as RowHeaderModel
        (holder as RowHeaderViewHolder).rowHeaderTextView.text = rowHeader.getData()
    }

    override fun onCreateCornerView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_corner, null, false)
    }

    override fun getColumnHeaderItemViewType(position: Int): Int = 0

    override fun getRowHeaderItemViewType(position: Int): Int = 0

    override fun getCellItemViewType(position: Int): Int {
        return tableViewModel.getCellItemViewType(position)
    }

    fun setUserList(userList: List<PlayerStats>) {
        tableViewModel.generateListForTableView(userList)
        setAllItems(
            tableViewModel.getColumnHeaderModelList(),
            tableViewModel.getRowHeaderModelList(),
            tableViewModel.getCellModelList()
        )
    }
}