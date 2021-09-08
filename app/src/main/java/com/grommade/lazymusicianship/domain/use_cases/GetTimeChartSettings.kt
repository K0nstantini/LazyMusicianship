package com.grommade.lazymusicianship.domain.use_cases

import android.content.SharedPreferences
import com.grommade.lazymusicianship.domain.ResultUserCase
import com.grommade.lazymusicianship.ui_statistics.TimeChartMode
import com.grommade.lazymusicianship.ui_statistics.TimeChartSettings
import com.grommade.lazymusicianship.util.AppPreferences
import java.time.LocalDate
import javax.inject.Inject

class GetTimeChartSettings @Inject constructor(
    private val preferences: SharedPreferences
) : ResultUserCase<Unit, TimeChartSettings>() {

    override suspend fun doWork(params: Unit): TimeChartSettings {
        val dateStart = when (val milli = preferences.getLong(AppPreferences.DATE_START_TIME_CHART, 0L)) {
            0L -> LocalDate.MIN
            else -> LocalDate.ofEpochDay(milli)
        }

        val dateEnd = when (val milli = preferences.getLong(AppPreferences.DATE_END_TIME_CHART, 0L)) {
            0L -> LocalDate.MAX
            else -> LocalDate.ofEpochDay(milli)
        }

        val orderTimeMode = preferences.getInt(AppPreferences.TIME_MODE_TIME_CHART, 0)
        val timeMode = TimeChartMode.values().getOrElse(orderTimeMode) { TimeChartMode.BY_DAYS }

        return TimeChartSettings(
            dateStart = dateStart,
            dateEnd = dateEnd,
            timeMode = timeMode
        )
    }

}