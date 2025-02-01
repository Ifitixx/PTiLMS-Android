package com.pizzy.ptilms

import com.pizzy.ptilms.data.DatabasePopulator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val databasePopulator: DatabasePopulator
) {
    fun initialize(scope: CoroutineScope, onComplete: (Boolean) -> Unit = {}) {
        scope.launch(Dispatchers.IO) {
            val success = try {
                databasePopulator.populate(this)
                true
            } catch (e: Exception) {
                Timber.e(e, "Database initialization failed")
                false
            }
            withContext(Dispatchers.Main) {
                onComplete(success)
            }
        }
    }
}