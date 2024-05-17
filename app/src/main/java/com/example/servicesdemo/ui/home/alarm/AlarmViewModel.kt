package com.example.servicesdemo.ui.home.alarm

import androidx.lifecycle.MutableLiveData
import com.example.servicesdemo.domain.user.GetAllAlarms
import com.example.servicesdemo.domain.user.SaveAlarm
import com.example.servicesdemo.base.viewmodel.BaseViewModel
import com.example.servicesdemo.data.dto.alarm.Alarm

class AlarmViewModel(private val getAllAlarms: GetAllAlarms, private val saveAlarm: SaveAlarm) :
    BaseViewModel() {

    var alarmsLiveData: MutableLiveData<List<Alarm>> = MutableLiveData()

    fun saveAlarm(alarm: Alarm): Long {
        return saveAlarm.invoke(alarm)
    }

    fun getAllAlarms() {
        alarmsLiveData.postValue((getAllAlarms.invoke().value))
    }
}