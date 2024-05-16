package com.example.cleanarchitecture.domain.usecase.user

import com.example.cleanarchitecture.domain.repository.AlarmRepository
import com.example.servicesdemo.data.dto.alarm.Alarm

class SaveAlarm(private val alarmRepository: AlarmRepository) {
    fun invoke(alarm: Alarm) {
        alarmRepository.saveAlarm(alarm)
    }
}