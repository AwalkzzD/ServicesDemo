package com.example.servicesdemo.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "pre_alarm_action") val preAlarmAction: Boolean,
    @ColumnInfo(name = "on_alarm_action") val onAlarmAction: Boolean,
    @ColumnInfo(name = "post_alarm_action") val postAlarmAction: Boolean
)
