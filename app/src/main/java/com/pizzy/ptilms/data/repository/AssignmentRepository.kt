package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.AssignmentEntity
import kotlinx.coroutines.flow.Flow

interface AssignmentRepository {
    suspend fun insertAssignment(assignment: AssignmentEntity)
    suspend fun updateAssignment(assignment: AssignmentEntity)
    suspend fun deleteAssignment(assignment: AssignmentEntity)
    fun getAllAssignments(): Flow<List<AssignmentEntity>>
    suspend fun getAssignmentById(assignmentId: Int): AssignmentEntity?
    fun getAssignmentsByCourseId(courseId: Int): Flow<List<AssignmentEntity>>
    fun getRecentAssignments(limit: Int): Flow<List<AssignmentEntity>>
}