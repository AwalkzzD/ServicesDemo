package com.example.servicesdemo.domain.user

import androidx.lifecycle.LiveData
import com.example.servicesdemo.domain.repository.AlarmRepository
import com.example.servicesdemo.data.dto.alarm.Alarm

class GetAllAlarms(private val alarmRepository: AlarmRepository) {
    fun invoke(): LiveData<List<Alarm>> =
        alarmRepository.getAllAlarms()
}