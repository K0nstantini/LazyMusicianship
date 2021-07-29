package com.grommade.lazymusicianship.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grommade.lazymusicianship.ui.components.datepiker.datepicker
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.*
import com.grommade.lazymusicianship.ui.components.timepicker.timepicker
import com.grommade.lazymusicianship.util.extentions.minutesToLocalTime
import com.grommade.lazymusicianship.util.extentions.secondsToLocalTime
import com.grommade.lazymusicianship.util.extentions.toMinutes
import com.grommade.lazymusicianship.util.extentions.toSeconds
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
fun MaterialDialog.BuiltDateDialog(callback: (LocalDate) -> Unit) {
    build {
        datepicker(onDateChange = callback)
        SetButtonsOkCancel()
    }
}

@Composable
fun MaterialDialog.BuiltTimeDialog(
    initialTime: Int = 0,
    callback: (Int) -> Unit,
    minutesAndSeconds: Boolean = false,
) {
    val prefillTime = when (minutesAndSeconds) {
        true -> initialTime.secondsToLocalTime()
        false -> initialTime.minutesToLocalTime()
    }
    val changeTime = { time: LocalTime ->
        when (minutesAndSeconds) {
            true -> callback(time.toSeconds())
            false -> callback(time.toMinutes())
        }
    }
    build {
        timepicker(
            initialTime = prefillTime,
            is24HourClock = true,
            minutesAndSeconds = minutesAndSeconds,
        ) { time -> changeTime(time) }
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
fun MaterialDialog.BuiltInputDialog(
    title: String,
    message: String = "",
    prefill: String = "",
    label: String = "",
    hint: String = "",
    isTextValid: (String) -> Boolean = { true },
    callback: (String) -> Unit,
) {
    build {
        SetTitle(title, message)
        Divider(color = Color.Transparent, thickness = 16.dp)
        input(
            label = label,
            hint = hint,
            prefill = prefill,
            isTextValid = isTextValid
        ) {
            callback(it.trim())
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

@Composable
private fun MaterialDialog.SetButtonsOkCancel(
    onClickOK: () -> Unit = {}
) {
    buttons {
        positiveButton("Ok", onClick = onClickOK)
        negativeButton("Cancel")
    }
}