package com.grommade.lazymusicianship.ui.charts.line_chart

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

@Suppress("MemberVisibilityCanBePrivate")
open class LineChart(
//    private val _values: List<Pair<String, Float>> = emptyList()
) {

    private var values = emptyList<Pair<String, Float>>()
    private val postfixY: String = ""

    /** Определяет расстояние м-у осями и метками */
    private val offsetStartAndY = 35f
    private val offsetBottomAndX = 35f

    private val verticalOffsetLabelsY = 8f
    private val spaceValuesAndX = 50f
    private val spaceValuesLabelAndX = 50f

    private val minSpaceBetweenValuesX = 5f

    private val netColor = Color(0xFF29224E)
    private val labelColor = Color(0xFF5C558D)
    private val chartColor1 = Color(0xFFFA3D75)
    private val chartColor2 = Color(0xFF8354F8)

    private val countValuesX: Int
        get() = values.count()

    private val countValuesY: Int
        get() = ((values.maxOfOrNull { it.second } ?: 0).toInt() + 1)
            .coerceAtMost(24)

    @Composable
    fun Built(
        _values: List<Pair<String, Float>> = values,
        content: DrawScope.() -> Unit
    ) {
        values = _values
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                content()
            }
        )
    }

    @Composable
    fun BuiltDel(_values: List<Pair<String, Float>> = values) {
        values = _values
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                net()
                marksY()
                marksX()
                chart()
            }
        )
    }

    /*private fun DrawScope.preparedValues(): List<LineChartData> {
        val paint = Paint()
        paint.textSize = 32f
        paint.typeface = Typeface.DEFAULT
        val lengthValues1 = values.sumOf { paint.measureText(it.valueX) * 1.5 }
        if (lengthX() < lengthValues1) {
            val preparedValues1 = values.map { it.copy(valueX = it.valueX1) }
            val lengthValues2 = preparedValues1.sumOf { paint.measureText(it.valueX) * 1.5 }
            return if (lengthX() < lengthValues2) {
                preparedValues1.map { it.copy(valueX = it.valueX2) }
            } else {
                preparedValues1
            }
        }
        return values
    }*/

    fun DrawScope.net(
        countLinesX: Int = if (countValuesX > 1) countValuesX - 1 else 0,
        countLinesY: Int = if (countValuesX > 1) countValuesY else 0,
        lineThick: Float = 2f,
        lineColor: Color = netColor // FIXME: Change
    ) {
        val drawAxis = { start: Offset, end: Offset ->
            drawLine(
                start = start,
                end = end,
                strokeWidth = lineThick,
                color = lineColor
            )
        }

        for (ind in 0..countLinesX) {
            val x = zeroPoint().x + ind * intervalX()
            drawAxis(Offset(x, zeroPoint().y), Offset(x, endYPoint().y))
        }

        for (ind in 0..countLinesY) {
            val y = zeroPoint().y - ind * intervalY()
            drawAxis(Offset(zeroPoint().x, y), Offset(endXPoint().x, y))
        }
    }


    fun DrawScope.marksY(
        textSize: Float = 32f,
        textColor: Color = labelColor, // FIXME: Change
        typeface: Typeface = Typeface.DEFAULT,
        verticalOffset: Float = 8f,
        horizontalOffset: Float = 35f,
        transformValue: (Int) -> String = { it.toString() },
    ) {
        for (ind in 1..countValuesY) {
            drawText(
                text = transformValue(ind),
                offset = Offset(
                    x = zeroPoint().x - horizontalOffset,
                    y = zeroPoint().y - intervalY() * ind + verticalOffset
                ),
                size = textSize,
                textColor = textColor,
                type = typeface,
                align = Paint.Align.RIGHT
            )
        }
    }

    fun DrawScope.marksX(
        textSize: Float = 32f,
        textColor: Color = labelColor, // FIXME: Change
        typeface: Typeface = Typeface.DEFAULT,
        verticalOffset: Float = 50f,
        align: Paint.Align = Paint.Align.CENTER
    ) {
        values.forEachIndexed() { ind, value ->
            drawText(
                text = value.first,
                offset = Offset(
                    x = zeroPoint().x + intervalX() * ind,
                    y = zeroPoint().y + verticalOffset
                ),
                size = textSize,
                textColor = textColor,
                type = typeface,
                align = align
            )
        }

        /*val offset = Offset(zeroPoint().x, zeroPoint().y + spaceValuesAndX)

        val countLabels = values.count { it.first.isNotEmpty() }
        values.forEachIndexed() { ind, value ->
            val valueX = offset.x + intervalX() * ind
            drawText(
                text = value.first,
                offset = offset.copy(x = valueX),
                textAlign = if (values.count() == 1) Paint.Align.LEFT else Paint.Align.CENTER
            )
            if (value.first.isNotEmpty() && countLabels > 1) {
                drawText(
                    text = value.first,
                    offset = Offset(
                        x = offset.x + intervalX() * ind,
                        y = offset.y + spaceValuesLabelAndX
                    )
                )
            }
        }
        if (countLabels == 1) {
            labelsX(
                values.maxOf { it.first },
                offset.y + spaceValuesLabelAndX
            )
        }*/
    }

    private fun DrawScope.labelsX(label: String, y: Float) {
        drawText(
            text = label,
            offset = Offset(
                x = zeroPoint().x + lengthX() / 2,
                y = y
            ),
            align = Paint.Align.CENTER
        )
    }

    fun DrawScope.chart() {
        val offset = { ind: Int, valueY: Float ->
            Offset(zeroPoint().x + intervalX() * ind, zeroPoint().y - intervalY() * valueY)
        }
        val points = values.mapIndexed { index, pair -> offset(index, pair.second) }
        if (points.count() == 1) {
            drawPoint(points.first())
        } else {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
            }

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

            val brush = Brush.verticalGradient(
                listOf(chartColor1, chartColor2),
            )

            drawPath(
                path = path,
                brush = brush,
                style = Stroke(width = 4f)
            )

            val maxY = points.minOf { it.y }
            val countMax = points.filter { it.y == maxY }.count()
            if (countMax == 1) {
                drawPoint(points.first())
            }
        }
    }

    private fun DrawScope.drawPoint(offset: Offset) {
        drawCircle(
            color = chartColor1,
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

    private fun DrawScope.drawText(
        text: String,
        offset: Offset,
        size: Float = 32f,
        type: Typeface = Typeface.DEFAULT,
        textColor: Color = labelColor, // FIXME
        align: Paint.Align = Paint.Align.LEFT
    ): Float {
        val paint = Paint().apply {
            textAlign = align
            textSize = size
            typeface = type
            color = textColor.toArgb()
        }
        drawIntoCanvas {
            it.nativeCanvas.drawText(text, offset.x, offset.y, paint)
        }
        // FIXME: Del
        return offset.x + when (align) {
            Paint.Align.LEFT -> paint.measureText(text)
            Paint.Align.CENTER -> paint.measureText(text) / 2
            Paint.Align.RIGHT -> 0f
        }
    }

    fun DrawScope.zeroPoint() = Offset(0f + offsetStartAndY, size.height - offsetBottomAndX)

    fun DrawScope.endYPoint() = Offset(zeroPoint().x, 0f)
    fun DrawScope.endXPoint() = Offset(size.width, zeroPoint().y)

    fun DrawScope.lengthY() = zeroPoint().y - endYPoint().y
    fun DrawScope.lengthX() = endXPoint().x - zeroPoint().x


    fun DrawScope.intervalY() = when (countValuesY) {
        0 -> lengthY()
        else -> lengthY() / countValuesY
    }

    fun DrawScope.intervalX() = when (countValuesX) {
        1 -> lengthX()
        else -> lengthX() / (countValuesX - 1)
    }

}
