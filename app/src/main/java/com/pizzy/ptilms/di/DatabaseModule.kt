package com.pizzy.ptilms.di

import android.content.Context
import com.pizzy.ptilms.AppDatabase
import com.pizzy.ptilms.data.DatabasePopulator
import com.pizzy.ptilms.data.local.AnnouncementDao
import com.pizzy.ptilms.data.local.AssignmentDao
import com.pizzy.ptilms.data.local.ChatMessageDao
import com.pizzy.ptilms.data.local.ChatDao
import com.pizzy.ptilms.data.local.CourseDao
import com.pizzy.ptilms.data.local.DepartmentDao
import com.pizzy.ptilms.data.local.DepartmentLevelDao
import com.pizzy.ptilms.data.local.LevelDao
import com.pizzy.ptilms.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideDepartmentDao(appDatabase: AppDatabase): DepartmentDao {
        return appDatabase.departmentDao()
    }

    @Provides
    @Singleton
    fun provideLevelDao(appDatabase: AppDatabase): LevelDao {
        return appDatabase.levelDao()
    }

    @Provides
    @Singleton
    fun provideCourseDao(appDatabase: AppDatabase): CourseDao {
        return appDatabase.courseDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideAnnouncementDao(appDatabase: AppDatabase): AnnouncementDao {
        return appDatabase.announcementDao()
    }

    @Provides
    @Singleton
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }

    @Provides
    @Singleton
    fun provideChatMessageDao(appDatabase: AppDatabase): ChatMessageDao {
        return appDatabase.chatMessageDao()
    }

    @Provides
    @Singleton
    fun provideAssignmentDao(appDatabase: AppDatabase): AssignmentDao {
        return appDatabase.assignmentDao()
    }

    @Provides
    @Singleton
    fun provideDepartmentLevelDao(appDatabase: AppDatabase): DepartmentLevelDao {
        return appDatabase.departmentLevelDao()
    }

    @Provides
    @Singleton
    fun provideDatabasePopulator(
        departmentDao: DepartmentDao,
        levelDao: LevelDao,
        courseDao: CourseDao,
        departmentLevelDao: DepartmentLevelDao,
        appDatabase: AppDatabase // Add AppDatabase as a dependency
    ): DatabasePopulator {
        return DatabasePopulator(departmentDao, levelDao, courseDao, departmentLevelDao, appDatabase) // Pass AppDatabase to the constructor
    }
}