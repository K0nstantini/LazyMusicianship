package com.grommade.lazymusicianship.ui_states

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.StateStudy
import com.grommade.lazymusicianship.ui.common.ShowSnackBar
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.DeleteIcon
import com.grommade.lazymusicianship.ui.components.FloatingAddActionButton
import com.grommade.lazymusicianship.ui.components.NavigationBackIcon

@Composable
fun StatesUi(
    openState: (Long) -> Unit,
    back: () -> Unit
) {
    StatesUi(
        viewModel = hiltViewModel(),
        openState = openState,
        back = back
    )
}

@Composable
fun StatesUi(
    viewModel: StatesViewModel,
    openState: (Long) -> Unit,
    back: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = StatesViewState.Empty)

    StatesUi(viewState) { action ->
        when (action) {
            is StatesActions.Open -> openState(action.id)
            StatesActions.AddNew -> openState(0)
            StatesActions.Back -> back()
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun StatesUi(
    viewState: StatesViewState,
    actioner: (StatesActions) -> Unit
) = Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        topBar = { StatesTopBar { actioner(StatesActions.Back) } },
        floatingActionButton = { FloatingAddActionButton { actioner(StatesActions.AddNew) } },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(viewState.states, key = { state -> state.id }) { state ->
                StateItem(
                    state = state,
                    selected = state.id == viewState.selected,
                    modifier = Modifier.fillParentMaxWidth(),
                    selectPiece = { actioner(StatesActions.Select(state.id)) },
                    deletePiece = { actioner(StatesActions.Delete(state)) },
                    openPiece = { actioner(StatesActions.Open(state.id)) }
                )
            }
        }
    }

    ShowSnackBar(
        error = viewState.error,
        modifier = Modifier.align(Alignment.BottomCenter),
        onDismiss = { actioner(StatesActions.ClearError) }
    )
}

@Composable
fun StatesTopBar(back: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.states_title)) },
        navigationIcon = { NavigationBackIcon(back) },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.97f),
        contentColor = MaterialTheme.colors.onSurface,
    )
}

@Composable
fun StateItem(
    state: StateStudy,
    selected: Boolean,
    modifier: Modifier,
    selectPiece: () -> Unit,
    openPiece: () -> Unit,
    deletePiece: () -> Unit
) {
    val backgroundColor = when (selected) {
        true -> Color.LightGray
        false -> Color.Transparent
    }

    Divider()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = backgroundColor)
            .combinedClickable(
                onClick = openPiece,
                onLongClick = selectPiece
            )
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                then modifier
    ) {
        Text(
            text = state.name,
            maxLines = 2,
            style = MaterialTheme.typography.subtitle1,
        )
        if (selected) {
            DeleteIcon(deletePiece)
        }
    }
}

@Preview
@Composable
fun StatesUiPreview() {
    val states = listOf(
        StateStudy(id = 1, name = "Анализ"),
        StateStudy(id = 2, name = "Игра в темпе"),
        StateStudy(id = 3, name = "Закончено"),
    )
    StatesUi(StatesViewState(states)) {}
}

