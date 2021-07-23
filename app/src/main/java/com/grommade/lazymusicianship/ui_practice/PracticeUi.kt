package com.grommade.lazymusicianship.ui_practice

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
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Practice
import com.grommade.lazymusicianship.data.entity.PracticeWithPieceAndSections
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.DeleteIcon
import com.grommade.lazymusicianship.ui.components.FloatingAddActionButton
import java.time.LocalDate

@Composable
fun PracticeUi(openPracticeDetails: (Long) -> Unit) {
    PracticeUi(
        viewModel = hiltViewModel(),
        openPracticeDetails = openPracticeDetails,
    )
}

@Composable
fun PracticeUi(
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
fun PracticeUi(
    viewState: PracticeViewState,
    actioner: (PracticeActions) -> Unit
) {
    Scaffold(
        topBar = { PracticeTopBar() },
        floatingActionButton = { FloatingAddActionButton { actioner(PracticeActions.AddNew) } },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(viewState.practices, key = { practice -> practice.practice.id }) { practiceItem -> // FIXME: change id
                StateItem(
                    practiceItem = practiceItem,
                    selected = practiceItem.practice.id == viewState.selected,
                    modifier = Modifier.fillParentMaxWidth(),
                    select = { actioner(PracticeActions.Select(practiceItem.practice.id)) },
                    delete = { actioner(PracticeActions.Delete(practiceItem.practice)) },
                    open = { actioner(PracticeActions.Open(practiceItem.practice.id)) }
                )
            }
        }
    }
}

@Composable
fun PracticeTopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.practice_title)) },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.97f),
        contentColor = MaterialTheme.colors.onSurface,
    )
}

@Composable
fun StateItem(
    practiceItem: PracticeWithPieceAndSections,
    selected: Boolean,
    modifier: Modifier,
    select: () -> Unit,
    open: () -> Unit,
    delete: () -> Unit
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
                onClick = open,
                onLongClick = select
            )
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                then modifier
    ) {
        Column() {
            Text(
                text = practiceItem.piece.name,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle1,
            )
            Text(
                text = practiceItem.practice.date.toString(),
                style = MaterialTheme.typography.caption,
            )
//            if (practiceItem) {
//                Text(
//                    text = piece.author,
//                    maxLines = 1,
//                    style = MaterialTheme.typography.caption,
//                )
//            }
        }
        if (selected) {
            DeleteIcon(delete)
        }
    }
}

@Preview
@Composable
fun PracticeUiPreview() {
    val practices = listOf(
        PracticeWithPieceAndSections(
            practice = Practice(id = 1, pieceId = 1, date = LocalDate.now()),
            piece = Piece(id = 1, name = "Knockin' on Heaven's Door"),
            sectionFrom = Section(),
            sectionTo = Section(),
        ),
        PracticeWithPieceAndSections(
            practice = Practice(id = 2, pieceId = 2, date = LocalDate.now().minusDays(1)),
            piece = Piece(id = 2, name = "Don't Cry"),
            sectionFrom = Section(),
            sectionTo = Section(),
        ),
    )
    PracticeUi(PracticeViewState(practices)) {}
}

