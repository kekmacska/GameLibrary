package org.kekmacska.gamelibrary.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.userPrefs by preferencesDataStore("user_prefs")

private val LOGGED_IN_KEY = booleanPreferencesKey("logged_in")

suspend fun Context.saveLoggedIn() {
    userPrefs.edit { prefs -> prefs[LOGGED_IN_KEY] = true }
}

suspend fun Context.saveNotLoggedIn() {
    userPrefs.edit { prefs -> prefs[LOGGED_IN_KEY] = false }
}

val Context.isLoggedInFlow
    get() = userPrefs.data.map { prefs ->
        prefs[LOGGED_IN_KEY] ?: false
    }
