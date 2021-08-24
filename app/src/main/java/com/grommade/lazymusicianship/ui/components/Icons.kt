package com.grommade.lazymusicianship.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.theme.DarkGray
import com.grommade.lazymusicianship.ui.theme.DarkRed
import com.grommade.lazymusicianship.ui.theme.DarkYellow
import com.grommade.lazymusicianship.ui.theme.LightPurple1

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
            contentDescription = stringResource(R.string.cd_delete_icon),
            tint = DarkGray
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
            contentDescription = stringResource(R.string.cd_close_icon),
            tint = LightPurple1
        )
    }
}

@Composable
fun NavigationBackIcon(
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.cd_back_icon)
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
            contentDescription = stringResource(R.string.cd_add_icon),
            tint = DarkRed
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
            contentDescription = stringResource(R.string.cd_save_icon),
            tint = DarkYellow
        )
    }
}

@Composable
fun FilterIcon(
    pressed: Boolean = false,
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = if (pressed) Icons.Filled.FilterList else Icons.Outlined.FilterList,
            contentDescription = stringResource(R.string.cd_filter_icon)
        )
    }
}

@Composable
fun IconExpandMore() {
    Icon(
        imageVector = Icons.Default.ExpandMore,
        contentDescription = stringResource(R.string.cd_expand_more_icon)
    )
}

@Composable
fun IconMusicNote(color: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)) {
    Icon(
        imageVector = Icons.Default.MusicNote,
        contentDescription = stringResource(R.string.cd_music_note_icon),
        tint = color
    )
}


@Composable
fun IconChevronLeft(
    opacity: Float = LocalContentAlpha.current,
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = stringResource(R.string.cd_chevron_left_icon),
            tint = LocalContentColor.current.copy(alpha = opacity),
        )
    }
}

@Composable
fun IconChevronRight(
    opacity: Float = LocalContentAlpha.current,
    callback: () -> Unit
) {
    IconButton(onClick = callback) {
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = stringResource(R.string.cd_chevron_right_icon),
            tint = LocalContentColor.current.copy(alpha = opacity),
        )
    }
}