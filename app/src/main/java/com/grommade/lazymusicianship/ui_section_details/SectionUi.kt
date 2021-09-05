package com.grommade.lazymusicianship.ui_section_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.SaveCloseTopBar
import com.grommade.lazymusicianship.ui.components.SetItemDefaultWithInputDialog
import com.grommade.lazymusicianship.ui.components.SetItemSwitch
import com.grommade.lazymusicianship.ui.components.TextFieldName

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

    SectionUi(viewState) { action ->
        when (action) {
            SectionActions.SaveAndClose -> {
                viewModel.save()
                close()
            }
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
                new = viewState.section.isNew,
                saveEnabled = viewState.section.name.isNotEmpty(),
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
            SetItemDefaultWithInputDialog(
                title = stringResource(R.string.section_title_tempo),
                value = viewState.section.tempo.toString(),
                isTextValid = { text -> text.isDigitsOnly() }
            ) { tempo ->
                actioner(SectionActions.ChangeTempo(tempo.toIntOrNull() ?: 0))
            }
            SetItemSwitch(
                title = stringResource(R.string.section_title_new),
                stateSwitch = viewState.section.firstTime,
                onClick = { actioner(SectionActions.ChangeNew(!viewState.section.firstTime)) },
                onClickSwitch = { actioner(SectionActions.ChangeNew(it)) },
            )
            OutlinedTextField(
                value = viewState.section.description,
                label = { Text(stringResource(R.string.section_title_description)) },
                maxLines = 3,
                onValueChange = { actioner(SectionActions.ChangeDescription(it)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun SectionUiPreview() {
    SectionUi(
        viewState = SectionViewState(),
        actioner = {}
    )
}