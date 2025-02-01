package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.local.AssignmentDao
import com.pizzy.ptilms.data.model.AssignmentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignmentRepositoryImpl @Inject constructor(
    private val assignmentDao: AssignmentDao
) : AssignmentRepository {
    override suspend fun insertAssignment(assignment: AssignmentEntity) {
        assignmentDao.insertAssignment(assignment)
    }

    override suspend fun updateAssignment(assignment: AssignmentEntity) {
        assignmentDao.updateAssignment(assignment)
    }

    override suspend fun deleteAssignment(assignment: AssignmentEntity) {
        assignmentDao.deleteAssignment(assignment)
    }

    override fun getAllAssignments(): Flow<List<AssignmentEntity>> {
        return assignmentDao.getAssignments()
    }

    override suspend fun getAssignmentById(assignmentId: Int): AssignmentEntity? {
        return assignmentDao.getAssignmentById(assignmentId)
    }

    override fun getAssignmentsByCourseId(courseId: Int): Flow<List<AssignmentEntity>> {
        return assignmentDao.getAssignmentsByCourseId(courseId)
    }

    override fun getRecentAssignments(limit: Int): Flow<List<AssignmentEntity>> {
        return assignmentDao.getRecentAssignments(limit)
    }
}