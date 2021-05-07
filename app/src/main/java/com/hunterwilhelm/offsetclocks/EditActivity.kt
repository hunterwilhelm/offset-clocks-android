package com.hunterwilhelm.offsetclocks

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


enum class EditMode {
    HOUR,
    MINUTE,
    SECOND
}

class EditActivity : AppCompatActivity() {
    var editMode: EditMode = EditMode.SECOND

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        registerButtons()
    }

    private fun registerButtons() {
        findViewById<Button>(R.id.edit_hour_button_inactive).setOnClickListener {
            editMode = EditMode.HOUR
            notifyEditModeChanged()
        }
        findViewById<Button>(R.id.edit_minute_button_inactive).setOnClickListener {
            editMode = EditMode.MINUTE
            notifyEditModeChanged()
        }
        findViewById<Button>(R.id.edit_second_button_inactive).setOnClickListener {
            editMode = EditMode.SECOND
            notifyEditModeChanged()
        }
    }

    private fun notifyEditModeChanged() {
        updateText()
        updateButtons()
    }

    private fun updateText() {
        findViewById<TextView>(R.id.edit_selected_mode_text).text = when (editMode) {
            EditMode.SECOND -> resources.getString(R.string.selected_seconds)
            EditMode.MINUTE -> resources.getString(R.string.selected_minutes)
            EditMode.HOUR -> resources.getString(R.string.selected_hours)
        }
    }

    private fun updateButtons() {
        fun visibleTransform(visible: Boolean): Int {
            return if (visible) View.VISIBLE else View.GONE
        }

        val editHourI = findViewById<Button>(R.id.edit_hour_button_inactive)
        val editHour = findViewById<Button>(R.id.edit_hour_button)
        val editSecondI = findViewById<Button>(R.id.edit_second_button_inactive)
        val editSecond = findViewById<Button>(R.id.edit_second_button)
        val editMinuteI = findViewById<Button>(R.id.edit_minute_button_inactive)
        val editMinute = findViewById<Button>(R.id.edit_minute_button)

        editHour.visibility = visibleTransform(editMode == EditMode.HOUR)
        editHourI.visibility = visibleTransform(editMode != EditMode.HOUR)
        editMinute.visibility = visibleTransform(editMode == EditMode.MINUTE)
        editMinuteI.visibility = visibleTransform(editMode != EditMode.MINUTE)
        editSecond.visibility = visibleTransform(editMode == EditMode.SECOND)
        editSecondI.visibility = visibleTransform(editMode != EditMode.SECOND)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition

            overridePendingTransition(
                R.anim.nothing,
                R.anim.slide_out_right
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        // click on icon to go back
        //triangle icon on the main android toolbar.
        return if (id == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }
}