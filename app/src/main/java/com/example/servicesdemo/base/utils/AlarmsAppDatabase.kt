package com.example.servicesdemo.base.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.servicesdemo.data.local.dao.AlarmsDao
import com.example.servicesdemo.data.local.model.AlarmEntity


@Database(
    version = 2, entities = [AlarmEntity::class]
)
abstract class AlarmsAppDatabase : RoomDatabase() {

    abstract fun alarmsDao(): AlarmsDao

    companion object {
        private var INSTANCE: AlarmsAppDatabase? = null
        private val mLock = Any()

        fun getInstance(context: Context): AlarmsAppDatabase = INSTANCE ?: synchronized(mLock) {
            Room.databaseBuilder(
                context.applicationContext, AlarmsAppDatabase::class.java, "test"
            ).allowMainThreadQueries().build().apply { INSTANCE = this }
        }
    }
}