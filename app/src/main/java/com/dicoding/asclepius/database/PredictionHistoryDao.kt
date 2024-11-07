package com.dicoding.asclepius.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PredictionHistoryDao {
    @Insert
    suspend fun insertHistory(history: PredictionHistory)

    @Query("SELECT * FROM prediction_history ORDER BY date DESC")
    suspend fun getAllHistories(): List<PredictionHistory>

    @Query("DELETE FROM prediction_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Int)
}
