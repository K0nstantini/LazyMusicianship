package com.grommade.lazymusicianship.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.grommade.lazymusicianship.R

@Composable
fun MoreVertIcon(
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.cd_more_vert_icon)
        )
    }
}

@Composable
fun DeleteIcon(
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.cd_delete_icon)
        )
    }
}

@Composable
fun SettingsIcon(
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.cd_settings_icon)
        )
    }
}

@Composable
fun NavigationCloseIcon(
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.cd_close_icon)
        )
    }
}

@Composable
fun AddIcon(
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.cd_close_icon),
            tint = MaterialTheme.colors.secondaryVariant
        )
    }
}

@Composable
fun SaveIcon(
    enabled: Boolean = true,
    callback: () -> Unit
) {
    IconButton(
        onClick = callback,
        enabled = enabled
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = stringResource(R.string.cd_save_icon)
        )
    }
}