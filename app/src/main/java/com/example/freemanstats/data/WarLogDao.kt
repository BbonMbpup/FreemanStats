package com.example.freemanstats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.freemanstats.model.WarLogEntity

@Dao
interface WarLogDao {
    @Insert
    suspend fun insertWarLog(log: WarLogEntity)

    @Query("SELECT * FROM war_logs ORDER BY warEndTime DESC")
    fun getAllWarLogs(): LiveData<List<WarLogEntity>>

    @Query("SELECT * FROM war_logs WHERE playerTag = :playerTag ORDER BY warEndTime DESC")
    fun getWarLogsForPlayer(playerTag: String): LiveData<List<WarLogEntity>>
}