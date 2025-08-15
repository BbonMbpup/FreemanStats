package com.example.freemanstats.presentation.historypage

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.freemanstats.R
import com.example.freemanstats.databinding.ItemWarLogBinding
import com.example.freemanstats.domain.model.WarLogItem

class WarLogViewHolder(
    private val binding: ItemWarLogBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WarLogItem) {
        with(binding) {
            tvPlayerNumber.text = item.playerNumber.toString()
            tvPlayerName.text = item.playerName

            val actionIcon = if (item.isAttack) R.drawable.sword else R.drawable.shield
            ivActionType.setImageResource(actionIcon)

            val arrowIcon = if (item.isAttack) R.drawable.arrows_right else R.drawable.arrows_left
            ivDirection.setImageResource(arrowIcon)

            tvOpponentNumber.text = item.opponentNumber.toString()
            tvOpponentName.text = item.opponentName

            val pointsText = if (item.points >= 0) "+${item.points}" else item.points.toString()
            tvPoints.text = pointsText
            tvPoints.setTextColor(
                ContextCompat.getColor(root.context,
                    if (item.points >= 0) R.color.green else R.color.red)
            )
        }
    }
}