package com.luisvertiz.nutriscan.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.luisvertiz.nutriscan.util.AppConstants.PACKAGE

fun Context.goToSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts(PACKAGE, packageName, null)
    }
    startActivity(intent)
}
