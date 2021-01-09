package com.zdan.stopwatch.data.exercise

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class ExercisesRepository private constructor() {

    val realm: Realm by lazy { Realm.getDefaultInstance() }

    fun addItem(item: Exercise) {
        realm.executeTransactionAsync { realm ->
            realm.insertOrUpdate(item)
        }
    }

    fun deleteItem(item: Exercise) {
        realm.executeTransactionAsync {
            item.deleteFromRealm()
        }
    }

    fun updateItem(item: Exercise) {
        realm.executeTransactionAsync { realm ->
            realm.insertOrUpdate(item)
        }
    }

    fun getAllItems(): RealmResults<Exercise> {
        return realm.where<Exercise>().findAll()
    }

    fun onDestroy() {
        realm.close()
        instance = null
    }

    companion object {
        private var instance: ExercisesRepository? = null

        fun instance(): ExercisesRepository {
            if (instance == null) {
                instance = ExercisesRepository()
            }
            return instance!!
        }
    }

}