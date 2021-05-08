package com.hunterwilhelm.offsetclocks

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class MainActivity : AppCompatActivity() {

    private var fabButtonDisabled: Boolean = false
    private lateinit var myAdapter: MainClockListAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        val items = ArrayList<ClockModel>()
        items.add(ClockModel("Your Phone's Time", "", 0, false))
        val listView = findViewById<NonScrollListView>(R.id.nonscroll_list)
        this.myAdapter = MainClockListAdapter(this, R.layout.row_main, items)
        listView.adapter = myAdapter

        sharedPreferences = applicationContext.getSharedPreferences(
            getString(R.string.preference_key_clock_storage),
            MODE_PRIVATE
        )

        // setup for timer
        registerTimers()
        registerListeners()
    }

    private fun registerTimers() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                myAdapter.update()
            }
        }, 0, 50)
    }

    private fun registerListeners() {
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            fabButtonDisabled = true
            addClock()
        }
    }

    private fun loadData() {
        val clockKey = getString(R.string.preference_key_clocks)

        val hs: HashSet<String> =
            sharedPreferences.getStringSet(clockKey, HashSet()) as HashSet<String>
        myAdapter.clear()
        val gson = Gson()
        if (hs.count() == 0) {
            val clock = ClockModel("Your Phone's Time", "", 0, false)
            myAdapter.add(clock)
            with(sharedPreferences.edit()) {
                val set = HashSet<String>()
                set.add(gson.toJson(clock))
                putStringSet(clockKey, set)
                apply()
            }
        } else {
            hs.forEach {
                try {
                    val clock = gson.fromJson(it, ClockModel::class.java)
                    myAdapter.add(clock)
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                }
            }
        }
        myAdapter.notifyDataSetChanged()
    }

    private fun addClock() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition

            startActivity(Intent(this, EditActivity::class.java))
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.nothing
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
        fabButtonDisabled = false
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