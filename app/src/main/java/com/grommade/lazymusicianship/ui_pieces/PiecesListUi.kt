package com.grommade.lazymusicianship.ui_pieces

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.ui.common.ShowSnackBar
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.DeleteIcon
import com.grommade.lazymusicianship.ui.components.FloatingAddActionButton
import com.grommade.lazymusicianship.ui.components.IconMusicNote
import com.grommade.lazymusicianship.ui.components.MoreVertIcon
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme

private const val Debug = false // fixme

@Composable
fun PiecesListUi(openPiece: (Long) -> Unit) {
    PiecesListUi(
        viewModel = hiltViewModel(),
        openPiece = openPiece
    )
}

@Composable
fun PiecesListUi(
    viewModel: PiecesListViewModel,
    openPiece: (Long) -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PiecesListViewState.Empty)

    PiecesListUi(viewState) { action ->
        when (action) {
            is PiecesListActions.Open -> openPiece(action.pieceId)
            PiecesListActions.AddNew -> openPiece(-1L)
            else -> viewModel.submitAction(action)
        }
    }
}

@Composable
fun PiecesListUi(
    viewState: PiecesListViewState,
    actioner: (PiecesListActions) -> Unit
) = Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        topBar = {
            if (Debug) {
                PiecesListTopBar { actioner(PiecesListActions.PopulateDB) }
            }
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { FloatingAddActionButton { actioner(PiecesListActions.AddNew) } },
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(viewState.pieces, key = { piece -> piece.id }) { piece ->
                PieceItem(
                    piece = piece,
                    selected = piece.id == viewState.selectedPiece,
                    modifier = Modifier.fillParentMaxWidth(),
                    selectPiece = { actioner(PiecesListActions.SelectPiece(piece.id)) },
                    deletePiece = { actioner(PiecesListActions.Delete(piece)) },
                    openPiece = { actioner(PiecesListActions.Open(piece.id)) }
                )
            }
        }
    }

    ShowSnackBar(
        error = viewState.error,
        modifier = Modifier.align(Alignment.BottomCenter),
        onDismiss = { actioner(PiecesListActions.ClearError) }
    )
}

@Composable
fun PiecesListTopBar(populateDB: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.pieces_title)) },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.97f),
        contentColor = MaterialTheme.colors.onSurface,
        actions = { PiecesListDropdownMenu(populateDB) }
    )
}

@Composable
fun PiecesListDropdownMenu(populateDB: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        MoreVertIcon { expanded = true }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                populateDB()
            }) {
                Text("Fill in")
            }
        }
    }
}


@Composable
fun PieceItem(
    piece: Piece,
    selected: Boolean,
    modifier: Modifier,
    selectPiece: () -> Unit,
    openPiece: () -> Unit,
    deletePiece: () -> Unit
) {
    Divider(color = Color(0x8029224E))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = if (selected) Color(0xFF645D90) else Color.Transparent)
            .combinedClickable(
                onClick = openPiece,
                onLongClick = selectPiece
            )
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                then modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemIcon()
            ItemInfo(piece.name, piece.author, selected)
        }
        if (selected) {
            DeleteIcon(deletePiece)
        }
    }
}

@Composable
fun ItemIcon() {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .border(1.dp, Color(0xFF645D90), CircleShape)
            .background(Color(0xFF150F41)),
        contentAlignment = Alignment.Center
    ) {
        IconMusicNote(Color(0xFFDE395A))
    }
}

@Composable
fun ItemInfo(
    name: String,
    author: String,
    selected: Boolean
) {
    val headerColor = Color(0xFFB1AFCD).let {
        if (selected) it else it.copy(alpha = 0.8f)
    } // fixme

    val recentnessColor = if (selected) Color(0xCCB1AFCD) else Color(0xCC645D90) // fixme

    Column(
        modifier = Modifier.padding(start = 32.dp)
    ) {
        Text(
            text = name,
            maxLines = 2,
            fontSize = 18.sp,
            color = headerColor,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = author.ifEmpty { stringResource(R.string.pieces_unknown_author) },
            maxLines = 1,
            fontSize = 12.sp,
            color = Color(0xFFAAB694),
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            text = "5 days ago",
            maxLines = 1,
            fontSize = 10.sp,
            color = recentnessColor,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
fun PiecesListUiPreview() {
    val list = listOf(
        Piece(id = 1, name = "Sherlock (BBC) Main Theme", author = "David Arnold & Michael Price"),
        Piece(id = 2, name = "Elfen Lied"),
        Piece(id = 3, name = "Let It Be", author = "Beatles"),
        Piece(id = 4, name = "Rape me", author = "Nirvana"),
    )
    LazyMusicianshipTheme {
        PiecesListUi(PiecesListViewState(pieces = list, selectedPiece = 2)) {}
    }
}

@Preview
@Composable
fun PieceItemPreview() {
    LazyMusicianshipTheme {
        PieceItem(
            piece = Piece(name = "Sherlock (BBC) Main Theme"),
            selected = false,
            modifier = Modifier,
            selectPiece = {},
            openPiece = {},
            deletePiece = {}
        )
    }
}