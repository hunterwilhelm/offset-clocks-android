package com.hunterwilhelm.offsetclocks

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyCustomAdapter
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var timerHandler: Handler
    private lateinit var doUpdateTimeout: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            addClock()
        }

        val items = ArrayList<Model>()
        items.add(Model("Actual", "010:00:00AM", 0))
        items.add(Model("System", "010:00:00AM", 1000))
        val listView = findViewById<NonScrollListView>(R.id.nonscroll_list)
        this.myAdapter = MyCustomAdapter(this, R.layout.row_main, items)
        listView.adapter = myAdapter


        // setup for timer
        this.doUpdateTimeout = Runnable { myAdapter.notifyDataSetChanged() }
        mHandlerThread = HandlerThread("my-handler")
        mHandlerThread.start()
        timerHandler = Handler(mHandlerThread.looper)

        val timer = Timer()
        val tick = UpdateClass(myAdapter)

        timer.scheduleAtFixedRate(tick, 0, 100)
    }

    private class UpdateClass(var myCustomAdapter: MyCustomAdapter) : TimerTask() {

        override fun run() {
            myCustomAdapter.update()
        }

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