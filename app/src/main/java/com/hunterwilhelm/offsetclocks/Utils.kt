package com.hunterwilhelm.offsetclocks

import android.content.SharedPreferences
import android.util.Log
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
            Log.d("COUNTDEBUG", clocks.count().toString())
            return clocks
        }
    }
}