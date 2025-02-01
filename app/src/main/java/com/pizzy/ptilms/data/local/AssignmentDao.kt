package com.pizzy.ptilms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pizzy.ptilms.data.model.AssignmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity)

    @Update
    suspend fun updateAssignment(assignment: AssignmentEntity)

    @Delete
    suspend fun deleteAssignment(assignment: AssignmentEntity)

    @Query("SELECT * FROM assignments")
    fun getAssignments(): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE assignmentId = :assignmentId")
    suspend fun getAssignmentById(assignmentId: Int): AssignmentEntity?

    @Query("SELECT * FROM assignments WHERE courseId = :courseId")
    fun getAssignmentsByCourseId(courseId: Int): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments ORDER BY dueDate DESC LIMIT :limit")
    fun getRecentAssignments(limit: Int): Flow<List<AssignmentEntity>>
}