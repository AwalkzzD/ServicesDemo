package com.example.servicesdemo.ui.home.alarm

import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.servicesdemo.R
import com.example.servicesdemo.base.adapter.GenericDataAdapter
import com.example.servicesdemo.base.extensions.toLocalDateTime
import com.example.servicesdemo.base.utils.AlarmUtils
import com.example.servicesdemo.base.utils.AppPermission
import com.example.servicesdemo.base.utils.CommonUtils
import com.example.servicesdemo.base.utils.isGranted
import com.example.servicesdemo.base.utils.requestPermission
import com.example.servicesdemo.base.views.BaseFragment
import com.example.servicesdemo.data.dto.alarm.Alarm
import com.example.servicesdemo.databinding.FragmentAlarmBinding
import java.util.Locale

private const val TAG = "AlarmFragment"

class AlarmFragment : BaseFragment<FragmentAlarmBinding, AlarmViewModel>(
    R.layout.fragment_alarm, AlarmViewModel::class.java
) {

    private lateinit var alarmsAdapter: GenericDataAdapter<Alarm>
    private val alarmsList: MutableList<Alarm> = mutableListOf()

    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

            val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)

            if (CommonUtils.checkCurrentTimePassed(formattedTime.toLocalDateTime())) {
                showToast("Please select a future time!", Toast.LENGTH_SHORT)
            } else {
                showToast("Setting your alarm for $formattedTime", Toast.LENGTH_SHORT)
                val alarm = Alarm(0, formattedTime, "Alarm")
                val alarmId = fragmentViewModel.saveAlarm(alarm).toInt()
                fragmentViewModel.getAllAlarms()
                AlarmUtils.schedule(alarmId, alarm, requireContext())
            }
        }

    override fun setUpView() {
        super.setUpView()

        init()
        setUpListeners()
        setUpRecyclerView()
        setUpViewModel()
    }

    private fun init() {
        // check alarm permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isGranted(AppPermission.USE_EXACT_ALARM)) {
                requestPermission(AppPermission.USE_EXACT_ALARM)
            } else if (!isGranted(AppPermission.SCHEDULE_EXACT_ALARM)) {
                requestPermission(AppPermission.SCHEDULE_EXACT_ALARM)
            }
        }
    }

    private fun setUpListeners() {
        fragmentBinding.addAlarmFab.setOnClickListener {
            showAddAlarmDialog()
        }
    }

    private fun setUpViewModel() {
        fragmentViewModel.getAllAlarms()

        fragmentViewModel.alarmsLiveData.observe(viewLifecycleOwner) { alarms ->
            if (!alarms.isNullOrEmpty()) {
                alarmsList.clear()
                alarmsList.addAll(alarms)
                alarmsAdapter.notifyItemRangeChanged(alarmsList.size, alarmsList.size + alarms.size)
            } else {
                showToast("No alarms set!", Toast.LENGTH_SHORT)
            }
        }
    }

    private fun showAddAlarmDialog() {
        val timePicker = TimePickerDialog(requireContext(), timePickerDialogListener, 12, 10, false)
        timePicker.show()
    }

    private fun setUpRecyclerView() {
        alarmsAdapter = GenericDataAdapter(alarmsList, R.layout.alarm_list_item) { alarm ->
            showToast(alarm.message, Toast.LENGTH_SHORT)
        }

        fragmentBinding.alarmListRv.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = alarmsAdapter
        }
    }
}