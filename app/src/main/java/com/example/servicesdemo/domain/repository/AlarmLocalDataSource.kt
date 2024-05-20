package com.example.servicesdemo.domain.repository

import com.example.servicesdemo.data.dto.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmLocalDataSource {
    fun getAllAlarmsLocal(): Flow<List<Alarm>?>
    fun saveAlarmLocal(alarm: Alarm): Long
}