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


class MyCustomAdapter(var ctx: Context, var resource: Int, var items: ArrayList<ClockModel>) :
    ArrayAdapter<ClockModel>(ctx, resource, items) {


    private val formatter: SimpleDateFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(ctx)
        val view = layoutInflater.inflate(resource, null)


        val name = view.findViewById<TextView>(R.id.row_main_name)
        val time = view.findViewById<TextView>(R.id.row_main_time)

        name.text = items[position].Name
        time.text = items[position].CurrentTime

        return view
    }

    fun update() {
        val timeInMillis: Long = Calendar.getInstance().timeInMillis
        var needToNotifyFlag = false
        this.items.forEach {
            val timeStr = formatter.format(Date(timeInMillis + it.delay))
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
}