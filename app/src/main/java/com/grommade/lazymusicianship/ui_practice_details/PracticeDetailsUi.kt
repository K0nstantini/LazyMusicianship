package com.grommade.lazymusicianship.ui_practice_details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.grommade.lazymusicianship.ui.theme.*
import com.grommade.lazymusicianship.util.extentions.*
import java.time.LocalDate

private const val X_LEVEL_1 = 27f

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
private fun StateDetailsUi(
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
private fun PracticeDetailsUi(
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

        val modifier = Modifier.padding(start = 16.dp, end = 8.dp)

        Column(Modifier.padding(paddingValues)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth()
            ) {
                DateItem(practice.date) { actioner(PracticeDetailsActions.ChangeDate(it)) }
                TimeItem(practice.elapsedTime) { actioner(PracticeDetailsActions.ChangeTime(it)) }
            }

            StateItem(
                state = viewState.practiceItem.stateStudy,
                states = viewState.allStates,
                modifier = modifier,
            ) { actioner(PracticeDetailsActions.ChangeState(it)) }

            if (viewState.practiceItem.stateStudy.considerTempo) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier.fillMaxWidth()
                ) {
                    TempoItem(practice.tempo) { actioner(PracticeDetailsActions.ChangeTempo(it)) }
                    NumberTimesItem(practice.countTimes) { actioner(PracticeDetailsActions.ChangeNumberTimes(it)) }
                }
            }

            PieceItem(
                pieceName = viewState.practiceItem.piece.name,
                pieces = viewState.allPieces,
                sections = viewState.allSections,
                selectedPiece = viewState.selectedPiece,
                selectedSections = viewState.selectedSections,
                modifier = modifier,
                actioner = actioner
            )
        }

/*Column {
    PieceItem(
        pieceName = viewState.practiceItem.piece.name,
        pieces = viewState.allPieces,
        changePiece = { piece -> actioner(PracticeDetailsActions.ChangePiece(piece)) }
    )
}*/
    }
}

@Composable
private fun RowScope.DateItem(
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
private fun RowScope.TimeItem(
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
private fun StateItem(
    state: StateStudy,
    states: List<StateStudy>,
    modifier: Modifier,
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
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = listDialog::show
    )
}

@Composable
private fun RowScope.TempoItem(
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
private fun RowScope.NumberTimesItem(
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
private fun PieceItem(
    pieceName: String,
    pieces: List<Piece>,
    sections: List<Section>,
    selectedPiece: Boolean,
    selectedSections: List<Section>,
    modifier: Modifier,
    actioner: (PracticeDetailsActions) -> Unit
) {

    val widthSections = remember { mutableStateMapOf(0 to X_LEVEL_1) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            IconMusicNote(color = DarkRed)
            PieceText(pieceName, pieces) { actioner(PracticeDetailsActions.ChangePiece(it)) }

        }
        Checkbox(
            checked = selectedPiece,
            onCheckedChange = { actioner(PracticeDetailsActions.SelectPiece) },
            colors = CheckboxDefaults.colors(
                checkedColor = DarkRed,
                uncheckedColor = LightPurple1
            )
        )
    }

    Box(modifier = modifier.size(16.dp)) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawLine(
                    start = Offset(X_LEVEL_1, 0f),
                    end = Offset(X_LEVEL_1, size.height),
                )
            }
        )
    }

    sections.forEach { section ->
        SectionItem(
            section = section,
            sections = sections,
            selected = selectedSections.contains(section),
            widthSections = widthSections,
            modifier = modifier,
        ) { actioner(PracticeDetailsActions.SelectSection(it)) }
    }
}

@Composable
private fun PieceText(
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

    Text(
        text = pieceName,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = DarkRed,
        modifier = Modifier.clickable(onClick = listDialog::show)
    )
}

@Composable
private fun SectionItem(
    section: Section,
    sections: List<Section>,
    selected: Boolean,
    widthSections: SnapshotStateMap<Int, Float>,
    modifier: Modifier,
    selectSection: (Section) -> Unit
) {
    val hasChildren = sections.hasChildren(section)
    val level = section.getLevel(sections)
    val width = (level + 1) * 16 + 16

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) DarkRed.copy(0.3f) else Color.Transparent)
    ) {
        Row(
            verticalAlignment = CenterVertically,
            modifier = modifier
        ) {
            Box(modifier = Modifier.size(width = width.dp, height = 32.dp)) {
                Canvas(
                    modifier = Modifier.fillMaxSize(),
                    onDraw = {
                        if (!widthSections.containsKey(level + 1)) {
                            widthSections[level + 1] = size.width
                        }
                        drawVerticalLines(section, sections, widthSections)
                        drawHorizontalLines(level, hasChildren, widthSections)
                    }
                )
            }

            TextSection(
                section = section,
                highlight = hasChildren || selected
            ) { selectSection(section) }
        }
        Checkbox(
            checked = selected,
            onCheckedChange = { selectSection(section) },
            colors = CheckboxDefaults.colors(
                checkedColor = LightPurple1,
                uncheckedColor = LightPurple1
            ),
            modifier = modifier
        )
    }
}

@Composable
private fun RowScope.TextSection(
    section: Section,
    highlight: Boolean,
    onClick: (Section) -> Unit
) {
    Text(
        text = section.name,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = if (highlight) WhitePurple else LightPurple1,
        modifier = Modifier
            .padding(start = 8.dp)
            .align(CenterVertically)
            .clickable { onClick(section) }
    )
}

private fun DrawScope.drawVerticalLines(
    section: Section,
    sections: List<Section>,
    widthSections: SnapshotStateMap<Int, Float>,
) {
    val yHorizontalLine = size.height / 2

    var cSection: Section? = section
    while (cSection != null) {
        val level = cSection.getLevel(sections)
        val hasNext = sections.hasNext(cSection)
        val firstSection = cSection.order == 1
        val x = widthSections.getOrDefault(level, 0f)
        val yStart = if (firstSection || sections.hasPrev(cSection)) 0f else yHorizontalLine
        val yEnd = if (hasNext) size.height else yHorizontalLine

        if (section == cSection || hasNext) {
            drawLine(
                start = Offset(x, yStart),
                end = Offset(x, yEnd),
            )
        }

        cSection = sections.getParent(cSection)
    }

    if (sections.hasChildren(section)) {
        drawLine(
            start = Offset(size.width, yHorizontalLine + 4),
            end = Offset(size.width, size.height),
        )
    }
}

private fun DrawScope.drawHorizontalLines(
    level: Int,
    hasChildren: Boolean,
    widthSections: SnapshotStateMap<Int, Float>,
) {
    val yHorizontalLine = size.height / 2
    val x = widthSections.getOrDefault(level, 0f)
    drawLine(
        start = Offset(x, yHorizontalLine),
        end = Offset(size.width, yHorizontalLine),
    )

    drawCircle(
        center = Offset(size.width, yHorizontalLine),
        hasChildren = hasChildren
    )
}

private fun DrawScope.drawLine(
    start: Offset,
    end: Offset
) {
    drawLine(
        start = start,
        end = end,
        strokeWidth = 1f,
        color = DarkPurple2
    )
}

private fun DrawScope.drawCircle(
    center: Offset,
    hasChildren: Boolean = false
) {
    drawCircle(
        center = center,
        radius = 5f,
        color = if (hasChildren) DarkRed else DarkPurple2,
    )
}

@Preview
@Composable
fun PracticeDetailsUiPreview() {

    val allSections = listOf(
        Section(id = 1, name = "Intro", order = 1),
        Section(id = 2, name = "Verse 1", order = 2),
        Section(id = 3, name = "Part I", parentId = 2, order = 1),
        Section(id = 4, name = "Part II", parentId = 2, order = 2),
        Section(id = 5, name = "1", parentId = 4, order = 1),
        Section(id = 6, name = "2", parentId = 4, order = 2),
        Section(id = 7, name = "Part III", parentId = 2, order = 3),
//        Section(id = 8, name = "Verse 2", order = 3),
    )

    val viewState = PracticeDetailsViewState(
        practiceItem = PracticeWithPieceAndSections(
            practice = Practice(id = 1, pieceId = 1, date = LocalDate.now()),
            piece = Piece(id = 1, name = "I just want you"),
            stateStudy = StateStudy(id = 1, name = "In tempo", considerTempo = true),
        ),
        allSections = allSections,
        selectedSections = allSections.filter { it.id == 6L }
    )
    LazyMusicianshipTheme {
        PracticeDetailsUi(
            viewState = viewState,
            actioner = {}
        )
    }

}