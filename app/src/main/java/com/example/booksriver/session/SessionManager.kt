package com.example.booksriver.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.booksriver.data.model.User
import com.google.gson.Gson

class SessionManager(private val sharedPreferences: SharedPreferences) {

    private fun saveToken(token: String?) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_TOKEN, token)
        }
    }

    fun saveUser(user: User?) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_USER, Gson().toJson(user))
        }
        if (user != null) {
            saveToken(user.token)
        } else {
            saveToken(null)
        }
    }

    fun getToken(): String? = sharedPreferences.getString(KEY_TOKEN, null)

    fun getUser(): User? =
        Gson().fromJson(sharedPreferences.getString(KEY_USER, null), User::class.java)

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "auth_user"
    }
}

object SharedPreferencesFactory {
    private const val FILE_NAME_SESSION_PREF = "auth_shared_pref"

    fun sessionPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            FILE_NAME_SESSION_PREF,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
