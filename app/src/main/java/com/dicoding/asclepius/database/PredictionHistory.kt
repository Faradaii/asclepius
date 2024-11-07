package com.dicoding.asclepius.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "prediction_history")
data class PredictionHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val result: String,
    val confidenceScore: Int,
    val date: Date,
)
