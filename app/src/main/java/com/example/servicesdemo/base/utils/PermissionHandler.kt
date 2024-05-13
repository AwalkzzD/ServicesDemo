package com.example.servicesdemo.base.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment

private const val TAG = "PermissionHandler"

// Extension functions for permission handling
fun Fragment.isGranted(permission: AppPermission) = run {
    context?.let {
        (PermissionChecker.checkSelfPermission(
            it, permission.permissionName
        ) == PermissionChecker.PERMISSION_GRANTED)
    } ?: false
}

fun Fragment.requestPermission(permission: AppPermission) {
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "requestPermission: Permission Granted")
        } else {
            Log.d(TAG, "requestPermission: Permission Denied")
        }
    }
    requestPermissionLauncher.launch(permission.permissionName)
}

fun Fragment.goToAppDetailsSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context?.packageName, null)
    }
    val appSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "goToAppDetailsSettings: Opened")
            } else {
                Log.d(TAG, "goToAppDetailsSettings: Unable to Open")
            }
        }

    appSettingLauncher.launch(intent)
}


private fun mapPermissionsAndResults(
    permissions: Array<out String>, grantResults: IntArray
): Map<String, Int> = permissions.mapIndexedTo(
    mutableListOf<Pair<String, Int>>()
) { index, permission -> permission to grantResults[index] }.toMap()


sealed class AppPermission(
    val permissionName: String
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    data object POST_NOTIFICATIONS : AppPermission(
        Manifest.permission.POST_NOTIFICATIONS
    )
}