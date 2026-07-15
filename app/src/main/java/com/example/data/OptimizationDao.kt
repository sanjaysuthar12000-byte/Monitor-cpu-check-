package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OptimizationDao {
    @Query("SELECT * FROM optimization_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<OptimizationLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: OptimizationLog)

    @Query("DELETE FROM optimization_logs")
    suspend fun clearLogs()

    @Query("SELECT SUM(savingsMah) FROM optimization_logs")
    fun getTotalSavingsFlow(): Flow<Float?>
}
