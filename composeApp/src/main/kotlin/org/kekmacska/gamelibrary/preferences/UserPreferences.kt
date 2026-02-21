package org.kekmacska.gamelibrary.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.userPrefs by preferencesDataStore("user_prefs")
private val NOT_LOGGED_IN_KEY = booleanPreferencesKey("not_logged_in")

//save
suspend fun Context.saveNotLoggedIn() {
    userPrefs.edit { prefs -> prefs[NOT_LOGGED_IN_KEY] = true }
}

//read
val Context.NotLoggedInSelectedFlow
    get() = userPrefs.data.map { prefs -> prefs[NOT_LOGGED_IN_KEY] ?: false }