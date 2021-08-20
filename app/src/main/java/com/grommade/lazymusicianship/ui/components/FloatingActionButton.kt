package com.grommade.lazymusicianship.ui.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.theme.DarkPurple
import com.grommade.lazymusicianship.ui.theme.DarkRed

@Composable
fun FloatingAddActionButton(callback: () -> Unit) {
    FloatingActionButton(
        backgroundColor = DarkRed,
        onClick = callback
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.cd_add_icon),
            tint = DarkPurple
        )
    }
}