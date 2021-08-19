package com.grommade.lazymusicianship.ui_statistics

import android.content.Context
import androidx.annotation.StringRes
import com.grommade.lazymusicianship.R

enum class TimeChartMode(@StringRes private val title: Int) {
    BY_DAYS(R.string.time_chart_mode_by_days),
    BY_WEEKS(R.string.time_chart_mode_by_weeks),
    BY_MONTHS(R.string.time_chart_mode_by_months),
    BY_YEARS(R.string.time_chart_mode_by_years);

    fun getTitle(context: Context) = context.getString(title)
}