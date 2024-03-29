package com.gmail.pentominto.us.supernotes.utility

import java.text.SimpleDateFormat
import java.util.*

object DateGetter {

    fun getCurrentDate(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat(
            "M/d/yy",
            Locale.US
        )
        return dateFormatter.format(currentTime)
    }
}
