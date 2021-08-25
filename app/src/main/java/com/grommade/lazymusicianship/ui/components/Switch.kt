package com.grommade.lazymusicianship.ui.components

import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grommade.lazymusicianship.ui.theme.DarkRed
import com.grommade.lazymusicianship.ui.theme.LightPurple1
import com.grommade.lazymusicianship.ui.theme.LightPurple2

@Composable
fun AppSwitch(
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit),
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = DarkRed,
            uncheckedThumbColor = LightPurple2,
            uncheckedTrackColor = LightPurple1
        ),
        modifier = modifier
    )
}