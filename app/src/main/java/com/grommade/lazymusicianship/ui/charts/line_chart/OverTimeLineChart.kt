package com.grommade.lazymusicianship.ui.charts.line_chart

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartText
import com.grommade.lazymusicianship.ui.charts.line_chart_core.LineChartTextFormat
import com.grommade.lazymusicianship.util.extentions.*
import java.time.LocalDate

fun transformLineChartText(
    dates: List<LocalDate>,
    intervalX: Float
): (LineChartTextFormat, LocalDate, LocalDate?) -> LineChartText {

    return { format: LineChartTextFormat, date: LocalDate, lastDate: LocalDate? ->
        val oneDate = dates.count() == 1
        val onlyFirstDays = showOnlyFirstDays(dates, format.paint, intervalX)

        val sameMonth = lastDate?.sameMonth(date) ?: true
        val sameYear = lastDate?.sameYear(date) ?: true

        val showDay = date.dayOfMonth == 1 || !onlyFirstDays
        val showMonth = showDay && (oneDate || onlyFirstDays || !sameMonth)
        val showYear = showMonth && (oneDate || !sameYear)

        val textDay = if (showDay) date.stringDay() else ""
        val textMonth = if (showMonth) " " + date.stringMonth(true) else ""
        val textYear = if (showYear) " " + date.stringYear() else ""

        val newFormat = if (showMonth) format.copy(color = Color(0x99AAB694)) else format // fixme: color

        LineChartText(textDay, newFormat, textMonth + textYear)
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