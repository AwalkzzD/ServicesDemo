package com.example.servicesdemo.base.utils

import androidx.databinding.BindingAdapter
import com.example.servicesdemo.R
import com.example.servicesdemo.data.dto.alarm.Alarm
import com.google.android.material.card.MaterialCardView

@BindingAdapter("listItemBackground")
fun MaterialCardView.setListItemBackground(alarm: Alarm) {
    if (alarm.postAlarmAction) {
        backgroundTintList = resources.getColorStateList(R.color.red_tint, null)
    } else if (alarm.onAlarmAction) {
        backgroundTintList = resources.getColorStateList(R.color.yellow_tint, null)
    } else if (alarm.preAlarmAction) {
        backgroundTintList = resources.getColorStateList(R.color.dark_green_tint, null)
    } else {
        backgroundTintList = resources.getColorStateList(R.color.white, null)
    }
}