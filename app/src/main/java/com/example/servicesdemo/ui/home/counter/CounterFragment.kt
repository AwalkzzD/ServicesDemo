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

    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var counterServiceIntent: Intent

    override fun setUpView() {
        setUpService()
        registerBroadcastReceiver()
        setUpObservers()
        setUpListeners()
    }

    private fun setUpObservers() {
        fragmentViewModel.counterLiveData.observe(viewLifecycleOwner) { count ->
            fragmentBinding.count.text = count.toString()
        }
    }

    private fun setUpListeners() {
        fragmentBinding.stopServiceBtn.setOnClickListener {
            requireActivity().stopService(counterServiceIntent)
        }
    }

    private fun setUpService() {

        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isGranted(AppPermission.POST_NOTIFICATIONS)) {
                requestPermission(AppPermission.POST_NOTIFICATIONS)
            }
        }

        // create service intent
        counterServiceIntent = Intent(requireActivity(), CounterService::class.java)

        // check service running
        if (requireActivity().isServiceRunning(CounterService::class.java)) {
            showToast("Counter Service Already Running", Toast.LENGTH_SHORT)
        } else {
            // start service
            requireActivity().startService(counterServiceIntent)
        }
    }

    private fun registerBroadcastReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(
                fragmentViewModel.broadcastReceiver,
                IntentFilter("counter_updated"),
                AppCompatActivity.RECEIVER_EXPORTED
            )
        }
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}