package com.example.servicesdemo.ui.home.counter

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.servicesdemo.R
import com.example.servicesdemo.base.extensions.isServiceRunning
import com.example.servicesdemo.base.utils.AppConstants.JS_JOB_ID
import com.example.servicesdemo.base.utils.AppPermission
import com.example.servicesdemo.base.utils.isGranted
import com.example.servicesdemo.base.utils.requestPermission
import com.example.servicesdemo.base.views.BaseFragment
import com.example.servicesdemo.databinding.FragmentCounterBinding
import com.example.servicesdemo.services.foreground.CounterService
import com.example.servicesdemo.services.jobscheduler.NotificationJS
import com.example.servicesdemo.services.workmanager.NotificationWM
import java.util.concurrent.TimeUnit

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

        // schedule job using Work Manager button click listener
        fragmentBinding.scheduleJobWm.setOnClickListener {
            createWorkRequestWM()
        }

        fragmentBinding.scheduleJobJs.setOnClickListener {
            createWorkRequestJS()
        }
    }

    private fun createWorkRequestJS() {
        val componentName = ComponentName(requireContext(), NotificationJS::class.java)
        val jobInfo = JobInfo.Builder(JS_JOB_ID, componentName)
            .build()

        val jobScheduler =
            requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)
    }

    private fun createWorkRequestWM() {
        // one time work request
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWM::class.java)
            .setInputData(Data.Builder().putString("name", "Devarshi").build())
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .setInitialDelay(5, TimeUnit.SECONDS).build()

        WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest)


        /*
        // periodic work request
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            NotificationWM::class.java, 15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(requireContext()).enqueue(periodicWorkRequest)
        */


        /*
        // work chaining
        val chainWork = WorkManager.getInstance(requireContext()).beginUniqueWork(
            AppConstants.CHAIN_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(NotificationWM::class.java)
        ).then(OneTimeWorkRequest.from(LoggingWM::class.java)).enqueue()

        chainWork.state.observe(viewLifecycleOwner) {
            showToast(it.toString(), Toast.LENGTH_SHORT)
        }
        */
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