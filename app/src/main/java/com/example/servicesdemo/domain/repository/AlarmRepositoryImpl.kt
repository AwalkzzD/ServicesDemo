package com.example.cleanarchitecture.domain.repository

import androidx.lifecycle.LiveData
import com.example.servicesdemo.base.extensions.toLiveData
import com.example.servicesdemo.data.dto.alarm.Alarm

class AlarmRepositoryImpl(
    private val localDataSource: AlarmLocalDataSource
) : AlarmRepository {

    override fun getAllAlarms(): LiveData<List<Alarm>> =
        localDataSource.getAllAlarmsLocal().toLiveData()

    override fun saveAlarm(alarm: Alarm) {
        localDataSource.saveAlarmLocal(alarm)
    }
}