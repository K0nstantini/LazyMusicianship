package com.grommade.lazymusicianship.domain.use_cases

import android.content.SharedPreferences
import com.grommade.lazymusicianship.domain.InputWorkUseCase
import com.grommade.lazymusicianship.ui_statistics.TimeChartSettings
import com.grommade.lazymusicianship.util.AppPreferences
import javax.inject.Inject

class SaveTimeChartSettings @Inject constructor(
    private val preferences: SharedPreferences
) : InputWorkUseCase<SaveTimeChartSettings.Params>() {

    override suspend fun doWork(params: Params) {
        with(preferences.edit()) {
            putLong(AppPreferences.DATE_START_TIME_CHART, params.filter.dateStart.toEpochDay())
            putLong(AppPreferences.DATE_END_TIME_CHART, params.filter.dateEnd.toEpochDay())
            putInt(AppPreferences.TIME_MODE_TIME_CHART, params.filter.timeMode.ordinal)
            apply()
        }
    }

    data class Params(val filter: TimeChartSettings)
}