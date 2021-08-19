package com.grommade.lazymusicianship.ui.charts.line_chart

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartText
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartTextFormat
import com.grommade.lazymusicianship.ui_statistics.TimeChartMode
import com.grommade.lazymusicianship.util.extentions.*
import java.time.LocalDate

fun transformLineChartText(
    dates: List<LocalDate>,
    intervalX: Float,
    mode: TimeChartMode
) = when (mode) {
    TimeChartMode.BY_DAYS -> datesByDays(dates, intervalX)
    TimeChartMode.BY_WEEKS -> datesByWeeks()
    TimeChartMode.BY_MONTHS -> datesByMonths(dates)
    TimeChartMode.BY_YEARS -> datesByYears()
}

private fun datesByDays(
    dates: List<LocalDate>,
    intervalX: Float
): (LineChartTextFormat, LocalDate, LocalDate?) -> LineChartText {
    val oneDate = dates.count() == 1
    return { format: LineChartTextFormat, date: LocalDate, lastDate: LocalDate? ->
        val onlyFirstDays = showOnlyFirstDays(dates, format.paint, intervalX)

        val sameMonth = lastDate?.sameMonth(date) ?: true
        val sameYear = lastDate?.sameYear(date) ?: true

        val showDay = date.dayOfMonth == 1 || !onlyFirstDays
        val showMonth = showDay && (oneDate || onlyFirstDays || !sameMonth)
        val showYear = showMonth && (oneDate || !sameYear)

        val textDay = if (showDay) date.stringDay() else ""
        val textMonth = if (showMonth) " " + date.stringMonth(true) else ""
        val textYear = if (showYear) " " + date.stringYear() else ""

        val newFormat = if (showMonth && !oneDate) format.copy(color = Color(0x99AAB694)) else format // fixme: color

        LineChartText(textDay, newFormat, textMonth + textYear)
    }
}

private fun datesByWeeks(): (LineChartTextFormat, LocalDate, LocalDate?) -> LineChartText {
    var currentWeek = 1
    return { format: LineChartTextFormat, _: LocalDate, _: LocalDate? ->
        LineChartText("${currentWeek++}", format)
    }
}

private fun datesByMonths(dates: List<LocalDate>): (LineChartTextFormat, LocalDate, LocalDate?) -> LineChartText {
    val oneDate = dates.count() == 1
    return { format: LineChartTextFormat, date: LocalDate, lastDate: LocalDate? ->
        val showYear = lastDate?.sameYear(date) == false || oneDate
        val textMonth = date.stringMonth(true)
        val textYear = if (showYear) " " + date.stringYear() else ""

        val newFormat = if (showYear && !oneDate) format.copy(color = Color(0x99AAB694)) else format // fixme: color
        LineChartText(textMonth, newFormat, textYear)
    }
}

private fun datesByYears(): (LineChartTextFormat, LocalDate, LocalDate?) -> LineChartText {
    return { format: LineChartTextFormat, date: LocalDate, _: LocalDate? ->
        LineChartText(date.year.toString(), format)
    }
}

private fun showOnlyFirstDays(
    dates: List<LocalDate>,
    paint: Paint,
    intervalX: Float
): Boolean {
    if (dates.isEmpty()) {
        return false
    }

    val countMonths = dates.last().diffMonths(dates.first())
    val text = dates.first().stringDay()
    val countDaysOnInterval = intervalX / paint.measureText(text)
    return countMonths > 2 && countDaysOnInterval < 3
}