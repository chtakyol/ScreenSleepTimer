package com.ToolCompany.screentimer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val APP_LAUNCH_COUNT = intPreferencesKey("app_launch_count")
        private val REVIEW_REQUESTED = booleanPreferencesKey("review_requested")
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }

    val appLaunchCount: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[APP_LAUNCH_COUNT] ?: 0
        }

    val reviewRequested: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[REVIEW_REQUESTED] ?: false
        }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun incrementLaunchCount() {
        dataStore.edit { preferences ->
            val currentCount = preferences[APP_LAUNCH_COUNT] ?: 0
            preferences[APP_LAUNCH_COUNT] = currentCount + 1
        }
    }

    suspend fun setReviewRequested(requested: Boolean) {
        dataStore.edit { preferences ->
            preferences[REVIEW_REQUESTED] = requested
        }
    }

    suspend fun resetLaunchCount() {
        dataStore.edit { preferences ->
            preferences[APP_LAUNCH_COUNT] = 0
            preferences[REVIEW_REQUESTED] = false
        }
    }
} 