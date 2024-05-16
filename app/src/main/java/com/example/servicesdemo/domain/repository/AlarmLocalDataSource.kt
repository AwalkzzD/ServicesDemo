package com.example.cleanarchitecture.domain.repository

import com.example.servicesdemo.data.dto.alarm.Alarm

interface AlarmLocalDataSource {
    fun getAllAlarmsLocal(): List<Alarm>
    fun saveAlarmLocal(alarm: Alarm)
}