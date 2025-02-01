package com.pizzy.ptilms.data.repository

import com.pizzy.ptilms.data.model.DepartmentEntity
import kotlinx.coroutines.flow.Flow

interface DepartmentRepository {
    fun getAllDepartments(): Flow<List<DepartmentEntity>>
    suspend fun getDepartmentByName(name: String): DepartmentEntity?
    suspend fun insertDepartment(department: DepartmentEntity)
    suspend fun updateDepartment(department: DepartmentEntity)
    suspend fun deleteDepartment(department: DepartmentEntity)
}