package com.grommade.lazymusicianship.ui_practice_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.*
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.*
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
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

    viewModel.navigateToBack.collectAsState(null).value?.let { close() }

    PracticeDetailsUi(viewState) { action ->
        when (action) {
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
    Scaffold(
        topBar = {
            val enabled = with(viewState.practiceItem) { !(piece.isNew || viewState.errorSections || stateStudy.isNew) }
            SaveCloseTopBar(
                saveEnabled = enabled,
                save = { actioner(PracticeDetailsActions.SaveAndClose) },
                close = { actioner(PracticeDetailsActions.Close) }
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
            DateItem(
                date = viewState.practiceItem.practice.date,
                changeDate = { date -> actioner(PracticeDetailsActions.ChangeDate(date)) }
            )
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
            StateStudyItem(
                stateName = viewState.practiceItem.stateStudy.name,
                states = viewState.allStates,
                changeStudy = { state -> actioner(PracticeDetailsActions.ChangeState(state)) }
            )
        }
    }
}

@Composable
fun DateItem(
    date: LocalDate,
    changeDate: (LocalDate) -> Unit
) {
    val dateDialog = remember { MaterialDialog() }.apply {
        BuiltDateDialog(changeDate)
    }
    SetItemDefault(
        title = stringResource(R.string.practice_title_date),
        value = date.toString(),
        onClick = dateDialog::show
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

@Composable
fun StateStudyItem(
    stateName: String,
    states: List<StateStudy>,
    changeStudy: (StateStudy) -> Unit
) {
    val listDialog = remember { MaterialDialog() }.apply {
        BuiltListDialog(
            title = stringResource(R.string.practice_alert_state_study),
            list = states.map { it.name },
            callback = { index -> changeStudy(states.getOrElse(index) { StateStudy() }) }
        )
    }

    SetItemDefault(
        title = stringResource(R.string.practice_title_state_study),
        value = stateName,
        onClick = listDialog::show
    )
}

@Preview
@Composable
fun PracticeDetailsUiPreview() {
    val viewState = PracticeDetailsViewState(
        practiceItem = PracticeWithPieceAndSections(
            Practice(id = 1, pieceId = 1, date = LocalDate.now()),
            piece = Piece(id = 1, name = "I just want you")
        )
    )
    PracticeDetailsUi(
        viewState = viewState,
        actioner = {}
    )
}