package com.hunterwilhelm.offsetclocks

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class EditActivity : AppCompatActivity() {

    private fun visibleTransform(visible: Boolean): Int {
        return if (visible) View.VISIBLE else View.GONE
    }

    private var currentDelay: Long = 0
    private var superFineDelay: Long = 0
    private var editMode: EditMode = EditMode.SECOND
    private var playMode: PlayMode = PlayMode.PLAY
    private var pauseTime: Long = 0
    private var currentDelayUpdatedFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        registerListeners()
        registerTimers()
    }

    private fun registerTimers() {
        val self = this
        val clockText = findViewById<TextView>(R.id.edit_clock_text)
        val formatter = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                // performance improvement
                if (currentDelayUpdatedFlag || self.playMode == PlayMode.PLAY) {
                    val currentTime = Calendar.getInstance().timeInMillis
                    var clockTime = currentTime + self.currentDelay + self.superFineDelay

                    // pauses clock time when paused
                    if (self.playMode == PlayMode.PAUSE) {
                        clockTime -= currentTime - pauseTime
                    }

                    val newTimeText = formatter.format(Date(clockTime))

                    // this prevents the app from having to do a refresh unless the text is different
                    if (currentDelayUpdatedFlag || clockText.text.toString() != newTimeText) {
                        val (start, end) = when (editMode) {
                            EditMode.HOUR -> Pair(0, 2)
                            EditMode.MINUTE -> Pair(3, 5)
                            EditMode.SECOND -> Pair(6, 8)
                        }
                        val spannable = SpannableString(newTimeText)
                        spannable.setSpan(
                            UnderlineSpan(),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                        )
                        clockText.text = spannable
                    }
                }
            }
        }, 0, 50)
    }

    private fun registerListeners() {
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

        findViewById<Button>(R.id.edit_play_button).setOnClickListener {
            playMode = PlayMode.PLAY
            notifyPlayModeChanged()
        }
        findViewById<Button>(R.id.edit_pause_button).setOnClickListener {
            playMode = PlayMode.PAUSE
            notifyPlayModeChanged()
        }

        fun updateCurrentDelay(coarse: Boolean, minus: Boolean) {
            currentDelay += when (editMode) {
                EditMode.HOUR -> 3600000 * if (coarse) 6 else 1
                EditMode.MINUTE -> 60000 * if (coarse) 15 else 1
                EditMode.SECOND -> 1000 * if (coarse) 15 else 1
            } * if (minus) -1 else 1
            currentDelayUpdatedFlag = true
        }

        findViewById<Button>(R.id.edit_rewind_coarse).setOnClickListener {
            updateCurrentDelay(true, minus = true)
        }
        findViewById<Button>(R.id.edit_skip_coarse).setOnClickListener {
            updateCurrentDelay(true, minus = false)
        }
        findViewById<Button>(R.id.edit_rewind_fine).setOnClickListener {
            updateCurrentDelay(false, minus = true)
        }
        findViewById<Button>(R.id.edit_skip_fine).setOnClickListener {
            updateCurrentDelay(false, minus = false)
        }

        findViewById<SeekBar>(R.id.edit_seek_bar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                superFineDelay = progress.toLong() - 1000
                currentDelayUpdatedFlag = true

                val prefix = resources.getString(R.string.fine_tune_seconds)
                val seconds = "%.2f".format(superFineDelay / 1000F)
                val spannable = SpannableString("$prefix: $seconds seconds")
                spannable.setSpan(
                    UnderlineSpan(),
                    prefix.length + 2,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                findViewById<TextView>(R.id.edit_fine_tune_text).text = spannable
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


    }

    // play mode

    enum class PlayMode {
        PAUSE,
        PLAY
    }

    private fun notifyPlayModeChanged() {
        val playButton = findViewById<Button>(R.id.edit_play_button)
        val pauseButton = findViewById<Button>(R.id.edit_pause_button)

        playButton.visibility = visibleTransform(playMode != PlayMode.PLAY)
        pauseButton.visibility = visibleTransform(playMode == PlayMode.PLAY)

        if (playMode == PlayMode.PAUSE) {
            pauseTime = Calendar.getInstance().timeInMillis
        } else if (pauseTime != 0L) {
            currentDelay -= Calendar.getInstance().timeInMillis - pauseTime
        }
    }

    // edit mode

    enum class EditMode {
        HOUR,
        MINUTE,
        SECOND
    }

    private fun notifyEditModeChanged() {
        currentDelayUpdatedFlag = true
        updateText()
        updateButtons()
        updateBlocks()
    }

    private fun updateText() {
        findViewById<TextView>(R.id.edit_selected_mode_text).text = when (editMode) {
            EditMode.SECOND -> resources.getString(R.string.selected_seconds)
            EditMode.MINUTE -> resources.getString(R.string.selected_minutes)
            EditMode.HOUR -> resources.getString(R.string.selected_hours)
        }
        findViewById<Button>(R.id.edit_rewind_coarse).text = when (editMode) {
            EditMode.SECOND, EditMode.MINUTE -> resources.getString(R.string.minus_fifteen)
            EditMode.HOUR -> resources.getString(R.string.minus_six)
        }
        findViewById<Button>(R.id.edit_skip_coarse).text = when (editMode) {
            EditMode.SECOND, EditMode.MINUTE -> resources.getString(R.string.plus_fifteen)
            EditMode.HOUR -> resources.getString(R.string.plus_six)
        }
    }

    private fun updateButtons() {

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

    private fun updateBlocks() {
        findViewById<LinearLayout>(R.id.edit_fine_tune_container).visibility =
            visibleTransform(editMode == EditMode.SECOND)
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