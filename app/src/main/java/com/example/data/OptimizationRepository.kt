package com.example.data

import kotlinx.coroutines.flow.Flow

class OptimizationRepository(private val optimizationDao: OptimizationDao) {
    val allLogs: Flow<List<OptimizationLog>> = optimizationDao.getAllLogs()
    val totalSavings: Flow<Float?> = optimizationDao.getTotalSavingsFlow()

    suspend fun insert(log: OptimizationLog) {
        optimizationDao.insertLog(log)
    }

    suspend fun clear() {
        optimizationDao.clearLogs()
    }
}
