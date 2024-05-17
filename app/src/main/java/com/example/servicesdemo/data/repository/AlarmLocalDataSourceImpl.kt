package com.example.servicesdemo.data.repository

import com.example.servicesdemo.base.extensions.toAlarm
import com.example.servicesdemo.base.extensions.toAlarmEntity
import com.example.servicesdemo.data.dto.alarm.Alarm
import com.example.servicesdemo.data.local.dao.AlarmsDao
import com.example.servicesdemo.domain.repository.AlarmLocalDataSource

class AlarmLocalDataSourceImpl(private val alarmsDao: AlarmsDao) : AlarmLocalDataSource {

    override fun getAllAlarmsLocal(): List<Alarm> {
        return alarmsDao.getAllAlarms().map { alarm -> alarm.toAlarm() }
    }

    override fun saveAlarmLocal(alarm: Alarm): Long {
        return alarmsDao.saveAlarm(alarm.toAlarmEntity())
    }
}