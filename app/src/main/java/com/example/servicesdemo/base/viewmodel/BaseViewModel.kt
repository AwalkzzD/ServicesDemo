package com.example.servicesdemo.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "BaseViewModel"

open class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData(false)

    fun startLoading() {
        isLoading.value = true
    }

    fun stopLoading() {
        isLoading.value = false
    }
}