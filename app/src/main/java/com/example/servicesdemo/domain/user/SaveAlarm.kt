package com.example.servicesdemo.domain.user

import com.example.servicesdemo.domain.repository.AlarmRepository
import com.example.servicesdemo.data.dto.alarm.Alarm

class SaveAlarm(private val alarmRepository: AlarmRepository) {
    fun invoke(alarm: Alarm): Long {
        return alarmRepository.saveAlarm(alarm)
    }
}