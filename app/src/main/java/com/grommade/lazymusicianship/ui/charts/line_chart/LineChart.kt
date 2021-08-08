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

class LineChart(
    private val values: List<Pair<String, Float>>,
    labelsX: List<Pair<String, Int>>,
    private val postfixY: String = "",
) {

    /** Определяет расстояние м-у осями и метками */
    private val offsetStartAndY = 35f
    private val offsetBottomAndX = 35f

    private val verticalOffsetLabelsY = 8f
    private val spaceValuesAndX = 50f
    private val spaceValuesLabelAndX = 45f

    private val netColor = Color(0xFF29224E)
    private val labelColor = Color(0xFF5C558D)
    private val chartColor1 = Color(0xFFFA3D75)
    private val chartColor2 = Color(0xFF8354F8)

    private val countValuesX = values.count()

    //    private val maxY = values.maxOfOrNull { it.second } ?: 0f
    private val countValuesY = (values.maxOfOrNull { it.second } ?: 0).toInt() + 1

    @Composable
    fun Built() {
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

    private fun DrawScope.net() {
        val countLinesX = if (countValuesX > 1) countValuesX else 0
        for (ind in 0..countLinesX) {
            val x = zeroPoint().x + ind * intervalX()
            drawAxis(
                start = Offset(x, zeroPoint().y),
                end = Offset(x, endYPoint().y)
            )
        }

        val countLinesY = if (countValuesY > 1) countValuesY else 0
        for (ind in 0..countLinesY) {
            val y = zeroPoint().y - ind * intervalY()
            drawAxis(
                start = Offset(zeroPoint().x, y),
                end = Offset(endXPoint().x, y)
            )
        }
    }

    private fun DrawScope.drawAxis(
        start: Offset,
        end: Offset
    ) {
        drawLine(
            start = start,
            end = end,
            strokeWidth = 2f,
            color = netColor
        )
    }

    private fun DrawScope.marksY() {
        for (ind in 1..countValuesY) {
            drawText(
                text = ind.toString() + postfixY,
                offset = Offset(
                    x = zeroPoint().x - offsetStartAndY,
                    y = zeroPoint().y - intervalY() * ind + verticalOffsetLabelsY
                ),
                textAlign = Paint.Align.RIGHT
            )
        }
    }

    private fun DrawScope.marksX() {
        val offset = Offset(zeroPoint().x, zeroPoint().y + spaceValuesAndX)

        values.forEachIndexed() { ind, value ->
            drawText(
                text = value.first,
                offset = Offset(
                    x = offset.x + intervalX() * ind,
                    y = offset.y
                ),
                textAlign = Paint.Align.CENTER
            )
        }
        labelsX(offset.y + spaceValuesLabelAndX)
    }

    private fun DrawScope.labelsX(y: Float) {
        drawText(
            text = "September",
            offset = Offset(
                x = zeroPoint().x + lengthX() / 2,
                y = y
            ),
            textAlign = Paint.Align.CENTER
        )
    }

    private fun DrawScope.chart() {
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
            points
                .filter { it.y == maxY }
                .forEach { drawPoint(it) }
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
        textSize: Float = 32f,
        typeface: Typeface = Typeface.DEFAULT,
        textAlign: Paint.Align = Paint.Align.LEFT
    ) {
        val paint = Paint()
        paint.textAlign = textAlign
        paint.textSize = textSize
        paint.typeface = typeface
        paint.color = labelColor.toArgb()
        drawIntoCanvas {
            it.nativeCanvas.drawText(text, offset.x, offset.y, paint)
        }
    }

    private fun DrawScope.zeroPoint() = Offset(0f + offsetStartAndY, size.height - offsetBottomAndX)

    private fun DrawScope.endYPoint() = Offset(zeroPoint().x, 0f)
    private fun DrawScope.endXPoint() = Offset(size.width, zeroPoint().y)

    private fun DrawScope.lengthY() = zeroPoint().y - endYPoint().y
    private fun DrawScope.lengthX() = endXPoint().x - zeroPoint().x


    private fun DrawScope.intervalY() = when (countValuesY) {
        0 -> lengthY()
        else -> lengthY() / countValuesY
    }

    private fun DrawScope.intervalX() = when (countValuesX) {
        1 -> lengthX()
        else -> lengthX() / (countValuesX - 1)
    }

}