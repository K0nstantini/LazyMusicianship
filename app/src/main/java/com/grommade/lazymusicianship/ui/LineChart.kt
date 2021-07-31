package com.grommade.lazymusicianship.ui

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas

class LineChart(
    private val values: List<Pair<String, Float>>,
    private val textLabelY: String = "",
    private val textLabelX: String = "",
    private val offsetTopLeft: Offset,
    private val offsetBottomRight: Offset,
) {

    private val spaceLabelAndY = 80f
    private val spaceLabelAndX = 100f

    private val spaceValuesAndY = 15f
    private val spaceValuesAndX = 50f

    private val offsetValuesAndY = 7f

    @Composable
    fun Built() {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                axes()
                marksY()
                marksX()
                labelsXY()
                chart()
            }
        )
    }

    private fun DrawScope.axes() {
        drawAxis(endYPoint())
        drawAxis(endXPoint())
    }

    private fun DrawScope.drawAxis(offset: Offset) {
        drawLine(
            start = offset,
            end = zeroPoint(),
            strokeWidth = 2f,
            color = Color.Black
        )
    }

    private fun DrawScope.marksY() {
        val maxValueY = values.maxOf { it.second }
        val marksY = if (maxValueY < 1) listOf(1) else (0..maxValueY.toInt() + 1).toList()

        val offset = Offset(zeroPoint().x - spaceValuesAndY, zeroPoint().y + offsetValuesAndY)
        val intervalY = intervalY()

        marksY.forEach { value ->
            drawText(
                text = value.toString(),
                offset = Offset(
                    x = offset.x,
                    y = offset.y - intervalY * value
                ),
                textAlign = Paint.Align.RIGHT
            )
        }
    }

    private fun DrawScope.marksX() {
        val offset = Offset(zeroPoint().x, zeroPoint().y + spaceValuesAndX)
        val intervalX = intervalX()

        values.forEachIndexed() { ind, value ->
            drawText(
                text = value.first,
                offset = Offset(
                    x = offset.x + intervalX * ind,
                    y = offset.y
                ),
                textAlign = Paint.Align.CENTER
            )
        }
    }

    private fun DrawScope.labelsXY() {
        val offsetY = Offset(offsetTopLeft.x, endYPoint().y + lengthY() / 2)
        rotate(-90F, pivot = offsetY) {
            drawLabelXY(textLabelY, offsetY)
        }

        val offsetX = Offset(zeroPoint().x + lengthX() / 2, size.height - offsetBottomRight.y)
        drawLabelXY(textLabelX, offsetX)
    }

    private fun DrawScope.drawLabelXY(text: String, offset: Offset) {
        drawText(
            text = text,
            offset = offset,
            textSize = 40F,
            typeface = Typeface.DEFAULT_BOLD,
            textAlign = Paint.Align.CENTER
        )
    }

    private fun DrawScope.chart() {
        val intervalY = intervalY()
        val intervalX = intervalX()
        val zeroPoint = zeroPoint()
        val offset = { ind: Int, valueY: Float ->
            Offset(zeroPoint.x + intervalX * ind, zeroPoint.y - intervalY * valueY)
        }
        val points = values.mapIndexed { index, pair -> offset(index, pair.second) }
        points.forEachIndexed { index, point ->
            drawPoint(point)
            points.getOrNull(index + 1)?.let { drawSection(point, it) }
        }
    }

    private fun DrawScope.drawPoint(offset: Offset) {
        drawCircle(
            color = Color.Blue,
            center = offset,
            radius = 10f,
        )
    }

    private fun DrawScope.drawSection(start: Offset, end: Offset) {
        drawLine(
            start = start,
            end = end,
            strokeWidth = 2f,
            color = Color.Blue
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
        drawIntoCanvas {
            it.nativeCanvas.drawText(text, offset.x, offset.y, paint)
        }
    }

    private fun DrawScope.zeroPoint() =
        Offset(offsetTopLeft.x + spaceLabelAndY, size.height - offsetBottomRight.y - spaceLabelAndX)

    private fun DrawScope.endYPoint() = Offset(zeroPoint().x, offsetTopLeft.y)
    private fun DrawScope.endXPoint() = Offset(size.width - offsetBottomRight.x, zeroPoint().y)

    private fun DrawScope.lengthY() = zeroPoint().y - endYPoint().y
    private fun DrawScope.lengthX() = endXPoint().x - zeroPoint().x

    private fun DrawScope.intervalY() = lengthY() / (values.maxOf { it.second }.toInt() + 1)
    private fun DrawScope.intervalX() = lengthX() / (values.count() - 1)
}