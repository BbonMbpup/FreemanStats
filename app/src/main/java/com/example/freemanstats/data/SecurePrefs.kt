package com.example.freemanstats.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePrefs {
    private const val PREFS_NAME = "secure_prefs"
    private const val API_KEY = "api_key"

    // Инициализация EncryptedSharedPreferences
    private fun getSharedPreferences(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    // Сохранить ключ
    fun saveApiKey(context: Context, key: String) {
        getSharedPreferences(context).edit().putString(API_KEY, key).apply()
    }

    // Получить ключ
    fun getApiKey(context: Context): String? {
        return getSharedPreferences(context).getString(API_KEY, null)
    }
}