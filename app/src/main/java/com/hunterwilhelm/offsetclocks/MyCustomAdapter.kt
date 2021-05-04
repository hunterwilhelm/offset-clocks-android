package com.hunterwilhelm.offsetclocks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class MyCustomAdapter(var ctx: Context, var resource: Int, var items: ArrayList<Model>) :
    ArrayAdapter<Model>(ctx, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(ctx)
        val view = layoutInflater.inflate(resource, null)


        val name = view.findViewById<TextView>(R.id.row_main_name)
        val time = view.findViewById<TextView>(R.id.row_main_time)

        name.text = items[position].Name
        time.text = items[position].CurrentTime

        return view
    }
}