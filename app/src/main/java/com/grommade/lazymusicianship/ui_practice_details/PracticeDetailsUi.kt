package com.grommade.lazymusicianship.ui_practice_details

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.AppTimer
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.*
import com.grommade.lazymusicianship.ui.AppTimerBox
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.*
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme
import com.grommade.lazymusicianship.util.extentions.toStringFormat
import java.time.LocalDate

@Composable
fun PracticeDetailsUi(
    close: () -> Unit
) {
    StateDetailsUi(
        viewModel = hiltViewModel(),
        close = close
    )
}

@Composable
fun StateDetailsUi(
    viewModel: PracticeDetailsViewModel,
    close: () -> Unit
) {

    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PracticeDetailsViewState.Empty)

    PracticeDetailsUi(viewState) { action ->
        when (action) {
            PracticeDetailsActions.SaveAndClose -> {
                viewModel.save()
                close()
            }
            PracticeDetailsActions.Close -> close()
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun PracticeDetailsUi(
    viewState: PracticeDetailsViewState,
    actioner: (PracticeDetailsActions) -> Unit
) {

    val practice = viewState.practiceItem.practice

    Scaffold(
        topBar = {
            SaveCloseTopBar(
                saveEnabled = viewState.saveEnabled,
                save = { actioner(PracticeDetailsActions.SaveAndClose) },
                close = { actioner(PracticeDetailsActions.Close) }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        Column(
            Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                DateItem(practice.date) { actioner(PracticeDetailsActions.ChangeDate(it)) }
                TimeItem(practice.elapsedTime) { actioner(PracticeDetailsActions.ChangeTime(it)) }
            }

            StateItem(
                state = viewState.practiceItem.stateStudy,
                states = viewState.allStates
            ) { actioner(PracticeDetailsActions.ChangeState(it)) }

            if (viewState.practiceItem.stateStudy.considerTempo) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TempoItem(practice.tempo) { actioner(PracticeDetailsActions.ChangeTempo(it)) }
                    NumberTimesItem(practice.countTimes) { actioner(PracticeDetailsActions.ChangeNumberTimes(it)) }
                }
            }
        }

/*Column {
    PieceItem(
        pieceName = viewState.practiceItem.piece.name,
        pieces = viewState.allPieces,
        changePiece = { piece -> actioner(PracticeDetailsActions.ChangePiece(piece)) }
    )
    SectionsItems(
        practiceItem = viewState.practiceItem,
        sections = viewState.allSections,
        actioner = actioner
    )
    if (viewState.errorSections) {
        Text(
            text = stringResource(R.string.practice_sections_error),
            style = MaterialTheme.typography.caption.copy(color = Color.Red, fontStyle = FontStyle.Italic)
        )
    }

    val successful = viewState.practiceItem.practice.successful
    SetItemSwitch(
        title = stringResource(R.string.practice_title_successful),
        stateSwitch = successful,
        onClick = { actioner(PracticeDetailsActions.ChangeSuccessful(!successful)) },
        onClickSwitch = { actioner(PracticeDetailsActions.ChangeSuccessful(it)) },
    )
}*/
    }
}

@Composable
fun RowScope.DateItem(
    date: LocalDate,
    changeDate: (LocalDate) -> Unit
) {
    SetDateValue(
        title = stringResource(R.string.practice_title_date),
        value = date.toStringFormat(),
        modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp),
        onClick = changeDate
    )
}

@Composable
fun RowScope.TimeItem(
    time: Int,
    changeTime: (Int) -> Unit
) {
    val timer = remember { AppTimer() }
    AppTimerBox(
        modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp),
        value = time,
        timer = timer,
        changeTime = changeTime,
    )
}

@Composable
fun StateItem(
    state: StateStudy,
    states: List<StateStudy>,
    changeState: (StateStudy) -> Unit
) {
    val listDialog = remember { MaterialDialog() }.apply {
        BuiltListDialog(
            title = stringResource(R.string.practice_alert_state_study),
            list = states.map { it.name },
            callback = { index -> changeState(states.getOrElse(index) { StateStudy() }) }
        )
    }

    SetDefaultValue(
        title = stringResource(R.string.practice_alert_state_study),
        value = state.name,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = listDialog::show
    )
}

@Composable
fun RowScope.TempoItem(
    tempo: Int,
    changeTempo: (Int) -> Unit
) {
    AppOutlinedTextField(
        text = tempo.toString(),
        modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp),
        label = stringResource(R.string.practice_title_tempo),
        changeText = {
            if (it.isDigitsOnly()) {
                changeTempo(it.toIntOrNull() ?: 0)
            }
        }
    )
}

@Composable
fun RowScope.NumberTimesItem(
    times: Int,
    changeTimes: (Int) -> Unit
) {
    AppOutlinedTextField(
        text = times.toString(),
        modifier = Modifier
            .weight(1f)
            .padding(start = 8.dp),
        label = stringResource(R.string.practice_title_number_times),
        changeText = {
            if (it.isDigitsOnly()) {
                changeTimes(it.toIntOrNull() ?: 0)
            }
        }
    )
}

@Composable
fun PieceItem(
    pieceName: String,
    pieces: List<Piece>,
    changePiece: (Piece) -> Unit
) {
    val listDialog = remember { MaterialDialog() }.apply {
        BuiltListDialog(
            title = stringResource(R.string.practice_alert_list_pieces),
            list = pieces.map { it.name },
            callback = { index -> changePiece(pieces.getOrElse(index) { Piece() }) }
        )
    }

    SetItemDefault(
        title = stringResource(R.string.practice_title_piece),
        value = pieceName,
        onClick = listDialog::show
    )
}

@Composable
fun SectionsItems(
    practiceItem: PracticeWithPieceAndSections,
    sections: List<Section>,
    actioner: (PracticeDetailsActions) -> Unit
) {
    val enabledSection = !(practiceItem.piece.isNew || sections.isEmpty())
    SectionItem(
        title = stringResource(R.string.practice_title_section_from),
        sectionName = practiceItem.sectionFrom?.name ?: "",
        enabled = enabledSection,
        sections = sections,
        changeSection = { section -> actioner(PracticeDetailsActions.ChangeSectionFrom(section)) }
    )
    SectionItem(
        title = stringResource(R.string.practice_title_section_to),
        sectionName = practiceItem.sectionTo?.name ?: "",
        enabled = enabledSection,
        sections = sections,
        changeSection = { section -> actioner(PracticeDetailsActions.ChangeSectionTo(section)) }
    )
}

@Composable
fun SectionItem(
    title: String,
    sectionName: String,
    enabled: Boolean,
    sections: List<Section>,
    changeSection: (Section) -> Unit
) {
    val listDialog = remember { MaterialDialog() }.apply {
        BuiltListDialog(
            title = stringResource(R.string.practice_alert_list_sections),
            list = sections.map { it.name },
            callback = { index -> changeSection(sections.getOrElse(index) { Section() }) }
        )
    }
    SetItemWithClear(
        title = title,
        value = sectionName,
        enabled = enabled,
        showClear = sectionName.isNotEmpty(),
        onClickClear = { changeSection(Section()) },
        onClick = listDialog::show
    )
}

@Preview
@Composable
fun PracticeDetailsUiPreview() {
    val viewState = PracticeDetailsViewState(
        practiceItem = PracticeWithPieceAndSections(
            practice = Practice(id = 1, pieceId = 1, date = LocalDate.now()),
            piece = Piece(id = 1, name = "I just want you"),
            stateStudy = StateStudy(id = 1, name = "In tempo", considerTempo = true)
        )
    )
    LazyMusicianshipTheme {
        PracticeDetailsUi(
            viewState = viewState,
            actioner = {}
        )
    }

}