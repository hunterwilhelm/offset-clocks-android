package com.hunterwilhelm.offsetclocks

import android.content.SharedPreferences
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Utils {
    companion object {
        fun getClocksFromStorage(sPref: SharedPreferences, key: String): ArrayList<ClockModel> {
            val clocks = ArrayList<ClockModel>()
            val clockJsonSet: String? = sPref.getString(key, null)
            if (clockJsonSet != null) {
                try {
                    val listType: Type = object : TypeToken<List<ClockModel>>() {}.type
                    val result = Gson().fromJson<List<ClockModel>>(clockJsonSet, listType)
                    clocks.addAll(result)
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                }
            }
            return clocks
        }

        fun putClocksIntoStorage(
            sPref: SharedPreferences,
            key: String,
            clocks: ArrayList<ClockModel>
        ) {
            val clocksJson = Gson().toJson(clocks)
            with(sPref.edit()) {
                putString(key, clocksJson)
                apply()
            }
        }

        fun getBooleanPreference(
            sPref: SharedPreferences,
            key: String
        ): Boolean {
            return sPref.getBoolean(key, false)
        }

        fun setBooleanPreference(
            sPref: SharedPreferences,
            key: String,
            value: Boolean
        ) {
            with(sPref.edit()) {
                putBoolean(key, value)
                apply()
            }
        }

        /*
        I got tired of writing the same ternary operator,
        so I turned it into an Util
         */
        fun visibleTransform(visible: Boolean): Int {
            return if (visible) View.VISIBLE else View.GONE
        }
    }
}