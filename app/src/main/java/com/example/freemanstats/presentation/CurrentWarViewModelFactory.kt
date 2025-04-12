package com.example.freemanstats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freemanstats.data.ClanWarRepository

class CurrentWarViewModelFactory(
    private val repository: ClanWarRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentWarViewModel::class.java)) {
            return CurrentWarViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}