package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.local.DepartmentDao
import com.pizzy.ptilms.data.model.DepartmentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DepartmentRepositoryImpl @Inject constructor(
    private val departmentDao: DepartmentDao
) : DepartmentRepository {

    override fun getAllDepartments(): Flow<List<DepartmentEntity>> = flow {
        val departments = departmentDao.getAllDepartments()
        emit(departments)
    }.flowOn(Dispatchers.IO)

    override suspend fun getDepartmentByName(name: String): DepartmentEntity? {
        return try {
            departmentDao.getDepartmentByName(name)
        } catch (e: Exception) {
            // Log the error for debugging purposes
            println("Error fetching department by name: ${e.message}")
            null
        }
    }

    override suspend fun insertDepartment(department: DepartmentEntity) {
        try {
            departmentDao.insertDepartment(department)
        } catch (e: Exception) {
            println("Error inserting department: ${e.message}")
            throw e
        }
    }

    override suspend fun updateDepartment(department: DepartmentEntity) {
        try {
            departmentDao.updateDepartment(department)
        } catch (e: Exception) {
            println("Error updating department: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteDepartment(department: DepartmentEntity) {
        try {
            departmentDao.deleteDepartment(department)
        } catch (e: Exception) {
            println("Error deleting department: ${e.message}")
            throw e
        }
    }
}