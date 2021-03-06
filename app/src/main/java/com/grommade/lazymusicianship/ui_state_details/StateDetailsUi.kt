package com.grommade.lazymusicianship.ui_state_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.SaveCloseTopBar
import com.grommade.lazymusicianship.ui.components.SetItemSwitch
import com.grommade.lazymusicianship.ui.components.TextFieldName

@Composable
fun StateDetailsUi(
    close: () -> Unit
) {
    StateDetailsUi(
        viewModel = hiltViewModel(),
        close = close
    )
}

@Composable
fun StateDetailsUi(
    viewModel: StateDetailsViewModel,
    close: () -> Unit
) {

    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = StateDetailsViewState.Empty)

    StateDetailsUi(viewState) { action ->
        when (action) {
            StateDetailsActions.SaveAndClose -> {
                viewModel.save()
                close()
            }
            StateDetailsActions.Close -> close()
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun StateDetailsUi(
    viewState: StateDetailsViewState,
    actioner: (StateDetailsActions) -> Unit
) {
    Scaffold(
        topBar = {
            SaveCloseTopBar(
                new = viewState.stateStudy.isNew,
                saveEnabled = viewState.stateStudy.name.isNotEmpty(),
                save = { actioner(StateDetailsActions.SaveAndClose) },
                close = { actioner(StateDetailsActions.Close) }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(
                    start = 16.dp,
                    end = 8.dp
                )
        ) {
            TextFieldName(viewState.stateStudy.name) { value ->
                actioner(StateDetailsActions.ChangeName(value))
            }
            Divider(color = Color.Transparent, thickness = 8.dp)
            SetItemSwitch(
                title = stringResource(R.string.state_title_for_piece),
                stateSwitch = viewState.stateStudy.forPiece,
                onClick = { actioner(StateDetailsActions.ChangeForPiece(!viewState.stateStudy.forPiece)) },
                onClickSwitch = { actioner(StateDetailsActions.ChangeForPiece(it)) },
            )
            SetItemSwitch(
                title = stringResource(R.string.state_title_for_section),
                stateSwitch = viewState.stateStudy.forSection,
                onClick = { actioner(StateDetailsActions.ChangeForSection(!viewState.stateStudy.forSection)) },
                onClickSwitch = { actioner(StateDetailsActions.ChangeForSection(it)) },
            )
            SetItemSwitch(
                title = stringResource(R.string.state_title_consider_tempo),
                stateSwitch = viewState.stateStudy.considerTempo,
                onClick = { actioner(StateDetailsActions.ChangeTempo(!viewState.stateStudy.considerTempo)) },
                onClickSwitch = { actioner(StateDetailsActions.ChangeTempo(it)) },
            )
            SetItemSwitch(
                title = stringResource(R.string.state_title_count_number_of_times),
                stateSwitch = viewState.stateStudy.countNumberOfTimes,
                onClick = { actioner(StateDetailsActions.ChangeTimes(!viewState.stateStudy.countNumberOfTimes)) },
                onClickSwitch = { actioner(StateDetailsActions.ChangeTimes(it)) },
            )
            SetItemSwitch(
                title = stringResource(R.string.state_title_completed),
                stateSwitch = viewState.stateStudy.completed,
                onClick = { actioner(StateDetailsActions.ChangeCompleted(!viewState.stateStudy.completed)) },
                onClickSwitch = { actioner(StateDetailsActions.ChangeCompleted(it)) },
            )
        }
    }
}

@Preview
@Composable
fun StateDetailsUiPreview() {
    StateDetailsUi(
        viewState = StateDetailsViewState(),
        actioner = {}
    )
}