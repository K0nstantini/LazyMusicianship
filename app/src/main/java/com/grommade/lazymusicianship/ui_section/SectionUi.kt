package com.grommade.lazymusicianship.ui_section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.TextFieldName
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.BuiltInputDialog
import com.grommade.lazymusicianship.ui.components.SaveCloseTopBar
import com.grommade.lazymusicianship.ui.components.SetItemDefault
import com.grommade.lazymusicianship.ui.components.SetItemSwitch

@Composable
fun SectionUi(
    close: () -> Unit
) {
    SectionUi(
        viewModel = hiltViewModel(),
        close = close
    )
}

@Composable
fun SectionUi(
    viewModel: SectionViewModel,
    close: () -> Unit
) {

    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = SectionViewState.Empty)

    viewModel.navigateToBack.collectAsState(null).value?.let { close() }

    SectionUi(viewState) { action ->
        when (action) {
            SectionActions.Close -> close()
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun SectionUi(
    viewState: SectionViewState,
    actioner: (SectionActions) -> Unit
) {
    Scaffold(
        topBar = {
            SaveCloseTopBar(
                saveEnabled = !viewState.section.isNew,
                save = { actioner(SectionActions.SaveAndClose) },
                close = { actioner(SectionActions.Close) }
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
            TextFieldName(viewState.section.name) { value ->
                actioner(SectionActions.ChangeName(value))
            }
            TempoItem(
                title = stringResource(R.string.section_title_tempo),
                value = viewState.section.tempo.toString(),
                isTextValid = { text: String -> text.isDigitsOnly() },
                changeInfo = { value: String -> actioner(SectionActions.ChangeTempo(value)) }
            )
            SetItemSwitch(
                title = stringResource(R.string.section_title_new),
                stateSwitch = viewState.section.firstTime,
                onClick = { actioner(SectionActions.ChangeNew(!viewState.section.firstTime)) },
                onClickSwitch = { actioner(SectionActions.ChangeNew(it)) },
            )
        }
    }
}

@Composable
fun TempoItem(
    title: String,
    value: String,
    isTextValid: (String) -> Boolean = { true },
    changeInfo: (String) -> Unit
) {
    val alertDialog = remember { mutableStateOf(false) }
    if (alertDialog.value) {
        BuiltInputDialog(
            title = title,
            value = value,
            isTextValid = isTextValid,
            callback = changeInfo,
            close = { alertDialog.value = false }
        )
    }

    SetItemDefault(
        title = title,
        value = value,
        onClick = { alertDialog.value = true }
    )
}

@Preview
@Composable
fun SectionUiPreview() {
    SectionUi(
        viewState = SectionViewState(),
        actioner = {}
    )
}