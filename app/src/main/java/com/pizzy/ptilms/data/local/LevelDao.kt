package com.pizzy.ptilms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pizzy.ptilms.data.model.LevelEntity

@Dao
interface LevelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(level: LevelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(levels: List<LevelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevel(level: LevelEntity)

    @Update
    suspend fun updateLevel(level: LevelEntity)

    @Delete
    suspend fun deleteLevel(level: LevelEntity)

    @Query("SELECT * FROM levels")
    suspend fun getAllLevels(): List<LevelEntity>

    @Query("SELECT * FROM levels WHERE id = :levelId")
    suspend fun getLevelById(levelId: Int): LevelEntity?

    @Query("SELECT * FROM levels WHERE name = :name")
    suspend fun getLevelByName(name: String): LevelEntity?

    @Query("DELETE FROM levels")
    suspend fun clearAll()
}