package com.grommade.lazymusicianship.ui.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

import com.grommade.lazymusicianship.R

@Composable
fun FloatingAddActionButton(callback: () -> Unit) {
    FloatingActionButton(onClick = callback) {
        Icon(Icons.Filled.Add, stringResource(R.string.cd_add_icon))
    }
}