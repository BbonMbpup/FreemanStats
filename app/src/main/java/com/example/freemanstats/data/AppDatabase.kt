package com.example.freemanstats.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.freemanstats.model.WarLogEntity

@Database(entities = [WarLogEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun warLogDao(): WarLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "war_logs_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}