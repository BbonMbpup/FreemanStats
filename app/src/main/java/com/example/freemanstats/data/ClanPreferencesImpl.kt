package com.example.freemanstats.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Singleton
class ClanPreferencesImpl constructor(
    private val context: Context
) : ClanPreferences {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("clan_prefs", Context.MODE_PRIVATE)
    }

    private val clanTagFlow = MutableStateFlow<String?>(null)

    override fun saveClanTag(clanTag: String) {
        prefs.edit {
            putString("clan_tag", clanTag)
        }
        clanTagFlow.value = clanTag
    }

    override fun getClanTag(): String? {
        return prefs.getString("clan_tag", null)
    }

    override fun getClanTagFlow(): kotlinx.coroutines.flow.Flow<String?> {
        if (clanTagFlow.value == null) {
            clanTagFlow.value = getClanTag()
        }
        return clanTagFlow
    }

    companion object {
        fun getClanTag(context: Context): String? {
            val prefs = context.getSharedPreferences("clan_prefs", Context.MODE_PRIVATE)
            return prefs.getString("clan_tag", null)
        }

        fun saveClanTag(context: Context, clanTag: String) {
            val prefs = context.getSharedPreferences("clan_prefs", Context.MODE_PRIVATE)
            prefs.edit {
                putString("clan_tag", clanTag)
            }
        }
    }
}