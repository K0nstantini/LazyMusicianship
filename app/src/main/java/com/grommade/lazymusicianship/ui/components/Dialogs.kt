package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.buttons
import com.grommade.lazymusicianship.ui.components.timepicker.timepicker
import com.grommade.lazymusicianship.util.MINUTES_IN_HOUR
import com.grommade.lazymusicianship.util.SECONDS_IN_MINUTE
import java.time.LocalTime

@Composable
fun BuiltSimpleOkCancelDialog(
    title: String,
    callback: () -> Unit,
    close: () -> Unit
) {
    AlertDialog(
        onDismissRequest = close,
        title = { Text(title) },
        confirmButton = {
            TextButton(
                onClick = {
                    callback()
                    close()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = close
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun BuiltInputDialog(
    title: String,
    value: String = "",
    label: String = "",
    isTextValid: (String) -> Boolean = { true },
    callback: (String) -> Unit,
    close: () -> Unit
) {
    var text by remember { mutableStateOf(value) }
    val valid = remember(text) { isTextValid(text) }

    AlertDialog(
        onDismissRequest = close,
        title = {
            Text(
                text = title,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp)
                    .height(64.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.Start)
            )
        },
        text = {
            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(label) },
                    isError = !valid,
                    singleLine = true,
                    textStyle = TextStyle(MaterialTheme.colors.onBackground, fontSize = 16.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    callback(text)
                    close()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = close
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MaterialDialog.BuiltMSTimeDialog(initialTime: Int = 0, callback: (Int) -> Unit) {
    build {
        timepicker(
            initialTime = initialTime.minutesToLocalTime(),
            is24HourClock = true
        ) { time ->
            callback(time.toSeconds())
        }
        SetButtonsOkCancel()
    }
}

private fun Int.minutesToLocalTime(): LocalTime =
    LocalTime.of(this / MINUTES_IN_HOUR, this % MINUTES_IN_HOUR)

private fun LocalTime.toSeconds(): Int =
    hour * MINUTES_IN_HOUR + minute * SECONDS_IN_MINUTE + second

@Composable
private fun MaterialDialog.SetButtonsOkCancel(
    onClickOK: () -> Unit = {}
) {
    buttons {
        positiveButton("Ok", onClick = onClickOK)
        negativeButton("Cancel")
    }
}

//@Composable
//fun BuildTimeDialog(
//    initialTime: Int = 0,
//    callback: () -> Unit,
//    close: () -> Unit
//) {
//
//    AlertDialog(
//        onDismissRequest = close,
//        title = { Text("SELECT TIME") },
//        text = { TimePicker() },
//        confirmButton = {
//            TextButton(
//                onClick = {
//                    callback()
//                    close()
//                }
//            ) {
//                Text("Ok")
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = close
//            ) {
//                Text("Cancel")
//            }
//        }
//    )
//}
//
//@Composable
//fun TimePicker() {
//    val currentScreen = remember { mutableStateOf(ClockScreen.Minute) }
//    val selectedTime = remember { mutableStateOf(LocalTime.MIN) }
//    TimePickerImpl(currentScreen, selectedTime)
//}
//
//@Composable
//fun TimePickerImpl(
//    currentScreen: MutableState<ClockScreen>,
//    selectedTime: MutableState<LocalTime>,
//    onBack: (() -> Unit)? = null
//) {
//
//    Column(
//        Modifier.padding(start = 24.dp, end = 24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(Modifier.align(Alignment.Start)) {
//            TimePickerTitle("SELECT TIME", height = 52.dp, onBack)
//        }
//
//        TimeLayout(selectedTime, currentScreen)
//
//        Spacer(modifier = Modifier.height(36.dp))
//        Crossfade(currentScreen) {
//            when (it.value) {
//                ClockScreen.Minute -> ClockMinuteLayout(selectedTime)
//                ClockScreen.Second -> ClockSecondLayout(selectedTime)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//    }
//}
//
//@Composable
//fun TimePickerTitle(text: String, height: Dp, onBack: (() -> Unit)?) {
//    if (onBack != null) {
//        Row(Modifier.height(height), verticalAlignment = Alignment.CenterVertically) {
//            Box(
//                Modifier
//                    .clip(CircleShape)
//                    .clickable(onClick = onBack),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    Icons.Default.ArrowBack,
//                    contentDescription = "Go back to date selection",
//                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
//                    modifier = Modifier
//                )
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Text(
//                text,
//                style = TextStyle(color = MaterialTheme.colors.onBackground)
//            )
//        }
//    } else {
//        Box(Modifier.height(height)) {
//            Text(
//                text,
//                modifier = Modifier.paddingFromBaseline(top = 28.dp),
//                style = TextStyle(color = MaterialTheme.colors.onBackground)
//            )
//        }
//    }
//}
//
//@Composable
//fun TimeLayout(selectedTime: MutableState<LocalTime>, currentScreen: MutableState<ClockScreen>) {
//    val clockMinutes: String = remember(selectedTime) {
//        selectedTime.value.minute.toString().padStart(2, '0')
//    }
//
//    val activeBackgroundColor = MaterialTheme.colors.primary.copy(0.3f)
//    val inactiveBackgroundColor = MaterialTheme.colors.onBackground.copy(0.3f)
//    val activeTextColor = MaterialTheme.colors.onPrimary
//    val inactiveTextColor = MaterialTheme.colors.onBackground
//
//    Row(
//        horizontalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .height(80.dp)
//            .fillMaxWidth()
//    ) {
//        ClockLabel(
//            text = clockMinutes,
//            backgroundColor = if (currentScreen.value.isMinute()) activeBackgroundColor else inactiveBackgroundColor,
//            textColor = if (currentScreen.value.isMinute()) activeTextColor else inactiveTextColor,
//            onClick = { currentScreen.value = ClockScreen.Minute }
//        )
//
//        Box(
//            Modifier
//                .width(24.dp)
//                .fillMaxHeight(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = ":",
//                style = TextStyle(fontSize = 60.sp, color = MaterialTheme.colors.onBackground)
//            )
//        }
//
//        ClockLabel(
//            text = selectedTime.value.second.toString().padStart(2, '0'),
//            backgroundColor = if (currentScreen.value.isSecond()) activeBackgroundColor else inactiveBackgroundColor,
//            textColor = if (currentScreen.value.isSecond()) activeTextColor else inactiveTextColor,
//            onClick = { currentScreen.value = ClockScreen.Second }
//
//        )
//    }
//}
//
//
//@Composable
//private fun ClockMinuteLayout(selectedTime: MutableState<LocalTime>) {
//    ClockLayout(
//        anchorPoints = 60,
//        label = { index -> index.toString().padStart(2, '0') },
//        onAnchorChange = { mins -> selectedTime.value = selectedTime.value.withMinute(mins) },
//        startAnchor = selectedTime.value.minute,
//        isNamedAnchor = { anchor -> anchor % 5 == 0 },
//        isAnchorEnabled = { true }
//    )
//}
//
//@Composable
//private fun ClockSecondLayout(selectedTime: MutableState<LocalTime>) {
//    ClockLayout(
//        anchorPoints = 60,
//        label = { index -> index.toString().padStart(2, '0') },
//        onAnchorChange = { seconds -> selectedTime.value = selectedTime.value.withSecond(seconds) },
//        startAnchor = selectedTime.value.second,
//        isNamedAnchor = { anchor -> anchor % 5 == 0 },
//        isAnchorEnabled = { true }
//    )
//}
//
//@Composable
//fun ClockLabel(
//    text: String,
//    backgroundColor: Color,
//    textColor: Color,
//    onClick: () -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .width(96.dp)
//            .fillMaxHeight(),
//        shape = MaterialTheme.shapes.medium,
//        color = backgroundColor,
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .clickable(onClick = onClick),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = text,
//                style = TextStyle(
//                    fontSize = 50.sp,
//                    color = textColor
//                )
//            )
//        }
//    }
//}
//
///* Offset of the clock line and selected circle */
//private data class SelectedOffset(
//    val lineOffset: Offset = Offset.Zero,
//    val selectedOffset: Offset = Offset.Zero,
//    val selectedRadius: Float = 0.0f
//)
//
//@Composable
//private fun ClockLayout(
//    isNamedAnchor: (Int) -> Boolean = { true },
//    anchorPoints: Int,
//    innerAnchorPoints: Int = 0,
//    label: (Int) -> String,
//    startAnchor: Int,
//    colors: TimePickerColors = TimePickerDefaults.colors(),
//    isAnchorEnabled: (Int) -> Boolean,
//    onAnchorChange: (Int) -> Unit = {},
//    onLift: () -> Unit = {}
//) {
//    val outerRadiusPx = with(LocalDensity.current) { 100.dp.toPx() }
//    val innerRadiusPx = remember(outerRadiusPx) { outerRadiusPx * 0.6f }
//
//    val textSizePx = with(LocalDensity.current) { 18.sp.toPx() }
//    val innerTextSizePx = remember(textSizePx) { textSizePx * 0.8f }
//
//    val selectedRadius = remember(outerRadiusPx) { outerRadiusPx * 0.2f }
//    val innerSelectedRadius = remember(innerRadiusPx) { innerRadiusPx * 0.3f }
//
//    val offset = remember { mutableStateOf(Offset.Zero) }
//    val center = remember { mutableStateOf(Offset.Zero) }
//    val namedAnchor = remember(isNamedAnchor) { mutableStateOf(isNamedAnchor(startAnchor)) }
//    val selectedAnchor = remember { mutableStateOf(startAnchor) }
//
//    val anchors = remember(anchorPoints, innerAnchorPoints) {
//        val anchors = mutableListOf<SelectedOffset>()
//        for (x in 0 until anchorPoints) {
//            val angle = (2 * PI / anchorPoints) * (x - 15)
//            val selectedOuterOffset = outerRadiusPx.getOffset(angle)
//            val lineOuterOffset = (outerRadiusPx - selectedRadius).getOffset(angle)
//
//            anchors.add(
//                SelectedOffset(
//                    lineOuterOffset,
//                    selectedOuterOffset,
//                    selectedRadius
//                )
//            )
//        }
//        for (x in 0 until innerAnchorPoints) {
//            val angle = (2 * PI / innerAnchorPoints) * (x - 15)
//            val selectedOuterOffset = innerRadiusPx.getOffset(angle)
//            val lineOuterOffset = (innerRadiusPx - innerSelectedRadius).getOffset(angle)
//
//            anchors.add(
//                SelectedOffset(
//                    lineOuterOffset,
//                    selectedOuterOffset,
//                    innerSelectedRadius
//                )
//            )
//        }
//        anchors
//    }
//
//    val anchoredOffset = remember(anchors, startAnchor) { mutableStateOf(anchors[startAnchor]) }
//
//    val updateAnchor: (Offset) -> Boolean = remember(anchors, isAnchorEnabled) {
//        { newOffset ->
//            val absDiff = anchors.map {
//                val diff = it.selectedOffset - newOffset + center.value
//                diff.x.pow(2) + diff.y.pow(2)
//            }
//
//            val minAnchor = absDiff.withIndex().minByOrNull { (_, f) -> f }!!.index
//            if (isAnchorEnabled(minAnchor)) {
//                if (anchoredOffset.value.selectedOffset != anchors[minAnchor].selectedOffset) {
//                    onAnchorChange(minAnchor)
//
//                    anchoredOffset.value = anchors[minAnchor]
//                    namedAnchor.value = isNamedAnchor(minAnchor)
//                    selectedAnchor.value = minAnchor
//                }
//                true
//            } else {
//                false
//            }
//        }
//    }
//
//    val dragSuccess = remember { mutableStateOf(false) }
//
//    val dragObserver: suspend PointerInputScope.() -> Unit = {
//        detectDragGestures(
//            onDragStart = { dragSuccess.value = true },
//            onDragCancel = { dragSuccess.value = false },
//            onDragEnd = { if (dragSuccess.value) onLift() },
//        ) { change, _ ->
//            dragSuccess.value = updateAnchor(change.position)
//            change.consumePositionChange()
//        }
//    }
//
//    val tapObserver: suspend PointerInputScope.() -> Unit = {
//        detectTapGestures(
//            onPress = {
//                val anchorsChanged = updateAnchor(it)
//                val success = tryAwaitRelease()
//
//                if ((success || !dragSuccess.value) && anchorsChanged) {
//                    onLift()
//                }
//            }
//        )
//    }
//
//    BoxWithConstraints(
//        Modifier
//            .padding(horizontal = 12.dp)
//            .size(256.dp)
//            .pointerInput(null, dragObserver)
//            .pointerInput(null, tapObserver)
//    ) {
//        SideEffect {
//            center.value =
//                Offset(constraints.maxWidth.toFloat() / 2f, constraints.maxWidth.toFloat() / 2f)
//            offset.value = center.value
//        }
//
//        val inactiveTextColor = colors.textColor(false).value.toArgb()
//        val clockBackgroundColor = colors.backgroundColor(false).value
//        val selectorColor = remember { colors.selectorColor() }
//        val selectorTextColor = remember { colors.selectorTextColor().toArgb() }
//        val clockSurfaceDiameter = with(LocalDensity.current) { 256.dp.toPx() / 2f }
//
//        val enabledAlpha = ContentAlpha.high
//        val disabledAlpha = ContentAlpha.disabled
//
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            drawCircle(clockBackgroundColor, radius = clockSurfaceDiameter, center = center.value)
//            drawCircle(selectorColor, radius = 16f, center = center.value)
//            drawLine(
//                color = selectorColor,
//                start = center.value,
//                end = center.value + anchoredOffset.value.lineOffset,
//                strokeWidth = 10f,
//                alpha = 0.8f
//            )
//
//            drawCircle(
//                selectorColor,
//                center = center.value + anchoredOffset.value.selectedOffset,
//                radius = anchoredOffset.value.selectedRadius,
//                alpha = 0.7f
//            )
//
//            if (!namedAnchor.value) {
//                drawCircle(
//                    Color.White,
//                    center = center.value + anchoredOffset.value.selectedOffset,
//                    radius = 10f,
//                    alpha = 0.8f
//                )
//            }
//
//            drawIntoCanvas { canvas ->
//                fun drawAnchorText(
//                    anchor: Int,
//                    textSize: Float,
//                    radius: Float,
//                    angle: Double,
//                    alpha: Int = 255
//                ) {
//                    val textOuter = label(anchor)
//                    val textColor = if (selectedAnchor.value == anchor) {
//                        selectorTextColor
//                    } else {
//                        inactiveTextColor
//                    }
//
//                    val contentAlpha = if (isAnchorEnabled(anchor)) enabledAlpha else disabledAlpha
//
//                    drawText(
//                        textSize,
//                        textOuter,
//                        center.value,
//                        angle.toFloat(),
//                        canvas,
//                        radius,
//                        alpha = (255f * contentAlpha).roundToInt().coerceAtMost(alpha),
//                        color = textColor
//                    )
//                }
//
//                for (x in 0 until 12) {
//                    val angle = (2 * PI / 12) * (x - 15)
//                    drawAnchorText(x * anchorPoints / 12, textSizePx, outerRadiusPx, angle)
//
//                    if (innerAnchorPoints > 0) {
//                        drawAnchorText(
//                            x * innerAnchorPoints / 12 + anchorPoints,
//                            innerTextSizePx,
//                            innerRadiusPx,
//                            angle,
//                            alpha = (255 * 0.8f).toInt()
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//private fun drawText(
//    textSize: Float,
//    text: String,
//    center: Offset,
//    angle: Float,
//    canvas: Canvas,
//    radius: Float,
//    alpha: Int = 255,
//    color: Int = android.graphics.Color.WHITE
//) {
//    val outerText = Paint()
//    outerText.color = color
//    outerText.textSize = textSize
//    outerText.textAlign = Paint.Align.CENTER
//    outerText.alpha = alpha
//
//    val r = Rect()
//    outerText.getTextBounds(text, 0, text.length, r)
//
//    canvas.nativeCanvas.drawText(
//        text,
//        center.x + (radius * cos(angle)),
//        center.y + (radius * sin(angle)) + (abs(r.height())) / 2,
//        outerText
//    )
//}
//
//fun Float.getOffset(angle: Double): Offset =
//    Offset((this * cos(angle)).toFloat(), (this * sin(angle)).toFloat())
//
//
//enum class ClockScreen {
//    Minute,
//    Second;
//
//    fun isMinute() = this == Minute
//    fun isSecond() = this == Second
//}