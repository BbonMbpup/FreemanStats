package com.example.freemanstats.presentation.tableview.holder

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.example.freemanstats.R
import com.example.freemanstats.presentation.tableview.model.CellModel

class CellViewHolder(view: View) : AbstractViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.cell_data)
    private val container: ViewGroup = view.findViewById(R.id.cell_container)

    fun setCellModel(cellModel: CellModel, columnPosition: Int) {
        val data = cellModel.getData()

        // Установка текста (с форматированием, если нужно)
        textView.text = formatCellText(data, columnPosition)

        // Выравнивание текста
        setupTextAlignment(columnPosition)

        // Стилизация ячейки (цвет и т.д.)
        setupCellStyle(columnPosition, data)

        // Принудительная пересборка макета
        container.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        textView.requestLayout()
    }

    override fun setSelected(selectionState: SelectionState) {
        super.setSelected(selectionState)
        container.setBackgroundResource(
            when (selectionState) {
                SelectionState.SELECTED -> R.color.selected_background_color
                else -> R.color.unselected_background_color
            }
        )
    }

    private fun formatCellText(data: Any?, column: Int): String {
        return if (data is Float && column == 5) {
            "%.1f%%".format(data)
        } else {
            data?.toString() ?: ""
        }
    }

    private fun setupTextAlignment(column: Int) {
        textView.gravity = when (column) {
            0, 2, 3, 4, 5, 6 -> Gravity.CENTER
            else -> Gravity.START
        } or Gravity.CENTER_VERTICAL
    }

    private fun setupCellStyle(column: Int, data: Any?) {
        when (column) {
            5 -> {
                val value = (data as? Float) ?: 0f
                val colorRes = when {
                    value >= 90f -> R.color.green
                    value >= 70f -> R.color.orange
                    else -> R.color.red
                }
                textView.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
            }
            6 -> {
                val value = (data as? Int) ?: 0
                val colorRes = if (value >= 0) R.color.green else R.color.red
                textView.setTextColor(ContextCompat.getColor(itemView.context, colorRes))
            }
            else -> {
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.unselected_text_color))
            }
        }
    }
}