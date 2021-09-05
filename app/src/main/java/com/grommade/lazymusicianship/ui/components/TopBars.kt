package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.theme.DarkRed
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme

@Composable
fun SaveCloseTopBar(
    new: Boolean,
    saveEnabled: Boolean = true,
    save: () -> Unit,
    close: () -> Unit
) {
    TopAppBar(
        title = { },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = { NavigationCloseIcon(close) },
        actions = {
            if (new) {
                Text(
                    text = stringResource(R.string.top_bar_save_close_new),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkRed,
                    modifier = Modifier.align(CenterVertically)
                )
            }
            SaveIcon(saveEnabled, save) }
    )
}

@Preview
@Composable
fun SaveCloseTopBarPreview() {
    LazyMusicianshipTheme {
        SaveCloseTopBar(
            new = true,
            saveEnabled = true,
            {},
            {}
        )
    }
}