package com.example.servicesdemo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.servicesdemo.data.local.model.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAlarm(alarm: AlarmEntity): Long

    @Query("SELECT * from alarms")
    fun getAllAlarms(): Flow<List<AlarmEntity>?>

    @Query("UPDATE alarms SET pre_alarm_action = :preAlarmAction WHERE id = :alarmId")
    fun updateAlarmPreAction(alarmId: Int, preAlarmAction: Boolean)

    @Query("UPDATE alarms SET on_alarm_action = :onAlarmAction WHERE id = :alarmId")
    fun updateAlarmOnAction(alarmId: Int, onAlarmAction: Boolean)

    @Query("UPDATE alarms SET post_alarm_action = :postAlarmAction WHERE id = :alarmId")
    fun updateAlarmPostAction(alarmId: Int, postAlarmAction: Boolean)
}