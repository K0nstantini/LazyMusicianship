package com.grommade.lazymusicianship.ui_section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.*

@ExperimentalMaterialApi
@Composable
fun SectionUi(
    close: () -> Unit
) {
    SectionUi(
        viewModel = hiltViewModel(),
        close = close
    )
}

@ExperimentalMaterialApi
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

@ExperimentalMaterialApi
@Composable
fun SectionUi(
    viewState: SectionViewState,
    actioner: (SectionActions) -> Unit
) {
    Scaffold(
        topBar = {
            SectionTopBar(viewState.section.name,
                { actioner(SectionActions.Save) },
                { actioner(SectionActions.Close) }
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
            SectionName(viewState.section.name) { value ->
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
                onClick = { actioner(SectionActions.ChangeNew(viewState.section.firstTime)) },
                onClickSwitch = { actioner(SectionActions.ChangeNew(it)) },
            )
        }
    }
}

@Composable
fun SectionTopBar(
    name: String,
    save: () -> Unit,
    close: () -> Unit
) {
    TopAppBar(
        title = { },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.97f),
        contentColor = MaterialTheme.colors.onSurface,
        navigationIcon = { NavigationCloseIcon(close) },
        actions = { SaveIcon(name.isNotEmpty(), save) }
    )
}

@Composable
fun SectionName(
    name: String,
    changeName: (String) -> Unit
) {
    TextField(
        value = name,
        onValueChange = changeName,
        label = { Text(stringResource(R.string.piece_hint_edit_text_name)) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        isError = name.isEmpty(),
        singleLine = true,
        textStyle = MaterialTheme.typography.h6.copy(
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondaryVariant
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@ExperimentalMaterialApi
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

@ExperimentalMaterialApi
@Preview
@Composable
fun SectionUiPreview() {
    SectionUi(
        viewState = SectionViewState(),
        actioner = {}
    )
}