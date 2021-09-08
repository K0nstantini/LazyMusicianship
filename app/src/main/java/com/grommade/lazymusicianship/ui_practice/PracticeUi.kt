package com.grommade.lazymusicianship.ui_practice

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.*
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.DeleteIcon
import com.grommade.lazymusicianship.ui.components.FloatingAddActionButton
import com.grommade.lazymusicianship.ui.theme.*
import com.grommade.lazymusicianship.util.extentions.*
import java.time.LocalDate

@Composable
fun PracticeUi(openPracticeDetails: (Long) -> Unit) {
    PracticeUi(
        viewModel = hiltViewModel(),
        openPracticeDetails = openPracticeDetails,
    )
}

@Composable
private fun PracticeUi(
    viewModel: PracticeViewModel,
    openPracticeDetails: (Long) -> Unit,
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PracticeViewState.Empty)

    PracticeUi(viewState) { action ->
        when (action) {
            is PracticeActions.Open -> openPracticeDetails(action.id)
            PracticeActions.AddNew -> openPracticeDetails(0)
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
private fun PracticeUi(
    viewState: PracticeViewState,
    actioner: (PracticeActions) -> Unit
) {

    /* val piecesDialog = remember { MaterialDialog() }.apply {
         BuiltListDialog(
             title = stringResource(R.string.practice_alert_list_pieces),
             list = viewState.allPieces.map { it.name },
             callback = { actioner(PracticeActions.AddNew(viewState.allPieces.getOrElse(it) { Piece() })) }
         )
     }*/

    Scaffold(
        floatingActionButton = { FloatingAddActionButton { actioner(PracticeActions.AddNew) } },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
        ) {
            itemsIndexed(viewState.practices, key = { _, it -> it.practice.id }) { ind, it ->
                val lastDate = viewState.practices.getOrNull(ind - 1)?.practice?.date ?: { LocalDate.MIN }
                if (lastDate != it.practice.date) {
                    DateLine(it.practice.date)
                } else {
                    Divider(color = DarkPurple2.copy(0.5f))
                }

                PracticeItem(
                    practiceDetails = it,
                    selected = it.practice.id == viewState.selected,
                    select = { actioner(PracticeActions.Select(it.practice.id)) },
                    delete = { actioner(PracticeActions.Delete(it.practice)) },
                    open = { actioner(PracticeActions.Open(it.practice.id)) }
                )
            }
        }
    }
}

@Composable
private fun DateLine(date: LocalDate) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        DateDivider()
        DateLineText(date)
        DateDivider()
    }
}

@Composable
private fun RowScope.DateDivider() {
    Divider(Modifier.weight(1f), color = DarkRed.copy(0.3f))
}

@Composable
private fun DateLineText(
    date: LocalDate,
) {
    val text = when {
        date.isToday() -> stringResource(R.string.practice_date_today)
        date.isYesterday() -> stringResource(R.string.practice_date_yesterday)
        else -> date.toStringFormat()
    }

    Text(
        text = text,
        fontSize = 10.sp,
        color = LightPurple,
        modifier = Modifier
            .wrapContentWidth()
            .padding(horizontal = 8.dp)
    )
}

@Composable
private fun PracticeItem(
    practiceDetails: PracticeWithDetails,
    selected: Boolean,
    select: () -> Unit,
    open: () -> Unit,
    delete: () -> Unit
) = with(practiceDetails) {

    val backgroundColor = if (selected) LightPurple1 else Color.Transparent

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .combinedClickable(
                onClick = open,
                onLongClick = select
            )
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 8.dp)) {
            PieceText(pieceWithSections.piece)

            val (s1, s2) = (sectionFrom to sectionTo)
            if (s1 != null && s2 != null) {
                SectionsText(s1, s2, pieceWithSections.sections, selected)
            }

            StateText(stateStudy, practice.tempo, practice.countTimes)
        }

        if (selected) {
            DeleteIcon(delete)
        }

    }
}

@Composable
private fun PieceText(piece: Piece) {
    Text(
        text = piece.name,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = LightPurple2,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun SectionsText(
    sectionFrom: Section,
    sectionTo: Section,
    sections: List<Section>,
    selected: Boolean
) {

    val textSections = when (sectionFrom) {
        sectionTo -> sectionFrom.name
        else -> "${sectionFrom.name} - ${sectionTo.name}"
    }

    val formatTextSections = if (sectionFrom.isChild) "{ $textSections }" else textSections

    Row {
        sections.parent(sectionFrom)?.let { parent ->
            Text(
                text = sections.parents(parent).joinToString(" -> ") { it.name },
                fontSize = 12.sp,
                color = if (selected) LightPurple2 else LightPurple,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(
            text = formatTextSections,
            fontSize = 12.sp,
            color = Color(0xFF3286B5), // fixme
        )
    }
}

@Composable
private fun StateText(
    stateStudy: StateStudy,
    tempo: Int,
    countTimes: Int
) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = stateStudy.name,
            fontSize = 12.sp,
            color = DarkYellow
        )
        with(stateStudy) {
            if (considerTempo || countNumberOfTimes) {
                val textTempo = if (considerTempo) stringResource(R.string.practice_tempo, tempo) else ""
                val textTimes =
                    if (countNumberOfTimes) stringResource(R.string.practice_count_times, countTimes) else ""
                Text(
                    text = "{ $textTempo $textTimes }",
                    fontSize = 12.sp,
                    color = LightPurple2,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PracticeUiPreview() {
    val state1 = StateStudy(name = "Just practice")
    val state2 = StateStudy(name = "In tempo", considerTempo = true, countNumberOfTimes = true)

    val practices = listOf(
        PracticeWithDetails(
            practice = Practice(
                id = 1,
                pieceId = 1,
                sectionIdFrom = 2,
                sectionIdTo = 2,
                date = LocalDate.now(),
                tempo = 120,
                countTimes = 5
            ),
            pieceWithSections = PieceWithSections(
                piece = Piece(id = 1, name = "Knockin' on Heaven's Door"),
                sections = listOf(
                    Section(id = 1, "Intro"),
                    Section(id = 2, "Verse 1"),
                    Section(id = 3, "Chorus 1")
                )
            ),
            stateStudy = state2
        ),

        PracticeWithDetails(
            practice = Practice(
                id = 2,
                pieceId = 2,
                sectionIdFrom = 3,
                sectionIdTo = 4,
                date = LocalDate.now().minusDays(1)
            ),
            pieceWithSections = PieceWithSections(
                piece = Piece(id = 2, name = "Don't Cry"),
                sections = listOf(
                    Section(id = 1, "Verse 1"),
                    Section(id = 2, "Part I", parentId = 1),
                    Section(id = 3, "1", parentId = 2),
                    Section(id = 4, "2", parentId = 2)
                )
            ),
            stateStudy = state1
        ),

        PracticeWithDetails(
            practice = Practice(
                id = 3,
                pieceId = 3,
                sectionIdFrom = 1,
                sectionIdTo = 2,
                date = LocalDate.now().minusDays(3)
            ),
            pieceWithSections = PieceWithSections(
                piece = Piece(id = 3, name = "Rape me"),
                sections = listOf(
                    Section(id = 1, "Verse 1"),
                    Section(id = 2, "Chorus 1"),
                )
            ),
            stateStudy = state1
        ),

        PracticeWithDetails(
            practice = Practice(
                id = 4,
                pieceId = 3,
                sectionIdFrom = 2,
                sectionIdTo = 2,
                date = LocalDate.now()
            ),
            pieceWithSections = PieceWithSections(
                piece = Piece(id = 3, name = "Город, которого нет"),
                sections = listOf(
                    Section(id = 1, "Verse 1"),
                    Section(id = 2, "1", parentId = 1),
                )
            ),
            stateStudy = state1
        ),

        PracticeWithDetails(
            practice = Practice(
                id = 5,
                pieceId = 4,
                date = LocalDate.now()
            ),
            pieceWithSections = PieceWithSections(
                piece = Piece(id = 4, name = "Mutter"),
                sections = listOf(
                    Section(id = 1, "Intro"),
                )
            ),
            stateStudy = state1
        ),

        ).sortedByDescending { it.practice.date }
    LazyMusicianshipTheme {
        PracticeUi(PracticeViewState(practices, selected = 2)) {}
    }
}

