package com.luisvertiz.nutriscan.util

import com.luisvertiz.nutriscan.util.DateConstants.FORMAT_DD_MM_YYYY
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    fun formatHeaderDate(timestamp: Long): String {
        val simpleDateFormat = SimpleDateFormat(
            FORMAT_DD_MM_YYYY,
            Locale.getDefault()
        )
        return simpleDateFormat.format(Date(timestamp))
    }
}
