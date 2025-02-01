package com.pizzy.ptilms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pizzy.ptilms.data.model.DepartmentEntity

@Dao
interface DepartmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(department: DepartmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(departments: List<DepartmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: DepartmentEntity)

    @Update
    suspend fun updateDepartment(department: DepartmentEntity)

    @Delete
    suspend fun deleteDepartment(department: DepartmentEntity)

    @Query("SELECT * FROM departments")
    suspend fun getAllDepartments(): List<DepartmentEntity>

    @Query("SELECT * FROM departments WHERE name = :name")
    suspend fun getDepartmentByName(name: String): DepartmentEntity?

    @Query("SELECT * FROM departments WHERE id = :departmentId")
    suspend fun getDepartmentById(departmentId: Int): DepartmentEntity?

    @Query("DELETE FROM departments")
    suspend fun clearAll()
}