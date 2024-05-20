package com.example.servicesdemo.domain.repository

import androidx.lifecycle.LiveData
import com.example.servicesdemo.base.extensions.toLiveData
import com.example.servicesdemo.data.dto.alarm.Alarm
import kotlinx.coroutines.flow.Flow

class AlarmRepositoryImpl(
    private val localDataSource: AlarmLocalDataSource
) : AlarmRepository {

    override fun getAllAlarms(): Flow<List<Alarm>?> =
        localDataSource.getAllAlarmsLocal()

    override fun saveAlarm(alarm: Alarm): Long {
        return localDataSource.saveAlarmLocal(alarm)
    }
}