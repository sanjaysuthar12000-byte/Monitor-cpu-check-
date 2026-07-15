package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "optimization_logs")
data class OptimizationLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val eventType: String, // "CLEANUP", "BOOST", "SPOOF", "BATTERY"
    val title: String,
    val description: String,
    val savingsMah: Float = 0f
)
