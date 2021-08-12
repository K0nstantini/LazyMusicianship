package com.grommade.lazymusicianship.ui.charts.line_chart

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.grommade.lazymusicianship.util.extentions.diffMonths
import com.grommade.lazymusicianship.util.extentions.sameMonth
import com.grommade.lazymusicianship.util.extentions.stringDay
import com.grommade.lazymusicianship.util.extentions.stringMonth
import java.time.LocalDate

@Composable
fun LineChart.OverTimeLineChart(values: List<Pair<LocalDate, Float>>) {
    val valuesForChart = values.map { it.first.dayOfMonth.toString() to it.second }
    Built(valuesForChart) {
        net()
        marksY { it.toString() + "h" }
        marksXOverTime(values, zeroPoint(), intervalX())
//        chart()
    }
}

private fun DrawScope.marksXOverTime(
    values: List<Pair<LocalDate, Float>>,
    zeroPoint: Offset,
    intervalX: Float
) {
    if (values.isEmpty()) {
        return
    }
    var lastX = 0f
    var lastDisplayedDate: LocalDate? = null
    val firstDate = values.first().first
    val lastDate = values.last().first
    val countMonths = lastDate.diffMonths(firstDate)
    val countDaysOnInterval = getCountDaysOnInterval(values, intervalX)
    val onlyFirstDays = countMonths > 2 && countDaysOnInterval < 3

    val displayDay = { date: LocalDate, x: Float ->
        val fit = lastX + 30f < x
        if (onlyFirstDays) fit && date.dayOfMonth == 1 else fit
    }

    val displayMonth = { date: LocalDate, ind: Int ->
        val oneValue = values.count() == 1
        val sameMonth = lastDisplayedDate?.sameMonth(date) ?: true
        onlyFirstDays || (ind < values.lastIndex && (oneValue || !sameMonth))
    }

    val offset = { ind: Int ->
        Offset(zeroPoint.x + intervalX * ind, zeroPoint.y + 50f)
    }

    values.forEachIndexed() { ind, value ->
        val date = value.first

        if (displayDay(date, offset(ind).x)) {
            lastX = drawText(
                text = date.stringDay(),
                offset = offset(ind),
            )

            if (displayMonth(date, ind)) {
                lastX = drawText(
                    text = " " + date.stringMonth(true),
                    offset = offset(ind).copy(x = lastX),
                    paint = getPaint(
                        textColor = Color(0x66FFFFFF),
                        align = Paint.Align.LEFT
                    ),
                )
            }
            lastDisplayedDate = date
        }
    }
}

private fun List<Pair<LocalDate, Float>>.fitFiltered(
    edgePosition: (LocalDate, Int, Boolean) -> Float,
    showMonth: (LocalDate, LocalDate?, Int) -> Boolean,
    valueX: (Pair<LocalDate, Float>, LocalDate?, Int) -> ValueX,
    filteredValues: List<ValueX> = emptyList(),
    prevPositionX: Float = 0f,
    ind: Int = 0,
): List<ValueX> {


    val currentValue = this.getOrNull(ind) ?: return filteredValues

    val recursiveFilter = { x: Float ->
        fitFiltered(edgePosition, showMonth, valueX, filteredValues, x, ind + 1)
    }

    val lastDate = filteredValues.lastOrNull()?.date
    val hasMonth = showMonth(currentValue.first, lastDate, ind)
    val newPosition = edgePosition(currentValue.first, ind, hasMonth)
    return when {
        prevPositionX < newPosition -> {
            val value = valueX(currentValue, lastDate, ind)
            recursiveFilter(newPosition) + value
        }
        else -> recursiveFilter(prevPositionX)
    }
}

private fun DrawScope.marksXOverTimeNew(
    values: List<Pair<LocalDate, Float>>,
    zeroPoint: Offset,
    intervalX: Float
) {
    if (values.isEmpty()) {
        return
    }

    val paintDay = getPaint()
    val paintMonth = getPaint(textColor = Color(0x66FFFFFF), align = Paint.Align.LEFT)

    val countMonths = values.last().first.diffMonths(values.first().first)
    val countDaysOnInterval = getCountDaysOnInterval(values, intervalX)
    val onlyFirstDays = countMonths > 2 && countDaysOnInterval < 3

    val showMonth = { date: LocalDate, lastDate: LocalDate?, ind: Int ->
        val oneValue = values.count() == 1
        val sameMonth = lastDate?.sameMonth(date) ?: true
        onlyFirstDays || (ind < values.lastIndex && (oneValue || !sameMonth))
    }

    val startPositionDay = { ind: Int ->
        Offset(
            x = zeroPoint.x + intervalX * ind,
            y = zeroPoint.y + 50f
        )
    }

    val endPositionDay = { date: LocalDate, ind: Int ->
        val lengthDay = paintDay.lengthText(date.stringDay())
        startPositionDay(ind).copy(x = startPositionDay(ind).x + lengthDay)
    }

    val endPositionValueX = { date: LocalDate, ind: Int, hasMonth: Boolean ->
        val x = endPositionDay(date, ind).x
        val lengthMonth = when (hasMonth) {
            true -> paintMonth.lengthText(" " + date.stringMonth(true))
            false -> 0f
        }
        val betweenInterval = 30f
        x + lengthMonth + betweenInterval
    }

    val valueX = { pair: Pair<LocalDate, Float>, lastDate: LocalDate?, ind: Int ->
        ValueX(
            pair = pair,
            showMonth = showMonth(pair.first, lastDate, ind),
            positionDay = startPositionDay(ind),
            positionMonth = endPositionDay(pair.first, ind)
        )
    }

    values
        .fitFiltered(endPositionValueX, showMonth, valueX)
        .forEach {
            drawText(
                text = it.date.stringDay(),
                offset = it.positionDay,
                paint = paintDay
            )
            if (it.showMonth) {
                drawText(
                    text = " " + it.date.stringMonth(true),
                    offset = it.positionMonth,
                    paint = paintMonth,
                )
            }
        }
}

private fun Paint.lengthText(text: String): Float {
    return when (textAlign) {
        Paint.Align.LEFT -> measureText(text)
        Paint.Align.CENTER -> measureText(text) / 2
        else -> 0f
    }
}

private fun getCountDaysOnInterval(
    values: List<Pair<LocalDate, Float>>,
    intervalX: Float
): Float {
    val paint = getPaint()
    val text = values.firstOrNull()?.first?.stringDay() ?: ""
    return intervalX / paint.measureText(text)
}

private fun DrawScope.drawText(
    text: String,
    offset: Offset,
    paint: Paint = getPaint(),
): Float {
    drawIntoCanvas {
        it.nativeCanvas.drawText(text, offset.x, offset.y, paint)
    }
    // fixme: del
    return offset.x + when (paint.textAlign) {
        Paint.Align.LEFT -> paint.measureText(text)
        Paint.Align.CENTER -> paint.measureText(text) / 2
        else -> 0f
    }
}

private fun getPaint(
    size: Float = 32f,
    type: Typeface = Typeface.DEFAULT,
    textColor: Color = Color(0xFF5C558D), // FIXME
    align: Paint.Align = Paint.Align.CENTER
): Paint {
    return Paint().apply {
        textAlign = align
        textSize = size
        typeface = type
        color = textColor.toArgb()
    }
}

private data class ValueX(
    private val pair: Pair<LocalDate, Float>,
    val positionDay: Offset,
    val positionMonth: Offset,
    val showMonth: Boolean
) {
    val date = pair.first
    val value = pair.second
}

private data class ValueXDel(
    private val date: LocalDate,
    private val lastDate: LocalDate,
    private val oneValue: Boolean = false,
    private val positionX: Float,
) {
    val hasMonth = oneValue || true
    val dateStr = date.stringDay()
    val monthStr = if (hasMonth) date.stringMonth() else ""
    val edgePosition = positionX + getPaint().measureText(dateStr + monthStr) + 30f
}