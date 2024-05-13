package com.example.servicesdemo.base.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.servicesdemo.ui.home.counter.CounterViewModel

class ViewModelFactory(
    private val mApplication: Application
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass) {
        when {
            isAssignableFrom(CounterViewModel::class.java) -> {
                CounterViewModel()
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