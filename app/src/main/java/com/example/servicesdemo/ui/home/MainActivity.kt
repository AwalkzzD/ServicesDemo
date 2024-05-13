package com.example.servicesdemo.ui.home

import com.example.servicesdemo.R
import com.example.servicesdemo.base.viewmodel.BaseViewModel
import com.example.servicesdemo.base.views.BaseActivity
import com.example.servicesdemo.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>(
    R.layout.activity_main,
    BaseViewModel::class.java
)