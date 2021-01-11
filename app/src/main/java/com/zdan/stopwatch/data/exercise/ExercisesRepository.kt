package com.zdan.stopwatch.data.exercise

import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import timber.log.Timber

class ExercisesRepository private constructor() {

    val realm: Realm by lazy { Realm.getDefaultInstance() }

    fun addItem(item: Exercise) {
        realm.executeTransactionAsync { realm ->
            Timber.d("repo add: $item")
            realm.insertOrUpdate(item)
        }
    }

    fun deleteItem(id: Long) {
        // TODO: find how to use existing realm instance
        val tempRealm = Realm.getDefaultInstance()
        tempRealm.executeTransactionAsync { realm ->
            val item = realm.where(Exercise::class.java).equalTo(Exercise.ID_KEY, id).findFirst()
            item?.deleteFromRealm()
        }
        tempRealm.close()

    }

    fun updateItem(item: Exercise) {
        realm.executeTransactionAsync { realm ->
            Timber.d("repo update: $item")
            realm.insertOrUpdate(item)
        }
    }

    fun getAllItems(): RealmResults<Exercise> {
        return realm.where<Exercise>().findAll()
    }

    fun onDestroy() {
        Timber.d("repo closing realm")
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