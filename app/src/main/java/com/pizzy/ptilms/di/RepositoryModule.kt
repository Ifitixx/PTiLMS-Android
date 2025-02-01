package com.pizzy.ptilms.di

import com.pizzy.ptilms.data.repository.AnnouncementRepository
import com.pizzy.ptilms.data.repository.AnnouncementRepositoryImpl
import com.pizzy.ptilms.data.repository.AssignmentRepository
import com.pizzy.ptilms.data.repository.AssignmentRepositoryImpl
import com.pizzy.ptilms.data.repository.CommunicationRepository
import com.pizzy.ptilms.data.repository.CommunicationRepositoryImpl
import com.pizzy.ptilms.data.repository.CourseRepository
import com.pizzy.ptilms.data.repository.CourseRepositoryImpl
import com.pizzy.ptilms.data.repository.DepartmentRepository
import com.pizzy.ptilms.data.repository.DepartmentRepositoryImpl
import com.pizzy.ptilms.data.repository.LevelRepository
import com.pizzy.ptilms.data.repository.LevelRepositoryImpl
import com.pizzy.ptilms.data.repository.UserRepository
import com.pizzy.ptilms.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAnnouncementRepository(announcementRepositoryImpl: AnnouncementRepositoryImpl): AnnouncementRepository

    @Binds
    @Singleton
    abstract fun bindCourseRepository(courseRepositoryImpl: CourseRepositoryImpl): CourseRepository

    @Binds
    @Singleton
    abstract fun bindAssignmentRepository(assignmentRepositoryImpl: AssignmentRepositoryImpl): AssignmentRepository

    @Binds
    @Singleton
    abstract fun bindCommunicationRepository(communicationRepositoryImpl: CommunicationRepositoryImpl): CommunicationRepository

    @Binds
    @Singleton
    abstract fun bindDepartmentRepository(departmentRepositoryImpl: DepartmentRepositoryImpl): DepartmentRepository

    @Binds
    @Singleton
    abstract fun bindLevelRepository(levelRepositoryImpl: LevelRepositoryImpl): LevelRepository
}