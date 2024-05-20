package com.example.servicesdemo.domain.repository

import com.example.servicesdemo.data.dto.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAllAlarms(): Flow<List<Alarm>?>
    fun saveAlarm(alarm: Alarm): Long
}