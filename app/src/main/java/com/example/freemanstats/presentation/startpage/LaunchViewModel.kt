package com.example.freemanstats.presentation.startpage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.freemanstats.data.ClanPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val clanPreferences: ClanPreferences
) : ViewModel() {

    private val _navigationEvent = MutableLiveData<Boolean>()
    val navigationEvent: LiveData<Boolean> = _navigationEvent

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun hasClanTag(): Boolean {
        return clanPreferences.getClanTag() != null
    }

    fun saveClanTag(clanTag: String) {
        if (clanTag.isEmpty()) {
            _error.value = "Введите тег клана"
            return
        }

        if (!isValidClanTag(clanTag)) {
            _error.value = "Неверный формат тега. Должен начинаться с #"
            return
        }

        clanPreferences.saveClanTag(clanTag)
        _navigationEvent.value = true
    }

    private fun isValidClanTag(tag: String): Boolean {
        return tag.startsWith("#") && tag.length in 4..15
    }
}