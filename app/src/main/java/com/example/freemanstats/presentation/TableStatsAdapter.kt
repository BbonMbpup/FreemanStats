package com.example.freemanstats.presentation

import android.content.res.Resources
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.example.freemanstats.R

class TableStatsAdapter : AbstractTableAdapter<SortableCellModel, SortableCellModel, SortableCellModel>() {


    private inner class CellViewHolder(view: View) : AbstractViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.cell_text)

        fun bind(item: SortableCellModel) {
            textView.text = item.getAsString()
            textView.setTextColor(Color.parseColor("#222222")) // Цвет текста
        }
    }

    private inner class ColumnHeaderViewHolder(view: View) : AbstractViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.header_text)

        fun bind(item: SortableCellModel) {
            textView.text = item.getAsString()
            textView.setBackgroundColor(Color.parseColor("#4285F4")) // Синий фон
            textView.setPadding(16.dpToPx(), 12.dpToPx(), 16.dpToPx(), 12.dpToPx())
            textView.textSize = 16f
        }
    }

    private inner class RowHeaderViewHolder(view: View) : AbstractViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.row_header_text)

        fun bind(item: SortableCellModel) {
            textView.text = item.getAsString()
            textView.setBackgroundColor(Color.LTGRAY)
            textView.setPadding(16.dpToPx(), 8.dpToPx(), 16.dpToPx(), 8.dpToPx())
        }
    }

    override fun onCreateCellViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_item, parent, false)
        return CellViewHolder(view)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItem: SortableCellModel?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        (holder as? CellViewHolder)?.apply {
            bind(cellItem ?: SortableCellModel(""))

            // Альтернирующие цвета строк
            if (rowPosition % 2 == 0) {
                itemView.setBackgroundColor(Color.WHITE)
            } else {
                itemView.setBackgroundColor(Color.parseColor("#F5F5F5"))
            }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        return ColumnHeaderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.colomn_header_item, parent, false)
        )
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItem: SortableCellModel?,
        columnPosition: Int
    ) {
        (holder as? ColumnHeaderViewHolder)?.bind(columnHeaderItem ?: SortableCellModel(""))
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder {
        return RowHeaderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_header_item, parent, false)
        )
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItem: SortableCellModel?,
        rowPosition: Int
    ) {
        (holder as? RowHeaderViewHolder)?.bind(rowHeaderItem ?: SortableCellModel(""))
    }



    override fun onCreateCornerView(parent: ViewGroup): View {
        return TextView(parent.context).apply {
            text = "#"
            gravity = Gravity.CENTER
            setBackgroundColor(Color.LTGRAY)
            setPadding(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())
        }
    }

    private fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}

