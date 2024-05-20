package com.example.servicesdemo.domain.user

import androidx.lifecycle.LiveData
import com.example.servicesdemo.domain.repository.AlarmRepository
import com.example.servicesdemo.data.dto.alarm.Alarm
import kotlinx.coroutines.flow.Flow

class GetAllAlarms(private val alarmRepository: AlarmRepository) {
    fun invoke(): Flow<List<Alarm>?> =
        alarmRepository.getAllAlarms()
}