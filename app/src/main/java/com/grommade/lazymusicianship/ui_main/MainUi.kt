package com.grommade.lazymusicianship.ui_main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.MoreVertIcon

@Composable
fun MainUi(openStates: () -> Unit) {
    MainUi(
        viewModel = hiltViewModel(),
        openStates = openStates
    )
}

@Composable
fun MainUi(
    viewModel: MainViewModel,
    openStates: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = MainViewState.Empty)

    MainUi(viewState) { action ->
        when (action) {
            MainActions.OpenStates -> openStates()
            MainActions.OpenSettings -> {
            }
        }
    }
}

@Composable
fun MainUi(
    viewState: MainViewState,
    actioner: (MainActions) -> Unit
) {
    Scaffold(
        topBar = { MainTopBar(actioner) },
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Coming soon...")
    }
}

@Composable
fun MainTopBar(actioner: (MainActions) -> Unit) {
    TopAppBar(
        title = {},
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.97f),
        contentColor = MaterialTheme.colors.onSurface,
        actions = {
            MainDropdownMenu(actioner)
        }
    )
}

@Composable
fun MainDropdownMenu(actioner: (MainActions) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box() {
        MoreVertIcon { expanded = true }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {

            DropdownMenuItem(onClick = {
                expanded = false
                actioner(MainActions.OpenStates)
            }) {
                Text(stringResource(R.string.main_menu_states))
            }

            DropdownMenuItem(onClick = {
                expanded = false
                actioner(MainActions.OpenSettings)
            }) {
                Text(stringResource(R.string.main_menu_settings))
            }
        }
    }
}

@Preview
@Composable
fun MainUiPreview() {
    MainUi(MainViewState()) {}
}
