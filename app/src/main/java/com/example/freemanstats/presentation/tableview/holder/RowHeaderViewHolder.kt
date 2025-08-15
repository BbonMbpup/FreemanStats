package com.example.freemanstats.presentation.tableview.holder

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.example.freemanstats.R
import com.example.freemanstats.presentation.tableview.model.RowHeaderModel

class RowHeaderViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    val rowHeaderTextView: TextView = itemView.findViewById(R.id.row_header_textview)

    override fun setSelected(selectionState: SelectionState) {
        super.setSelected(selectionState)

        val (bgColor, textColor) = when (selectionState) {
            SelectionState.SELECTED -> R.color.selected_background_color to R.color.selected_text_color
            SelectionState.UNSELECTED -> R.color.unselected_header_background_color to R.color.unselected_text_color
            SelectionState.SHADOWED -> R.color.shadow_background_color to R.color.unselected_text_color
        }

        itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, bgColor))
        rowHeaderTextView.setTextColor(ContextCompat.getColor(rowHeaderTextView.context, textColor))
    }
}