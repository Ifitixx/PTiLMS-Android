package com.pizzy.ptilms.di

import android.content.Context
import com.pizzy.ptilms.data.local.StorageHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideStorageHelper(@ApplicationContext context: Context): StorageHelper {
        return StorageHelper(context)
    }
}