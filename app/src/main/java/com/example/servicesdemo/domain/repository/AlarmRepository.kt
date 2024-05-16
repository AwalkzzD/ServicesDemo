package com.example.cleanarchitecture.domain.repository

import androidx.lifecycle.LiveData
import com.example.servicesdemo.data.dto.alarm.Alarm

interface AlarmRepository {
    fun getAllAlarms(): LiveData<List<Alarm>>
    fun saveAlarm(alarm: Alarm)
}