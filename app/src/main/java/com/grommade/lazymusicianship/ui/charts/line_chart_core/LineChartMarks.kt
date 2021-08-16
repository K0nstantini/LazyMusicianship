package com.grommade.lazymusicianship.ui.charts.line_chart_core

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class LineChartText(
    val text: String,
    val format: LineChartTextFormat,
    val postfix: String = "",
)

data class LineChartTextFormat(
    val size: Float = SIZE,
    val color: Color = textColor,
    val align: Paint.Align = Paint.Align.LEFT,
    val verticalOffset: Float = 0f,
    val horizontalOffset: Float = 0f,
    val hideIfOverlap: Boolean = true,
    val hideIfOutOfArea: Boolean = true,
    val minSpaceBetweenMarks: Float = SIZE, /** work if "hideIfOverlap" = true */
) {

    val paint = Paint().apply {
        textAlign = align
        textSize = size
        color = this@LineChartTextFormat.color.toArgb()
    }

    companion object {
        const val SIZE = 32f
        val textColor = Color(0xFF5C558D) // fixme
    }
}
