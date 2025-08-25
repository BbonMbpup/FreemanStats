package com.example.freemanstats.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.freemanstats.domain.model.WarLogEntity

@Database(entities = [WarLogEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun warLogDao(): WarLogDao
}