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
            initialTime = initialTime.secondsToLocalTime(),
            is24HourClock = true,
            minutesAndSeconds = true,
        ) { time ->
            callback(time.toSeconds())
        }
        SetButtonsOkCancel()
    }
}

private fun Int.secondsToLocalTime(): LocalTime =
    LocalTime.of(0, this / SECONDS_IN_MINUTE, this % SECONDS_IN_MINUTE)

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