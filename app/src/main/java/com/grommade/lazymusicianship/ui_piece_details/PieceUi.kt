package com.grommade.lazymusicianship.ui_piece_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.grommade.lazymusicianship.ui.theme.*
import com.grommade.lazymusicianship.util.extentions.minutesToStrTime
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
        BuiltSimpleOkCancelDialogDel(
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

    var state by remember { mutableStateOf(0) } // fixme

    Scaffold(
        topBar = {
            SaveCloseTopBar(
                saveEnabled = viewState.piece.name.isNotEmpty(),
                save = { actioner(PieceActions.SaveAndClose) },
                close = { actioner(PieceActions.Close) }
            )
        },
        floatingActionButton = {
            if (state == 0) {
                FloatingAddActionButton { actioner(PieceActions.NewSection(0)) }
            }
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

            val titles = listOf(
                stringResource(R.string.tab_sections),
                stringResource(R.string.tab_info),
            )
            TabRow(
                selectedTabIndex = state,
                contentColor = DarkRed,
                backgroundColor = Color.Transparent
            ) {
                titles.forEachIndexed { index, title ->
                    val tabColor = if (state == index) DarkRed else LightPurple1
                    Tab(
                        text = { Text(title, color = tabColor) },
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
    AuthorAndArrangerFields(piece, actioner)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {

        val modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, DarkBlue2, RoundedCornerShape(8.dp))
            .weight(1f)
            .height(55.dp)

        TimeField(piece.time, modifier) { actioner(PieceActions.ChangeTime(it)) }
        FinishedField(piece.finished, modifier) { actioner(PieceActions.ChangeFinished(it)) }
    }

    DescriptionField(piece.description) { actioner(PieceActions.ChangeDescription(it)) }
}

@Composable
fun AuthorAndArrangerFields(
    piece: Piece,
    actioner: (PieceActions) -> Unit,
) {
    val modifier = Modifier.padding(top = 8.dp)

    AppOutlinedTextField(
        text = piece.author,
        label = stringResource(R.string.piece_title_author),
        modifier = modifier
    ) { actioner(PieceActions.ChangeAuthor(it)) }

    AppOutlinedTextField(
        text = piece.arranger,
        label = stringResource(R.string.piece_title_arranger),
        modifier = modifier
    ) { actioner(PieceActions.ChangeArranger(it)) }
}

@Composable
fun TimeField(
    time: Int,
    modifier: Modifier,
    changeTime: (Int) -> Unit
) {
    val timeDialog = remember { MaterialDialog() }.apply {
        BuiltTimeDialog(time, changeTime, true)
    }
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable(onClick = timeDialog::show) then modifier
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.piece_title_time),
                fontSize = 14.sp,
                color = LightPurple2,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = time.minutesToStrTime(),
                fontSize = 12.sp,
                color = if (time == 0) DarkYellow.copy(alpha = 0.5f) else DarkYellow,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
fun FinishedField(
    finished: Boolean,
    modifier: Modifier,
    changeFinished: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .clickable(onClick = { changeFinished(!finished) }) then modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.piece_title_finished), color = LightPurple2)
            AppSwitch(
                checked = finished,
                onCheckedChange = changeFinished,
                enabled = true,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun DescriptionField(
    description: String,
    changeDescription: (String) -> Unit
) {
    OutlinedTextField(
        value = description,
        onValueChange = changeDescription,
        label = { Text(stringResource(R.string.piece_title_description), color = Color(0x809290AD)) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = LightPurple2),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 16.dp),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun SectionsScrollingContent(
    sections: List<Section>,
    selectedSection: Long,
    actioner: (PieceActions) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
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
    val backgroundColor = if (selected) LightPurple2 else LightPurple1
    val border = if (selected) BorderStroke(width = 1.dp, color = DarkRed) else null
    val padding = (level * 8).dp

    Card(
        border = border,
        backgroundColor = backgroundColor,
        modifier = Modifier.padding(start = padding, top = 8.dp)
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
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                SectionItemName(name)
                if (description.isNotEmpty()) {
                    SectionItemDetails(description)
                }
            }
            if (selected) {
                Row {
                    DeleteIcon(deleteSection)
                    AddIcon(newSection)
                }
            }
        }
    }
}

@Composable
fun SectionItemName(name: String) {
    Text(
        text = name,
        maxLines = 2,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = DarkPurple,
    )
}

@Composable
fun SectionItemDetails(info: String) {
    Text(
        text = info,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        color = DarkBlue,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 6.dp)
    )
}

@Preview
@Composable
fun PieceItemPreview() {
    val piece = Piece(
        name = "Sweet Harmony",
        author = "The Beloved",
        arranger = "Unknown"
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
    LazyMusicianshipTheme {
        PieceUi(
            viewState = PieceViewState(piece, sections, selectedSection = 4),
            actioner = {}
        )
    }
}