package com.example.freemanstats.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.freemanstats.model.WarLogItem

class WarLogDiffCallback : DiffUtil.ItemCallback<WarLogItem>() {
    override fun areItemsTheSame(oldItem: WarLogItem, newItem: WarLogItem): Boolean {
        return oldItem.order == newItem.order && oldItem.isAttack == newItem.isAttack
    }

    override fun areContentsTheSame(oldItem: WarLogItem, newItem: WarLogItem): Boolean {
        return oldItem == newItem
    }
}