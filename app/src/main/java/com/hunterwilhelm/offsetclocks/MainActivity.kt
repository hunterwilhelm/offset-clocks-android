package com.hunterwilhelm.offsetclocks

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var fabButtonDisabled: Boolean = false
    private lateinit var myAdapter: MainClockListAdapter
    private lateinit var sPrefs: SharedPreferences
    private lateinit var listView: NonScrollListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        listView = findViewById<NonScrollListView>(R.id.nonscroll_list)
        myAdapter = MainClockListAdapter(this, R.layout.row_main, ArrayList<ClockModel>())
        listView.adapter = myAdapter

        sPrefs = applicationContext.getSharedPreferences(
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
            editClock(null, null)
        }
        listView.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
            editClock(myAdapter.getItem(i), i)
        }
    }

    private fun loadData() {
        val gson = Gson()
        val clockKey = getString(R.string.preference_key_clocks)
        val clocks = Utils.getClocksFromStorage(sPrefs, clockKey)

        myAdapter.clear()
        if (clocks.count() == 0) {
            val defaultClock = ClockModel(getString(R.string.defualt_clock_name), "", 0, false)
            myAdapter.add(defaultClock)

            val clocksJson = gson.toJson(myAdapter.items)
            with(sPrefs.edit()) {
                putString(clockKey, clocksJson)
                apply()
            }
        } else {
            clocks.forEach {
                myAdapter.add(it)
            }
        }
        myAdapter.notifyDataSetChanged()
    }

    private fun editClock(clock: ClockModel?, index: Int?) {
        val intent = Intent(this, EditActivity::class.java)
        if (clock != null && index != null) {
            intent.putExtra(IntentExtraConstants.CLOCK_DELAY.name, clock.delay)
            intent.putExtra(IntentExtraConstants.CLOCK_INDEX.name, index)
            intent.putExtra(IntentExtraConstants.CLOCK_NAME.name, clock.Name)
            intent.putExtra(IntentExtraConstants.CLOCK_NEGATIVE.name, clock.isSeekBarNegative)
        }
        startActivity(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
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