package com.pizzy.ptilms.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pizzy.ptilms.data.model.CourseEntity
import com.pizzy.ptilms.data.model.DepartmentLevelCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentLevelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: DepartmentLevelCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartmentLevelCrossRef(crossRef: DepartmentLevelCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(crossRefs: List<DepartmentLevelCrossRef>)

    @Query("SELECT c.* FROM courses c INNER JOIN department_level_cross_ref dlcr ON c.departmentId = dlcr.departmentId AND c.levelId = dlcr.levelId WHERE dlcr.departmentId = :departmentId AND dlcr.levelId = :levelId")
    fun getCoursesByDepartmentAndLevel(departmentId: Int, levelId: Int): Flow<List<CourseEntity>>

    @Query("DELETE FROM department_level_cross_ref")
    suspend fun clearAll()
}