package com.grommade.lazymusicianship.ui_piece

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.AddIcon
import com.grommade.lazymusicianship.ui.components.NavigationCloseIcon
import com.grommade.lazymusicianship.ui.components.SaveIcon
import com.grommade.lazymusicianship.util.Keys

@Composable
fun PieceUi(
    navController: NavController,
    openSection: (Long) -> Unit,
    close: () -> Unit,
) {
    PieceUi(
        viewModel = hiltViewModel(),
        navController = navController,
        openSection = openSection,
        close = close
    )
}

@Composable
fun PieceUi(
    viewModel: PieceViewModel,
    navController: NavController,
    openSection: (Long) -> Unit,
    close: () -> Unit
) {

    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PieceViewState.Empty)

    val handle = navController.currentBackStackEntry?.savedStateHandle
    handle?.get<Section>(Keys.SECTION)?.let {
        viewModel.refreshSections(it)
        handle.remove<Section>(Keys.SECTION)
    }

    viewModel.navigateToSection.collectAsState(null).value?.let { openSection(viewState.piece.id) }
    viewModel.navigateToBack.collectAsState(null).value?.let { close() }



    PieceUi(viewState) { action ->
        when (action) {
            is PieceActions.NewSection -> openSection(viewState.piece.id)
            PieceActions.Close -> close()
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun PieceUi(
    viewState: PieceViewState,
    actioner: (PieceActions) -> Unit
) {
    Scaffold(
        topBar = {
            PieceTopBar(viewState.piece.name,
                { actioner(PieceActions.Save) },
                { actioner(PieceActions.Close) }
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
            PieceName(viewState.piece.name) { value ->
                actioner(PieceActions.ChangeName(value))
            }
            Divider(color = Color.Transparent, thickness = 4.dp)
            PieceTextField(
                text = viewState.piece.author,
                label = stringResource(R.string.hint_edit_text_author)
            ) { value ->
                actioner(PieceActions.ChangeAuthor(value))
            }
            PieceTextField(
                text = viewState.piece.arranger,
                label = stringResource(R.string.hint_edit_text_arranger)
            ) { value ->
                actioner(PieceActions.ChangeArranger(value))
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.subtitle_sections),
                    style = MaterialTheme.typography.h6,
                )
                AddIcon {actioner(PieceActions.NewSection(viewState.piece.id))}
            }
            SectionsScrollingContent(viewState.sections, actioner)
        }
    }
}

@Composable
fun PieceTopBar(
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
fun PieceName(
    name: String,
    changeName: (String) -> Unit
) {
    TextField(
        value = name,
        onValueChange = changeName,
        label = { Text(stringResource(R.string.hint_edit_text_name)) },
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
    TextField(
        value = text,
        onValueChange = changeText,
        label = { Text(label) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        singleLine = true,
        textStyle = MaterialTheme.typography.body2,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SectionsScrollingContent(
    sections: List<Section>,
    actioner: (PieceActions) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(sections, key = { section -> section.order }) { section ->
            SectionItem(
                section = section,
                modifier = Modifier.fillParentMaxWidth(),
                openSection = { actioner(PieceActions.OpenSection(section.order)) }
            )
        }
    }
}

@Composable
fun SectionItem(
    section: Section,
    modifier: Modifier,
    openSection: () -> Unit
) {
    Divider()
    Column(modifier = Modifier
        .clickable(onClick = openSection)
        .padding(vertical = 6.dp) then modifier) {
        Text(
            text = section.name,
            style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primaryVariant),
        )
        Text(
            text = stringResource(R.string.section_item_beat, section.beat),
            style = MaterialTheme.typography.caption
        )
        Text(
            text = stringResource(R.string.section_item_bars, section.countBars),
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview
@Composable
fun PieceItemPreview() {
    val piece = Piece(
        name = "Sweet Harmony",
        author = "The Beloved",
        arranger = "Eiro Nareth"
    )
    val sections = listOf(
        Section(
            id = 1,
            name = "Intro",
            beat = 120,
        ),
        Section(
            id = 2,
            name = "Verse 1",
        ),
        Section(
            id = 3,
            name = "Outro",
        )
    )
    PieceUi(
        viewState = PieceViewState(piece, sections),
        actioner = {}
    )
}