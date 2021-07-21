package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grommade.lazymusicianship.R

@Composable
fun FloatingAddActionButton(callback: () -> Unit) {
    androidx.compose.material.FloatingActionButton(
        onClick = callback,
        modifier = Modifier.padding(bottom = 48.dp)
    ) {
        Icon(Icons.Filled.Add, stringResource(R.string.cd_add_icon))
    }
}