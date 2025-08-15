package com.example.freemanstats.presentation.tableview.holder

import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.example.freemanstats.R
import com.example.freemanstats.presentation.tableview.model.ColumnHeaderModel

class ColumnHeaderViewHolder(
    itemView: View,
    private val tableView: ITableView
) : AbstractSorterViewHolder(itemView) {

    private val columnHeaderTextView: TextView = itemView.findViewById(R.id.column_header_textView)
    private val columnHeaderContainer: LinearLayout = itemView.findViewById(R.id.column_header_container)
    private val columnHeaderSortButton: ImageButton = itemView.findViewById(R.id.column_header_sort_imageButton)

    init {
        columnHeaderSortButton.setOnClickListener { onSortButtonClicked() }
    }

    fun setColumnHeaderModel(model: ColumnHeaderModel, columnPosition: Int) {
        columnHeaderTextView.gravity = COLUMN_TEXT_ALIGNS[columnPosition] or Gravity.CENTER_VERTICAL
        columnHeaderTextView.text = model.getData()
        columnHeaderContainer.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        columnHeaderTextView.requestLayout()
    }

    override fun setSelected(selectionState: SelectionState) {
        super.setSelected(selectionState)

        val (bgColor, textColor) = when (selectionState) {
            SelectionState.SELECTED -> R.color.selected_background_color to R.color.selected_text_color
            SelectionState.UNSELECTED -> R.color.unselected_header_background_color to R.color.unselected_text_color
            SelectionState.SHADOWED -> R.color.shadow_background_color to R.color.unselected_text_color
        }

        columnHeaderContainer.setBackgroundColor(ContextCompat.getColor(columnHeaderContainer.context, bgColor))
        columnHeaderTextView.setTextColor(ContextCompat.getColor(columnHeaderTextView.context, textColor))
    }

    override fun onSortingStatusChanged(sortState: SortState) {
        super.onSortingStatusChanged(sortState)
        controlSortState(sortState)

        // Пересчет размеров
        columnHeaderContainer.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        columnHeaderTextView.requestLayout()
        columnHeaderSortButton.requestLayout()
        columnHeaderContainer.requestLayout()
        itemView.requestLayout()
    }

    private fun controlSortState(sortState: SortState) {
        when (sortState) {
            SortState.ASCENDING -> {
                columnHeaderSortButton.visibility = View.VISIBLE
                columnHeaderSortButton.setImageResource(R.drawable.arrow_down)
            }
            SortState.DESCENDING -> {
                columnHeaderSortButton.visibility = View.VISIBLE
                columnHeaderSortButton.setImageResource(R.drawable.arrow_up)
            }
            else -> {
                columnHeaderSortButton.visibility = View.GONE
            }
        }
    }

    private fun onSortButtonClicked() {
        val position = adapterPosition
        when (sortState) {
            SortState.ASCENDING -> tableView.sortColumn(position, SortState.DESCENDING)
            SortState.DESCENDING -> tableView.sortColumn(position, SortState.ASCENDING)
            else -> tableView.sortColumn(position, SortState.DESCENDING)
        }
    }

    companion object {
        val COLUMN_TEXT_ALIGNS = intArrayOf(
            Gravity.CENTER,     // Id
            Gravity.LEFT,       // Name
            Gravity.LEFT,       // Nickname
            Gravity.LEFT,       // Email
            Gravity.CENTER,     // Birthday
            Gravity.CENTER,     // Gender
            Gravity.CENTER,     // Age
            Gravity.LEFT,       // Job
            Gravity.CENTER,     // Salary
            Gravity.CENTER,     // CreatedAt
            Gravity.CENTER,     // UpdatedAt
            Gravity.LEFT,       // Address
            Gravity.RIGHT,      // Zip Code
            Gravity.RIGHT,      // Phone
            Gravity.RIGHT       // Fax
        )
    }
}