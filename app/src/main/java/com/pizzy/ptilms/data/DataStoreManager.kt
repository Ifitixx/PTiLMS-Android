package com.pizzy.ptilms.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore

    object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val USER_ROLE = stringPreferencesKey("user_role")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val AUTH_EXPIRATION = longPreferencesKey("auth_expiration")
        val INITIAL_SCREEN = stringPreferencesKey("initial_screen")
    }

    val userId: Flow<String?> = dataStore.data
        .catch { exception ->
            handleDataStoreError(exception)
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.USER_ID]
        }

    val username: Flow<String?> = dataStore.data
        .catch { exception ->
            handleDataStoreError(exception)
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.USERNAME]
        }

    val userRole: Flow<String?> = dataStore.data
        .catch { exception ->
            handleDataStoreError(exception)
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.USER_ROLE]
        }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .catch { exception ->
            handleDataStoreError(exception)
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }

    val initialScreen: Flow<String?> = dataStore.data
        .catch { exception ->
            handleDataStoreError(exception)
            emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[PreferencesKeys.INITIAL_SCREEN]
        }

    suspend fun updateUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    suspend fun updateUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
        }
    }

    suspend fun updateUserRole(role: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ROLE] = role
        }
    }

    suspend fun setLoggedInState(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun updateInitialScreen(screenRoute: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.INITIAL_SCREEN] = screenRoute
        }
    }

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun handleDataStoreError(exception: Throwable) {
        when (exception) {
            is IOException -> {
                // Log detailed information about the IOException
                Timber.e(exception, "DataStore IO error: ${exception.message}")

                // Example of more specific logging based on the type of IO operation
                when {
                    exception.message?.contains("read") == true -> {
                        Timber.e(exception, "DataStore read error")
                    }

                    exception.message?.contains("write") == true -> {
                        Timber.e(exception, "DataStore write error")
                    }

                    else -> {
                        Timber.e(exception, "DataStore generic IO error")
                    }
                }
            }

            else -> {
                Timber.e(exception, "Unexpected DataStore error")
            }
        }
    }
}