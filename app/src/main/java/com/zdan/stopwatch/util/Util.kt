package com.zdan.stopwatch.util

import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.DimenRes

const val DELIMITER = ":"
const val MS_DELIMITER = "."
const val HOURS = "hour"
const val MINUTES = "minute"
const val SECONDS = "seconds"

fun Long.toStopwatchFormat(): String {
    val tenthSecond = this / 100 % 10
    val timeInSeconds = this / 1000
    val seconds = timeInSeconds % 60
    val minutes = timeInSeconds / 60
    val hours = minutes / 60

    var stringBuilder = StringBuilder()
    if (hours > 0) {
        stringBuilder.append(hours)
        stringBuilder.append(DELIMITER)
    }
    stringBuilder.append(formatWithLeadingZero(minutes))
    stringBuilder.append(DELIMITER)
    stringBuilder.append(formatWithLeadingZero(seconds))
    stringBuilder.append(MS_DELIMITER)
    stringBuilder.append(tenthSecond)

    return stringBuilder.toString()
}

private fun formatWithLeadingZero(long: Long) = if (long < 10) {
    "0$long" } else {
    long.toString()
}

fun Long.toTextFormat(): String {
    val timeInSeconds = this / 1000
    val seconds = timeInSeconds % 60
    val minutes = timeInSeconds / 60
    val hours = minutes / 60

    if (this == 60000L) { return "60 S" }

    val hoursString = if (hours > 0) {
        "$hours $HOURS "
    } else ""
    val minutesString = if (minutes > 0) {
        "$minutes $MINUTES "
    } else ""
    val secondsString = if (seconds > 0) {
        "$seconds $SECONDS"
    } else ""

    return hoursString + minutesString + secondsString
}

fun TextView.setTextSizeInSp(@DimenRes dimenRes: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(dimenRes))
}