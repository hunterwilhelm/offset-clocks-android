package com.hunterwilhelm.offsetclocks

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))



        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            addClock()
        }

        val items = ArrayList<Model>()
        items.add(Model("Phone", "07:00:00AM"))
        items.add(Model("Paris", "08:00:00AM"))
        items.add(Model("Seminary", "09:00:00AM"))
        items.add(Model("Phone", "010:00:00AM"))
        items.add(Model("Phone", "010:00:00AM"))
        items.add(Model("Phone", "010:00:00AM"))
        items.add(Model("Phone", "010:00:00AM"))
        items.add(Model("Phone", "010:00:00AM"))
        items.add(Model("Phone", "010:00:00AM"))
        val listView = findViewById<NonScrollListView>(R.id.lv_nonscroll_list)
        listView.adapter = MyCustomAdapter(this, R.layout.row_main, items)
    }

    fun addClock() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition

            startActivity(Intent(this, EditActivity::class.java))
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.nothing
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}