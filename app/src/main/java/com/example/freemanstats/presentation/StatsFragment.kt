package com.example.freemanstats.presentation


import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.TableView
import com.example.freemanstats.R

import com.evrencoskun.tableview.listener.ITableViewListener
import com.evrencoskun.tableview.sort.SortState
import com.example.freemanstats.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var tableView: TableView
    private lateinit var adapter: TableStatsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tableView = binding.tableView
        adapter = TableStatsAdapter()
        tableView.setAdapter(adapter)

        // Настройка таблицы
        setupTableView()

        // загрузка данных
        loadDummyData()
    }

    private fun setupTableView() {
        tableView.apply {
            setHasFixedWidth(false)
            setShowHorizontalSeparators(true)
            setShowVerticalSeparators(true)
            setSeparatorColor(Color.GRAY)
            setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
            setRowHeaderWidth(80.dpToPx())



            tableViewListener = object : ITableViewListener {
                override fun onCellClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
                    val text = cellView.itemView.findViewById<TextView>(R.id.cell_text)?.text
                    Toast.makeText(context, "Clicked: $text", Toast.LENGTH_SHORT).show()
                }

                override fun onColumnHeaderClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
                    val currentState = columnSortHandler?.getSortingStatus(column)
                    val newState = when (currentState) {
                        SortState.ASCENDING -> SortState.DESCENDING
                        SortState.DESCENDING -> SortState.UNSORTED
                        else -> SortState.ASCENDING
                    }
                    columnSortHandler?.sort(column, newState)
                }

                // Остальные обязательные методы интерфейса
                override fun onCellDoubleClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {}
                override fun onCellLongPressed(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {}
                override fun onColumnHeaderDoubleClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {}
                override fun onColumnHeaderLongPressed(columnHeaderView: RecyclerView.ViewHolder, column: Int) {}
                override fun onRowHeaderClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {}
                override fun onRowHeaderDoubleClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {}
                override fun onRowHeaderLongPressed(rowHeaderView: RecyclerView.ViewHolder, row: Int) {}
            }
        }
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()


    private fun loadDummyData() {
        val columnHeaders = (1..10).map { SortableCellModel("Колонка $it") }
        val rowHeaders = (1..100).map { SortableCellModel("row_$it", "Строка $it") }

        val cellItems = mutableListOf<List<SortableCellModel>>()

        for (rowIndex in 0 until 100) {
            val row = mutableListOf<SortableCellModel>()
            for (columnIndex in 0 until 10) {
                row.add(SortableCellModel("$rowIndex-$columnIndex", "Ячейка ${rowIndex + 1}:${columnIndex + 1}"))
            }
            cellItems.add(row)
        }

        tableView.adapter?.setAllItems(columnHeaders, rowHeaders, cellItems)
    }
}