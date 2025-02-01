package com.pizzy.ptilms.di

import com.pizzy.ptilms.data.DataStoreManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DataStoreEntryPoint {
    fun getDataStoreManager(): DataStoreManager
}