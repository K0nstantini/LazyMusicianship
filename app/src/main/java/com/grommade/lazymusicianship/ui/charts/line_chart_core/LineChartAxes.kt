package com.grommade.lazymusicianship.ui.charts.line_chart_core

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class LineChartAxes(
    val offsetBottomLeft: Offset = Offset(OFFSET_LEFT, OFFSET_BOTTOM), // todo: add auto mode
    val offsetTopRight: Offset = Offset(OFFSET_RIGHT, OFFSET_TOP),
    val color: Color = axesColor,
    val net: Boolean = true,
    val thick: Float = THICK,
) {

    companion object {
        const val OFFSET_LEFT = 70f
        const val OFFSET_BOTTOM = 70f
        const val OFFSET_TOP = 70f
        const val OFFSET_RIGHT = 70f
        const val THICK = 2f
        val axesColor = Color(0xFF29224E) // fixme
    }
}