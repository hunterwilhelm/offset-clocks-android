package com.hunterwilhelm.offsetclocks

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
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
}