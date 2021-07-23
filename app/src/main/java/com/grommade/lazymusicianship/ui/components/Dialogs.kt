package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.ui.components.datepiker.datepicker
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.*
import com.grommade.lazymusicianship.ui.components.timepicker.timepicker
import com.grommade.lazymusicianship.util.MINUTES_IN_HOUR
import com.grommade.lazymusicianship.util.SECONDS_IN_MINUTE
import java.time.LocalDate
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
fun MaterialDialog.BuiltDateDialog(callback: (LocalDate) -> Unit) {
    build {
        datepicker(onDateChange = callback)
        SetButtonsOkCancel()
    }
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

@Composable
fun MaterialDialog.BuiltListDialog(
    title: String,
    message: String = "",
    list: List<String>,
    callback: (Int) -> Unit
) {
    build {
        SetTitle(title, message)
        listItems(list = list) { index, _ ->
            callback(index)
        }
    }
}

@Composable
fun MaterialDialog.BuiltSingleChoiceDialog(
    title: String,
    message: String = "",
    list: List<String>,
    initialSelection: Int? = null,
    callback: (Int) -> Unit
) {
    build {
        SetTitle(title, message)
        listItemsSingleChoice(
            list = list,
            initialSelection = initialSelection
        ) {
            callback(it)
        }
        SetButtonsOkCancel()
    }
}

@Composable
private fun MaterialDialog.SetTitle(
    title: String,
    message: String = "",
) {
    title(title)
    if (message.isNotEmpty()) {
        message(message)
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