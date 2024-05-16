package com.example.servicesdemo.ui.home

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.servicesdemo.R
import com.example.servicesdemo.base.viewmodel.BaseViewModel
import com.example.servicesdemo.base.views.BaseActivity
import com.example.servicesdemo.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>(
    R.layout.activity_main, BaseViewModel::class.java
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupWithNavController(
            activityBinding.bottomNavigationView,
            findNavController(R.id.nav_host_fragment_container)
        )
    }
}