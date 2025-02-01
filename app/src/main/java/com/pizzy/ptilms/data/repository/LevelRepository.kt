package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.LevelEntity
import kotlinx.coroutines.flow.Flow

interface LevelRepository {
    fun getAllLevels(): Flow<List<LevelEntity>>
    suspend fun getLevelById(levelId: Int): LevelEntity?
    suspend fun getLevelByName(name: String): LevelEntity?
    suspend fun insertLevel(level: LevelEntity)
    suspend fun updateLevel(level: LevelEntity)
    suspend fun deleteLevel(level: LevelEntity)
}