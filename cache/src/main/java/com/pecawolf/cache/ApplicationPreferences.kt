package com.pecawolf.cache

import android.content.Context
import android.content.SharedPreferences

class ApplicationPreferences(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            "default",
            Context.MODE_PRIVATE
        )
    }

    var activeCharacterId: Long? = null
        get() = if (field == null)
            sharedPreferences.getLong(KEY_ACTIVE_CHARACTER, -1)
                .takeIf { it >= 0 }
                ?.also { field = it }
        else field
        set(value) {
            val edit = sharedPreferences.edit()

            if (value != null) edit.putLong(KEY_ACTIVE_CHARACTER, value)
            else edit.remove(KEY_ACTIVE_CHARACTER)

            field = value

            edit.commit()
        }

    companion object {
        private const val KEY_ACTIVE_CHARACTER = "KEY_ACTIVE_CHARACTER"
    }
}
