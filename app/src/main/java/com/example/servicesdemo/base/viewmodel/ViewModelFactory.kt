package com.example.servicesdemo.base.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servicesdemo.domain.repository.AlarmRepositoryImpl
import com.example.servicesdemo.domain.user.GetAllAlarms
import com.example.servicesdemo.domain.user.SaveAlarm
import com.example.servicesdemo.base.utils.AlarmsAppDatabase
import com.example.servicesdemo.data.repository.AlarmLocalDataSourceImpl
import com.example.servicesdemo.ui.home.alarm.AlarmViewModel
import com.example.servicesdemo.ui.home.counter.CounterViewModel

class ViewModelFactory(
    private val mApplication: Application
) : ViewModelProvider.Factory {

    private val alarmsAppDatabase by lazy {
        AlarmsAppDatabase.getInstance(mApplication)
    }

    private val alarmRepository by lazy {
        AlarmRepositoryImpl(
            AlarmLocalDataSourceImpl(alarmsAppDatabase.alarmsDao())
        )
    }

    private val getAllAlarms by lazy {
        GetAllAlarms(alarmRepository)
    }

    private val saveAlarm by lazy {
        SaveAlarm(alarmRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(CounterViewModel::class.java) -> {
                CounterViewModel()
            }

            isAssignableFrom(AlarmViewModel::class.java) -> {
                AlarmViewModel(getAllAlarms, saveAlarm)
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
            }
        }

    } as T

    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                ViewModelFactory(
                    application
                ).apply { INSTANCE = this }
            }
    }
}