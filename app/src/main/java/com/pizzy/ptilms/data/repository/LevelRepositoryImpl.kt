package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.local.LevelDao
import com.pizzy.ptilms.data.model.LevelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LevelRepositoryImpl @Inject constructor(
    private val levelDao: LevelDao
) : LevelRepository {

    override fun getAllLevels(): Flow<List<LevelEntity>> = flow {
        val levels = levelDao.getAllLevels()
        emit(levels)
    }.flowOn(Dispatchers.IO)

    override suspend fun getLevelById(levelId: Int): LevelEntity? {
        return levelDao.getLevelById(levelId)
    }

    override suspend fun getLevelByName(name: String): LevelEntity? {
        return try {
            levelDao.getLevelByName(name)
        } catch (e: Exception) {
            // Log the error for debugging purposes
            println("Error fetching level by name: ${e.message}")
            null
        }
    }

    override suspend fun insertLevel(level: LevelEntity) {
        try {
            levelDao.insertLevel(level)
        } catch (e: Exception) {
            println("Error inserting level: ${e.message}")
            throw e
        }
    }

    override suspend fun updateLevel(level: LevelEntity) {
        try {
            levelDao.updateLevel(level)
        } catch (e: Exception) {
            println("Error updating level: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteLevel(level: LevelEntity) {
        try {
            levelDao.deleteLevel(level)
        } catch (e: Exception) {
            println("Error deleting level: ${e.message}")
            throw e
        }
    }
}