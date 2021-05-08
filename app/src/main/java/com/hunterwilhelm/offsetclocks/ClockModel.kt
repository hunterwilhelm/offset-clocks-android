package com.hunterwilhelm.offsetclocks

// Hold information that the UI is using to display the clocks.
data class ClockModel(
    val Name: String,
    var CurrentTime: String,
    val delay: Long,
)