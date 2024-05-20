package com.example.servicesdemo.ui.home.alarm

import com.example.servicesdemo.base.viewmodel.BaseViewModel
import com.example.servicesdemo.data.dto.alarm.Alarm
import com.example.servicesdemo.domain.user.GetAllAlarms
import com.example.servicesdemo.domain.user.SaveAlarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull

class AlarmViewModel(private val getAllAlarms: GetAllAlarms, private val saveAlarm: SaveAlarm) :
    BaseViewModel() {

    var alarmsListFlow: Flow<List<Alarm>> = emptyFlow()

    fun saveAlarm(alarm: Alarm): Long {
        return saveAlarm.invoke(alarm)
    }

    fun getAllAlarms() {
        alarmsListFlow = getAllAlarms.invoke().filterNotNull()
    }
}