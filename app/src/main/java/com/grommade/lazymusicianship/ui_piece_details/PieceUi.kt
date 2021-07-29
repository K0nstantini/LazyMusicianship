package com.grommade.lazymusicianship.ui_piece_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.ui.common.ShowSnackBar
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.*
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.util.extentions.toStrTime
import kotlinx.coroutines.launch

@Composable
fun PieceUi(
    openSection: (Long, Long, Long) -> Unit,
    close: () -> Unit,
) {
    PieceUi(
        viewModel = hiltViewModel(),
        openSection = openSection,
        close = close
    )
}

@Composable
fun PieceUi(
    viewModel: PieceViewModel,
    openSection: (Long, Long, Long) -> Unit,
    close: () -> Unit
) {

    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PieceViewState.Empty)

    val scope = rememberCoroutineScope()

    val confirmToWriteDialog = remember { mutableStateOf(false) }
    if (confirmToWriteDialog.value) {
        BuiltSimpleOkCancelDialog(
            title = stringResource(R.string.alert_title_save_before_add_section),
            callback = {
                scope.launch {
                    openSection(viewModel.save(), 0, 0)
                }
            },
            close = { confirmToWriteDialog.value = false }
        )
    }

    PieceUi(viewState) { action ->
        when (action) {
            is PieceActions.OpenSection -> openSection(viewState.piece.id, action.sectionId, 0)
            is PieceActions.NewSection -> {
                if (viewState.piece.isNew) {
                    confirmToWriteDialog.value = true
                } else {
                    openSection(viewState.piece.id, 0, action.parentId)
                }
            }
            PieceActions.SaveAndClose -> scope.launch {
                viewModel.save()
                close()
            }
            PieceActions.Close -> close()
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun PieceUi(
    viewState: PieceViewState,
    actioner: (PieceActions) -> Unit
) = Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        topBar = {
            SaveCloseTopBar(
                saveEnabled = viewState.piece.name.isNotEmpty(),
                save = { actioner(PieceActions.SaveAndClose) },
                close = { actioner(PieceActions.Close) }
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
            TextFieldName(viewState.piece.name) { value ->
                actioner(PieceActions.ChangeName(value))
            }

            var state by remember { mutableStateOf(0) }
            val titles = listOf(
                stringResource(R.string.tab_sections),
                stringResource(R.string.tab_info),
                stringResource(R.string.tab_description),
            )
            TabRow(
                selectedTabIndex = state,
                backgroundColor = Color.Transparent
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = state == index,
                        onClick = { state = index }
                    )
                }
            }
            when (state) {
                0 -> SectionsScrollingContent(
                    sections = viewState.sections,
                    selectedSection = viewState.selectedSection,
                    actioner = actioner
                )
                1 -> PieceInfo(viewState.piece, actioner)
                2 -> PieceDescription(viewState.piece.description) { value: String ->
                    actioner(PieceActions.ChangeDescription(value))
                }
            }
        }
    }

    ShowSnackBar(
        error = viewState.error,
        modifier = Modifier.align(Alignment.BottomCenter),
        onDismiss = { actioner(PieceActions.ClearError) }
    )
}

@Composable
fun PieceInfo(
    piece: Piece,
    actioner: (PieceActions) -> Unit
) {
    SetItemDefaultWithInputDialog(
        title = stringResource(R.string.piece_title_author),
        value = piece.author,
        callback = { actioner(PieceActions.ChangeAuthor(it)) }
    )
    SetItemDefaultWithInputDialog(
        title = stringResource(R.string.piece_title_arranger),
        value = piece.arranger,
        callback = { actioner(PieceActions.ChangeArranger(it)) }
    )
    TimeItem(
        title = stringResource(R.string.piece_title_time),
        value = piece.time,
        changeTime = { value: Int -> actioner(PieceActions.ChangeTime(value)) }
    )
}

@Composable
fun TimeItem(
    title: String,
    value: Int,
    changeTime: (Int) -> Unit
) {
    val alertDialog = remember { MaterialDialog() }
        .apply { BuiltTimeDialog(value, changeTime, true) }

    SetItemDefault(
        title = title,
        value = value.toStrTime(),
        onClick = alertDialog::show
    )
}

@Composable
fun PieceDescription(
    description: String,
    changeDescription: (String) -> Unit
) {
    OutlinedTextField(
        value = description,
        onValueChange = changeDescription,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun SectionsScrollingContent(
    sections: List<Section>,
    selectedSection: Long,
    actioner: (PieceActions) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = { actioner(PieceActions.NewSection(0)) },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .padding(end = 8.dp)
                .size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.cd_add_icon),
                tint = MaterialTheme.colors.secondaryVariant,
            )
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(sections) { section ->
            SectionItem(
                name = section.name,
                level = section.getLevel(sections),
                selected = section.id == selectedSection,
                description = section.description,
                modifier = Modifier.fillParentMaxWidth(),
                openSection = { actioner(PieceActions.OpenSection(section.id)) },
                newSection = { actioner(PieceActions.NewSection(section.id)) },
                selectSection = { actioner(PieceActions.SelectSection(section.id)) },
                deleteSection = { actioner(PieceActions.DeleteSection(section)) },
            )
        }
    }
}

@Composable
fun SectionItem(
    name: String,
    level: Int,
    selected: Boolean,
    description: String,
    modifier: Modifier,
    openSection: () -> Unit,
    newSection: () -> Unit,
    selectSection: () -> Unit,
    deleteSection: () -> Unit
) {
    val backgroundColor = when (selected) {
        true -> Color.LightGray
        false -> Color(0xFFb2fab4)
    }
    val padding = (level * 8).dp
    Card(
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        backgroundColor = backgroundColor,
        modifier = Modifier.padding(start = padding, bottom = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .combinedClickable(
                    onClick = openSection,
                    onLongClick = selectSection
                )
                .padding(start = 32.dp) then modifier
        ) {
            Column() {
                Text(
                    text = name,
                    style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primaryVariant),
                )
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.DarkGray,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            if (selected) {
                Row() {
                    DeleteIcon(deleteSection)
                    AddIcon(newSection)
                }
            }
        }
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
            order = 1,
            name = "Intro",
            tempo = 120,
            description = "I-V-I"
        ),
        Section(
            id = 2,
            order = 2,
            name = "Verse 1",
            description = "Very long description. Very long description. Very long description. Very long description. Very long description. Very long description. Very long description. Very long description. Very long description. Very long description. Very long description"
        ),
        Section(
            id = 3,
            order = 1,
            name = "1",
            parentId = 2,
        ),
        Section(
            id = 4,
            order = 2,
            name = "2",
            parentId = 2,
            description = "I-V-I\nSomething\nMode"
        ),
        Section(
            id = 5,
            order = 3,
            name = "Verse 2",
        ),
        Section(
            id = 6,
            order = 4,
            name = "Outro",
            description = "I-V-I\nSomething\nMode\nDescription"
        )
    )
    PieceUi(
        viewState = PieceViewState(piece, sections),
        actioner = {}
    )
}