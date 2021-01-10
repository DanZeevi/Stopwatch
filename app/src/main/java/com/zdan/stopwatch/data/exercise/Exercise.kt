package com.zdan.stopwatch.data.exercise

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Exercise(
    @PrimaryKey
    var id: Long = UUID.randomUUID().leastSignificantBits,
    var name: String = "",
    var reps: Int = 1
): RealmObject()
{
    constructor(exercise: Exercise) : this() {
        this.id = exercise.id
        this.name = exercise.name
        this.reps = exercise.reps
    }
}