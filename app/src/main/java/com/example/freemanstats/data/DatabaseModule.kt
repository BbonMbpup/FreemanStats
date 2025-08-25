package com.example.freemanstats.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.freemanstats.domain.model.WarLogEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "freeman_database")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("Database", "База данных создана")

                    // Вставка тестовых данных после создания базы
                    CoroutineScope(Dispatchers.IO).launch {
                        val database = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "freeman_database"
                        ).build()

                        val dao = database.warLogDao()

                        val testData = listOf(
                            WarLogEntity(
                                warEndTime = "2025-06-23T20:00:00.000Z",
                                playerTag = "#AAA",
                                playerName = "Игрок1",
                                playerPosition = 1,
                                isAttack = true,
                                opponentTag = "#BBB",
                                opponentName = "Враг1",
                                opponentPosition = 1,
                                stars = 3,
                                points = 2,
                                destructionPercentage = 100
                            ),
                            WarLogEntity(
                                warEndTime = "2025-06-23T20:00:00.000Z",
                                playerTag = "#AAA",
                                playerName = "Игрок1",
                                playerPosition = 1,
                                isAttack = false,
                                opponentTag = "#CCC",
                                opponentName = "Враг2",
                                opponentPosition = 2,
                                stars = 0,
                                points = 1,
                                destructionPercentage = 45
                            )
                        )

                        dao.insertWarLogs(testData)
                        Log.d("Database", "Добавлено тестовых записей: ${testData.size}")
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWarLogDao(db: AppDatabase): WarLogDao = db.warLogDao()
}