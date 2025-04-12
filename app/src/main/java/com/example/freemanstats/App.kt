package com.example.freemanstats

import android.app.Application
import android.util.Log
import com.example.freemanstats.data.SecurePrefs

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SecurePrefs.saveApiKey(this, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6ImU4MjJhMGViLTkxOTYtNGI0Mi1hY2IwLThjZDk3MmQyOWNlMiIsImlhdCI6MTc0MjA4NTA2MCwic3ViIjoiZGV2ZWxvcGVyLzBmOTZhY2JlLWQzNTYtNDI1OS1kY2E0LWU2MTY1YWJhOGZhNSIsInNjb3BlcyI6WyJjbGFzaCJdLCJsaW1pdHMiOlt7InRpZXIiOiJkZXZlbG9wZXIvc2lsdmVyIiwidHlwZSI6InRocm90dGxpbmcifSx7ImNpZHJzIjpbIjUuMzUuMzMuMTgxIl0sInR5cGUiOiJjbGllbnQifV19.NoNdyGXFP8POvidYpwfcHMOVEHuGEeL8GEVU_7qqZ_b3Kr8Mo-Le-i4d2fpJlSNxW9hFd7ktvie-FYFUaeowig") // ← Сохраняем здесь
        Log.d("App", "API key saved: ${SecurePrefs.getApiKey(this) != null}")
    }
}