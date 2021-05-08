package com.hunterwilhelm.offsetclocks

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class MainClockListAdapter(var ctx: Context, var resource: Int, var items: ArrayList<ClockModel>) :
    ArrayAdapter<ClockModel>(ctx, resource, items) {


    private var setting24HourTime: Boolean = false
    private var settingShowDay: Boolean = false
    private val formatter: SimpleDateFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    private val formatter24: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val formatterDay: SimpleDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(ctx)
        val view = layoutInflater.inflate(resource, null)


        val name = view.findViewById<TextView>(R.id.row_main_name)
        val time = view.findViewById<TextView>(R.id.row_main_time)

        name.text = items[position].Name
        time.text = items[position].CurrentTime
        with(view.findViewById<TextView>(R.id.row_main_day)) {
            if (settingShowDay) {
                text = formatterDay.format(Calendar.getInstance().timeInMillis + items[position].delay)
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }

        return view
    }

    fun update() {
        val timeInMillis: Long = Calendar.getInstance().timeInMillis
        var needToNotifyFlag = false
        val currentFormatter = if (setting24HourTime) formatter24 else formatter
        items.forEach {
            val timeStr = currentFormatter.format(Date(timeInMillis + it.delay))
            if (timeStr != it.CurrentTime) {
                it.CurrentTime = timeStr
                needToNotifyFlag = true
            }
        }
        if (needToNotifyFlag) {
            (ctx as Activity).runOnUiThread {
                notifyDataSetChanged()
            }
        }
    }

    fun updateSettings(_setting24HourTime: Boolean, _settingShowDay: Boolean) {
        setting24HourTime = _setting24HourTime
        settingShowDay = _settingShowDay
        (ctx as Activity).runOnUiThread {
            notifyDataSetChanged()
        }
    }
}