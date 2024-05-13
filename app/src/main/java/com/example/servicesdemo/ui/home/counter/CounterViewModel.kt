package com.example.servicesdemo.ui.home.counter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.servicesdemo.base.viewmodel.BaseViewModel

class CounterViewModel : BaseViewModel() {

    val counterLiveData: MutableLiveData<Int> = MutableLiveData()

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            counterLiveData.postValue(intent?.getIntExtra("counter", 0))
        }
    }

    init {
        counterLiveData.value = 0
    }

}