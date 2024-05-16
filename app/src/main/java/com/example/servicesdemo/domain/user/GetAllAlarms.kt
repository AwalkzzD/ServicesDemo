package com.example.cleanarchitecture.domain.usecase.user

import androidx.lifecycle.LiveData
import com.example.cleanarchitecture.domain.repository.AlarmRepository
import com.example.servicesdemo.data.dto.alarm.Alarm

class GetAllAlarms(private val alarmRepository: AlarmRepository) {
    fun invoke(): LiveData<List<Alarm>> =
        alarmRepository.getAllAlarms()
}