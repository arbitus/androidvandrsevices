package com.example.vandrservices.ui.form.lot

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class Utils {


    companion object {
        fun getCurrentWeekOfYear(): Int {
            val localCalendar = Calendar.getInstance()
            return localCalendar.get(Calendar.WEEK_OF_YEAR)
        }

        fun getCurrentFormattedDate(): String {
            val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            formattedDate.timeZone = TimeZone.getDefault()
            val localCalendar = Calendar.getInstance()
            return formattedDate.format(localCalendar.time)
        }

        fun formatDate(millis: Long): String {
            val formattedDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            formattedDate.timeZone = TimeZone.getDefault()
            val localCalendar = Calendar.getInstance()
            localCalendar.timeInMillis = millis
            return formattedDate.format(localCalendar.time)
        }


        fun formatDateToApi(date: String): String {
            val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            return outputFormat.format(parsedDate!!)
        }
    }
}
