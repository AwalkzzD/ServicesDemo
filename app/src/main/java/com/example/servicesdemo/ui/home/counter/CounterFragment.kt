package com.example.servicesdemo.ui.home.counter

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.servicesdemo.R
import com.example.servicesdemo.base.extensions.isServiceRunning
import com.example.servicesdemo.base.utils.AppPermission
import com.example.servicesdemo.base.utils.isGranted
import com.example.servicesdemo.base.utils.requestPermission
import com.example.servicesdemo.base.views.BaseFragment
import com.example.servicesdemo.databinding.FragmentCounterBinding
import com.example.servicesdemo.services.CounterService

class CounterFragment : BaseFragment<FragmentCounterBinding, CounterViewModel>(
    R.layout.fragment_counter, CounterViewModel::class.java
) {

    private val broadcastReceiver: BroadcastReceiver by lazy {
        fragmentViewModel.broadcastReceiver
    }
    private lateinit var counterServiceIntent: Intent

    override fun setUpView() {
        init()
        registerBroadcastReceiver()
        setUpObservers()
        setUpListeners()
    }

    private fun init() {
        counterServiceIntent = Intent(requireActivity(), CounterService::class.java)

        // check notification permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isGranted(AppPermission.POST_NOTIFICATIONS)) {
                requestPermission(AppPermission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setUpObservers() {
        // view model counter observer
        fragmentViewModel.counterLiveData.observe(viewLifecycleOwner) { count ->
            fragmentBinding.count.text = count.toString()
        }
    }

    private fun setUpListeners() {
        // add time button listener
        fragmentBinding.addTimeBtn.setOnClickListener {
            if (requireActivity().isServiceRunning(CounterService::class.java)) {
                if (fragmentBinding.countdownTimeEt.text.isNotEmpty()) {
                    requireActivity().sendBroadcast(
                        Intent("timer_updated").putExtra(
                            "countdown_time",
                            fragmentBinding.countdownTimeEt.text.toString().toLong()
                        )
                    )
                    fragmentBinding.countdownTimeEt.text.clear()
                } else {
                    showToast(
                        getString(R.string.countdown_time_et_empty_toast_msg), Toast.LENGTH_SHORT
                    )
                }
            } else {
                showToast(getString(R.string.service_not_running_toast_msg), Toast.LENGTH_SHORT)
            }
        }

        // start service button listener
        fragmentBinding.startServiceBtn.setOnClickListener {
            // check if service already running
            if (requireActivity().isServiceRunning(CounterService::class.java)) {
                showToast(getString(R.string.service_already_running_toast_msg), Toast.LENGTH_SHORT)
            } else {
                // start service
                requireActivity().startService(counterServiceIntent)
            }
        }

        // stop service button listener
        fragmentBinding.stopServiceBtn.setOnClickListener {
            requireActivity().stopService(counterServiceIntent)
        }
    }

    private fun registerBroadcastReceiver() {
        // register broadcast receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(
                fragmentViewModel.broadcastReceiver,
                IntentFilter("counter_updated"),
                AppCompatActivity.RECEIVER_EXPORTED
            )
        }
    }

    override fun onDestroy() {
        // unregister broadcast receiver
        requireActivity().unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}