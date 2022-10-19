package com.pahnal.mystoryapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pahnal.mystoryapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "myDataStore")

object DataPref {

    suspend fun insertUser(user: User, context: Context) {
        context.dataStore.edit { pref ->
            pref[PreferencesKeys.USER_NAME] = user.name
            pref[PreferencesKeys.USER_ID] = user.userId
            pref[PreferencesKeys.USER_TOKEN] = user.token
        }
    }

    fun getUser(context: Context): Flow<User> {
        return context.dataStore.data.map { pref ->
            User(
                pref[PreferencesKeys.USER_NAME] ?: "",
                pref[PreferencesKeys.USER_ID] ?: "",
                pref[PreferencesKeys.USER_TOKEN] ?: "",
            )
        }
    }

    suspend fun deleteUser(context: Context) {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }
}


private object PreferencesKeys {
    val USER_TOKEN = stringPreferencesKey("user_data")
    val USER_ID = stringPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
}
