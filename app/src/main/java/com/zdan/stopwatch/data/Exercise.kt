package com.zdan.stopwatch.data

import io.realm.RealmObject
import java.util.*

open class Exercise(
    var id: Long = UUID.randomUUID().leastSignificantBits,
    var name: String = "",
    var reps: Int = 1
): RealmObject()