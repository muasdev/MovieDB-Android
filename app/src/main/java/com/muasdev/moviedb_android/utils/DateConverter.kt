package com.muasdev.moviedb_android.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateConverter {

    fun convertDate(
        date: String,
        outputFormatPattern: String? = "d MMMM yyyy"
    ): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
            DateTimeFormatter
                .ofPattern(outputFormatPattern)
                .format(localDate)
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat(outputFormatPattern, Locale.getDefault())
            val parsedDate: Date = inputFormat.parse(date)
            outputFormat.format(parsedDate)
        }
    }
}