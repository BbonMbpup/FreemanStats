package com.example.freemanstats.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.freemanstats.domain.model.PlayerStats
import com.example.freemanstats.domain.model.WarLogEntity

@Dao
interface WarLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarLog(log: WarLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarLogs(logs: List<WarLogEntity>)

    @Query("SELECT * FROM war_logs ORDER BY warEndTime DESC")
    fun getAllWarLogs(): LiveData<List<WarLogEntity>>

    @Query("SELECT * FROM war_logs WHERE playerTag = :playerTag ORDER BY warEndTime DESC")
    fun getWarLogsForPlayer(playerTag: String): LiveData<List<WarLogEntity>>

    @Query("""
        SELECT 
            playerTag,
            playerName,
            playerPosition AS number,
            COUNT(DISTINCT warEndTime) AS warsParticipated,
            SUM(CASE WHEN isAttack THEN 1 ELSE 0 END) AS totalAttacks,
            SUM(CASE WHEN isAttack THEN stars ELSE 0 END) AS totalStars,
            AVG(CASE WHEN isAttack THEN destructionPercentage ELSE NULL END) AS avgDestruction,
            SUM(points) AS totalUtility
        FROM war_logs
        GROUP BY playerTag
        ORDER BY totalUtility DESC
    """)
    suspend fun getPlayerStats(): List<PlayerStats>
}