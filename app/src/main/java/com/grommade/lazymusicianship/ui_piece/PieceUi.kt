package com.grommade.lazymusicianship.ui_piece

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.data.entity.Section
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.*
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.util.toStrTime
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
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

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@Composable
fun PieceUi(
    viewModel: PieceViewModel,
    openSection: (Long, Long, Long) -> Unit,
    close: () -> Unit
) {

    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PieceViewState.Empty)

    viewModel.navigateToSection.collectAsState(null).value?.let { pieceId ->
        openSection(pieceId, 0, 0)
    }
    viewModel.navigateToBack.collectAsState(null).value?.let { close() }

    val confirmToWriteDialog = remember { mutableStateOf(false) }
    if (confirmToWriteDialog.value) {
        BuiltSimpleOkCancelDialog(
            title = stringResource(R.string.alert_title_save_before_add_section),
            callback = viewModel::saveAndAddSection,
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
            PieceActions.Close -> close()
            else -> viewModel.submitAction(action)
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun PieceUi(
    viewState: PieceViewState,
    actioner: (PieceActions) -> Unit
) {
    Scaffold(
        topBar = {
            PieceTopBar(viewState.piece.name,
                { actioner(PieceActions.SaveAndClose) },
                { actioner(PieceActions.Close) }
            )
        },
        modifier = Modifier.fillMaxSize(),
//        backgroundColor = Color(0xFFcbae82)
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
//            Divider(color = Color.Transparent, thickness = 4.dp)
//            PieceTextField(
//                text = viewState.piece.author,
//                label = stringResource(R.string.piece_hint_edit_text_author)
//            ) { value ->
//                actioner(PieceActions.ChangeAuthor(value))
//            }
//            PieceTextField(
//                text = viewState.piece.arranger,
//                label = stringResource(R.string.piece_hint_edit_text_arranger)
//            ) { value ->
//                actioner(PieceActions.ChangeArranger(value))
//            }
//            BeatField(
//                beat = viewState.piece.tempo,
//                label = stringResource(R.string.piece_hint_edit_text_beat)
//            ) { value ->
//                actioner(PieceActions.ChangeBeat(value))
//            }
//            TimeField(
//                time = viewState.piece.time,
//                label = stringResource(R.string.piece_hint_edit_text_time)
//            ) { value ->
//                actioner(PieceActions.ChangeTime(value))
//            }
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .padding(top = 8.dp)
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = stringResource(R.string.subtitle_sections),
//                    style = MaterialTheme.typography.h6,
//                )
//                AddIcon { actioner(PieceActions.NewSection(0)) }
//            }
            var state by remember { mutableStateOf(1) }
            val titles = listOf(
                stringResource(R.string.subtitle_sections),
                stringResource(R.string.subtitle_info),
                stringResource(R.string.subtitle_description),
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
            }
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
//        backgroundColor = Color(0xFFcbae82),
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
fun PieceInfo(
    piece: Piece,
    actioner: (PieceActions) -> Unit
) {
    InfoItem(
        title = stringResource(R.string.alert_title_change_author),
        value = piece.author,
        label = stringResource(R.string.piece_hint_edit_text_author),
        changeInfo = { value: String -> actioner(PieceActions.ChangeAuthor(value)) }
    )
    InfoItem(
        title = stringResource(R.string.alert_title_change_arranger),
        value = piece.arranger,
        label = stringResource(R.string.piece_hint_edit_text_arranger),
        changeInfo = { value: String -> actioner(PieceActions.ChangeArranger(value)) }
    )
    InfoItem(
        title = stringResource(R.string.alert_title_change_tempo),
        value = piece.tempo.toString(),
        label = stringResource(R.string.piece_hint_edit_text_tempo),
        isTextValid = { text: String -> text.isDigitsOnly() },
        changeInfo = { value: String -> actioner(PieceActions.ChangeTempo(value)) }
    )
    TimeItem(
        title = stringResource(R.string.alert_title_change_time),
        value = piece.time,
        changeTime = { value: Int -> actioner(PieceActions.ChangeTime(value)) }
    )
}

@ExperimentalMaterialApi
@Composable
fun InfoItem(
    title: String,
    value: String,
    label: String,
    isTextValid: (String) -> Boolean = { true },
    changeInfo: (String) -> Unit
) {
    val alertDialog = remember { mutableStateOf(false) }
    if (alertDialog.value) {
        BuiltInputDialog(
            title = title,
            value = value,
            label = label,
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
@Composable
fun TimeItem(
    title: String,
    value: Int,
    changeTime: (Int) -> Unit
) {
    val alertDialog = remember { MaterialDialog() }
        .apply { BuiltMSTimeDialog(value, changeTime) }

    SetItemDefault(
        title = title,
        value = value.toStrTime(),
        onClick = alertDialog::show
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
fun BeatField(
    beat: Int,
    label: String,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        value = beat.toString(),
        onValueChange = changeText,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.width(100.dp)
    )
}

@Composable
fun TimeField(
    time: Int,
    label: String,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        value = time.toString(),
        onValueChange = changeText,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.width(100.dp)
    )
}

@ExperimentalFoundationApi
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
            onClick = {},
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
                modifier = Modifier.fillParentMaxWidth(),
                openSection = { actioner(PieceActions.OpenSection(section.id)) },
                newSection = { actioner(PieceActions.NewSection(section.id)) },
                selectSection = { actioner(PieceActions.SelectSection(section.id)) },
                deleteSection = { actioner(PieceActions.DeleteSection(section)) },
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun SectionItem(
    name: String,
    level: Int,
    selected: Boolean,
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
            Text(
                text = name,
                style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primaryVariant),
            )
            if (selected) {
                Row() {
                    DeleteIcon(deleteSection)
                    AddIcon(newSection)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
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
            order = 0,
            name = "Intro",
            tempo = 120,
        ),
        Section(
            id = 2,
            order = 1,
            name = "Verse 1",
        ),
        Section(
            id = 3,
            order = 0,
            name = "1",
            parentId = 2,
        ),
        Section(
            id = 4,
            order = 1,
            name = "2",
            parentId = 2,
        ),
        Section(
            id = 5,
            order = 2,
            name = "Outro",
        )
    )
    PieceUi(
        viewState = PieceViewState(piece, sections),
        actioner = {}
    )
}