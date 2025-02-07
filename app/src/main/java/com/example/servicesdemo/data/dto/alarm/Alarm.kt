package com.example.servicesdemo.data.dto.alarm

data class Alarm(
    val id: Int,
    val time: String,
    val message: String,
    val preAlarmAction: Boolean,
    val onAlarmAction: Boolean,
    val postAlarmAction: Boolean
)
