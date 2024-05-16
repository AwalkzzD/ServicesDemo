package com.example.servicesdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.servicesdemo.data.local.model.AlarmEntity

@Dao
interface AlarmsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAlarm(alarm: AlarmEntity)

    @Query("SELECT * from alarms")
    fun getAllAlarms(): List<AlarmEntity>
}