package com.grommade.lazymusicianship.ui_section

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.grommade.lazymusicianship.ui.components.NavigationCloseIcon
import com.grommade.lazymusicianship.ui.components.SaveIcon
import com.grommade.lazymusicianship.ui.components.SetItemSwitch

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
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                PieceTextField(
                    text = viewState.section.beat.toString(),
                    label = stringResource(R.string.piece_hint_edit_text_beat)
                ) { value ->
                    actioner(SectionActions.ChangeBeat(value))
                }
                Spacer(modifier = Modifier.padding(24.dp))
                PieceTextField(
                    text = viewState.section.countBars.toString(),
                    label = stringResource(R.string.hint_edit_text_count_bars)
                ) { value ->
                    actioner(SectionActions.ChangeBars(value))
                }
            }
            Divider(Modifier.padding(top = 16.dp))
            SetItemSwitch(
                title = stringResource(R.string.title_is_new),
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

@Composable
fun PieceTextField(
    text: String,
    label: String,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = changeText,
        label = { Text(label) },
        isError = text.isNotEmpty() && !text.isDigitsOnly(),
        singleLine = true,
        modifier = Modifier.width(100.dp)
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