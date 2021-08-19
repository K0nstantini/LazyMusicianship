package com.grommade.lazymusicianship.ui.charts.line_chart_core

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

// TODO: Сделать Labels
// TODO: Добавить режим автосмещения Y

//@Suppress("MemberVisibilityCanBePrivate")
open class LineChart<T>(
    private val axes: LineChartAxes = LineChartAxes(),
) {

    private var show: Boolean = false

    private var canvasSize = Size(0f, 0f)
    private var data: List<Pair<T, Float>> = emptyList()
    private var marksListY: List<Float> = emptyList()

    /** ============= Positions =========== */

    private val zeroPoint
        get() = Offset(axes.offsetBottomLeft.x, canvasSize.height - axes.offsetBottomLeft.y)

    private val endYPoint
        get() = Offset(zeroPoint.x, axes.offsetTopRight.y)

    private val endXPoint
        get() = Offset(canvasSize.width - axes.offsetTopRight.x, zeroPoint.y)


    /** ============= Lengths =============== */

    private val lengthY
        get() = zeroPoint.y - endYPoint.y

    private val lengthX
        get() = endXPoint.x - zeroPoint.x


    private val intervalY
        get() = when (val countValuesY = marksListY.count()) {
            1 -> lengthY
            else -> lengthY / (countValuesY - 1)
        }

    val intervalX
        get() = when (val countValuesX = data.count()) {
            1 -> lengthX
            else -> lengthX / (countValuesX - 1)
        }


    /** ============= Functions =============== */

    @Composable
    fun Built(
        values: List<Pair<T, Float>>,
        transformY: (format: LineChartTextFormat, value: Float) -> LineChartText = { format, value ->
            LineChartText(value.toString(), format)
        },
        transformX: (format: LineChartTextFormat, value: T, lastShownValue: T?) -> LineChartText = { format, value, _ ->
            LineChartText(value.toString(), format)
        },
        multiplierY: Float = 1f
    ) {
        data = values
        marksListY = getMarksY(multiplierY)

        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                canvasSize = size
                if (show) {
                    drawMarksY(transformY)
                    drawMarksX(transformX)
                    drawAxes(marksListY)
                    chart()
                }
            }
        )
    }

    fun show() = run { show = true }

    private fun getMarksY(multiplier: Float): List<Float> {
        val maxY = data.maxOfOrNull { it.second } ?: 0f
        val filter = { value: Float ->
            if (value < maxY) value + 1 * multiplier else null
        }

        return generateSequence(0f) { filter(it) }.toList()
    }

    private fun DrawScope.drawMarksY(transform: (LineChartTextFormat, Float) -> LineChartText) {
        var lastY = zeroPoint.y
        val defaultFormat = LineChartTextFormat(
            align = Paint.Align.RIGHT,
            horizontalOffset = -SPACE_MARKS_Y_AND_AXIS,
            verticalOffset = VERTICAL_OFFSET_MARKS_Y
        )
        marksListY.forEachIndexed { ind, value ->
            val (text, format) = transform(defaultFormat, value)

            val x = zeroPoint.x + format.horizontalOffset
            val y = zeroPoint.y - intervalY * ind + format.verticalOffset

            if (!format.hideIfOverlap || y < lastY) { // fixme: also check end screen as in X
                drawIntoCanvas {
                    it.nativeCanvas.drawText(text, x, y, format.paint)
                }
                lastY = y - format.size - format.minSpaceBetweenMarks
            }
        }
    }

    private fun DrawScope.drawMarksX(transform: (LineChartTextFormat, T, T?) -> LineChartText) {
        var lastX = 0f
        var lastValue: T? = null
        val defaultFormat = LineChartTextFormat(
            align = Paint.Align.CENTER,
            verticalOffset = 40f,
        )
        data.map { it.first }.forEachIndexed() { ind, value ->
            val (text, format, postfix) = transform(defaultFormat, value, lastValue)

            val x = zeroPoint.x + intervalX * ind + format.horizontalOffset
            val y = zeroPoint.y + format.verticalOffset //canvasSize.height - format.verticalOffset

            val offsetTextToLeft = format.paint.offsetTextToLeft(text)
            val offsetTextToRight = format.paint.offsetTextToRight(text)
            val offsetTextWithPostfixToRight = offsetTextToRight + format.paint.measureText(postfix)
            val endScreen = format.hideIfOutOfArea && (x + offsetTextWithPostfixToRight) > size.width
            if (text.isNotEmpty() && (!format.hideIfOverlap || (x > lastX + offsetTextToLeft && !endScreen))) {
                drawIntoCanvas {
                    it.nativeCanvas.drawText(text, x, y, format.paint)
                    if (postfix.isNotEmpty()) {
                        val align = format.align
                        it.nativeCanvas.drawText(
                            postfix, x + offsetTextToRight, y, format.paint.apply { textAlign = Paint.Align.LEFT }
                        )
                        format.paint.textAlign = align
                    }
                }
                lastX = x + offsetTextWithPostfixToRight + format.minSpaceBetweenMarks
                lastValue = value
            }
        }
    }

    private fun DrawScope.drawAxes(marksListY: List<Float>) {
        val countLinesY = if (axes.net) marksListY.lastIndex else 0
        for (ind in 0..countLinesY) {
            val y = zeroPoint.y - intervalY * ind
            drawLine(
                start = zeroPoint.copy(y = y),
                end = endXPoint.copy(y = y),
                strokeWidth = axes.thick,
                color = axes.color
            )
        }

        val countLinesX = if (axes.net) data.lastIndex else 0
        for (ind in 0..countLinesX) {
            val x = zeroPoint.x + intervalX * ind
            drawLine(
                start = zeroPoint.copy(x = x),
                end = endYPoint.copy(x = x),
                strokeWidth = axes.thick,
                color = axes.color
            )
        }
    }

    private fun Paint.offsetTextToRight(text: String) = when (textAlign) {
        Paint.Align.LEFT -> measureText(text)
        Paint.Align.CENTER -> measureText(text) / 2
        else -> 0f
    }

    private fun Paint.offsetTextToLeft(text: String) = when (textAlign) {
        Paint.Align.LEFT -> 0f
        Paint.Align.CENTER -> measureText(text) / 2
        else -> measureText(text)
    }

    private fun DrawScope.chart() {
        val offset = { ind: Int, valueY: Float ->
            Offset(zeroPoint.x + intervalX * ind, zeroPoint.y - intervalY * valueY)
        }
        val points = data.mapIndexed { index, pair -> offset(index, pair.second) }

        when (data.count()) {
            0 -> return
            1 -> drawPoint(points.first())
            else -> drawCurve(points)
        }
    }

    private fun DrawScope.drawCurve(points: List<Offset>) {
        val path = Path()
        path.moveTo(points.first().x, points.first().y)

        for (i in 1 until points.size) {
            val point = points[i]
            val lastPoint = points[i - 1]

            path.cubicTo(
                x1 = (point.x + lastPoint.x) / 2,
                y1 = lastPoint.y,
                x2 = (point.x + lastPoint.x) / 2,
                y2 = point.y,
                x3 = point.x,
                y3 = point.y
            )
        }

        val brush = Brush.verticalGradient(listOf(colorGradient1, colorGradient2))

        drawPath(
            path = path,
            brush = brush,
            style = Stroke(width = 4f)
        )

        val maxY = points.minOf { it.y }
        val highs = points.filter { it.y == maxY }
        if (highs.count() == 1) {
            drawPoint(highs.first())
        }
    }

    private fun DrawScope.drawPoint(offset: Offset) {
        drawCircle(
            color = colorGradient1,
            center = offset,
            style = Stroke(width = 7f),
            radius = 10f,
        )
        drawCircle(
            color = Color.White,
            center = offset,
            radius = 7f,
        )
    }

    companion object {
        const val SPACE_MARKS_Y_AND_AXIS = 15f
        const val VERTICAL_OFFSET_MARKS_Y = 8f
        val colorGradient1 = Color(0xFFFA3D75) // fixme
        val colorGradient2 = Color(0xFF8354F8) // fixme
    }
}


