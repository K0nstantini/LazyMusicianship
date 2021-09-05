package com.grommade.lazymusicianship.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grommade.lazymusicianship.AppTimer
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.components.BuiltTimeDialog
import com.grommade.lazymusicianship.ui.components.SetTitleWithValue
import com.grommade.lazymusicianship.ui.components.SetValueBox
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.ui.theme.DarkRed
import com.grommade.lazymusicianship.ui.theme.LightPurple1
import com.grommade.lazymusicianship.ui.theme.LightPurple2
import com.grommade.lazymusicianship.util.extentions.milliToMinutes
import com.grommade.lazymusicianship.util.extentions.minutesToMilli
import com.grommade.lazymusicianship.util.extentions.minutesToStrTime
import kotlinx.coroutines.launch

@Composable
fun AppTimerBox(
    modifier: Modifier = Modifier,
    value: Int = 0,
    timer: AppTimer,
    changeTime: (Int) -> Unit,
) {
    val timeDialog = remember { MaterialDialog() }.apply {
        BuiltTimeDialog(value, changeTime)
    }

    val (timerRunning, setTimerRunning) = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val changeMilli: (Long) -> Unit = { milli ->
        changeTime(milli.milliToMinutes())
    }

    val (iconStartStop, colorStartStop, colorRestore) = if (timerRunning) {
        Triple(Icons.Default.PauseCircleFilled, DarkRed, LightPurple1.copy(alpha = 0.3f))
    } else {
        Triple(Icons.Default.PlayCircleFilled, LightPurple2, LightPurple1)
    }

    SetValueBox(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            SetTitleWithValue(
                title = stringResource(R.string.ui_components_timer_title),
                value = value.minutesToStrTime(),
                modifier = if (timerRunning) Modifier else Modifier.clickable(onClick = timeDialog::show)
            )
            IconButton(
                modifier = Modifier.padding(start = 16.dp),
                enabled = !timerRunning,
                onClick = {
                    timer.restore()
                    changeTime(0)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Restore,
                    contentDescription = stringResource(R.string.cd_restore_icon),
                    tint = colorRestore,
                )
            }
            IconButton(onClick = {
                scope.launch {
                    setTimerRunning(!timerRunning)
                    timer.startStop(
                        changeValue = changeMilli,
                        initialValue = value.minutesToMilli()
                    )
                    if (!timerRunning) {
                        changeTime(timer.milli.milliToMinutes(true))
                    }
                }
            }) {
                Icon(
                    imageVector = iconStartStop,
                    contentDescription = stringResource(R.string.cd_play_icon),
                    tint = colorStartStop,
                )
            }
        }
    }
}