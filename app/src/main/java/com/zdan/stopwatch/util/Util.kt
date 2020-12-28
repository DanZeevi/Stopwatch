package com.zdan.stopwatch.util

const val DELIMITER = ":"
const val MS_DELIMITER = "."

class Util

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

    val hoursString = if (hours > 0) {
        "$hours H "
    } else ""
    val minutesString = if (minutes > 0) {
        "$minutes M "
    } else ""
    val secondsString = "$seconds S"

    return hoursString + minutesString + secondsString
}