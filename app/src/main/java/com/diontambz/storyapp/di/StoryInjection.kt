package com.diontambz.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.diontambz.storyapp.data.network.api.ApiConfig
import com.diontambz.storyapp.data.preference.UserPreferences
import com.diontambz.storyapp.data.repository.StoryRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyapp")

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiClient()
        return StoryRepository.getInstance(preferences, apiService)
    }
}