package com.example.freemanstats.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.freemanstats.databinding.ItemWarLogBinding
import com.example.freemanstats.model.WarLogItem

class WarLogAdapter : RecyclerView.Adapter<WarLogViewHolder>() {

    private var logs = emptyList<WarLogItem>()

    fun submitList(newLogs: List<WarLogItem>) {
        logs = newLogs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarLogViewHolder {
        val binding = ItemWarLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WarLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WarLogViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount() = logs.size
}