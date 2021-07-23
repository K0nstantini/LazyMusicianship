package com.grommade.lazymusicianship.ui_pieces

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.BuildConfig
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.DeleteIcon
import com.grommade.lazymusicianship.ui.components.FloatingAddActionButton
import com.grommade.lazymusicianship.ui.components.MoreVertIcon

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
) {
    Scaffold(
        topBar = {
            PiecesListTopBar { actioner(PiecesListActions.PopulateDB) }
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
}

@Composable
fun PiecesListTopBar(populateDB: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.pieces_title)) },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.97f),
        contentColor = MaterialTheme.colors.onSurface,
        actions = {
            if (BuildConfig.DEBUG) {
                PiecesListDropdownMenu(populateDB)
            }
        }
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
    val hasAuthor = piece.author.isNotEmpty()
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
                onClick = openPiece,
                onLongClick = selectPiece
            )
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                then modifier
    ) {
        Column() {
            Text(
                text = piece.name,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle1,
            )
            if (hasAuthor) {
                Text(
                    text = piece.author,
                    maxLines = 1,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
        if (selected) {
            DeleteIcon(deletePiece)
        }
    }
}

@Preview
@Composable
fun PiecesListUiPreview() {
    val list = listOf(
        Piece(id = 1, name = "Sherlock (BBC) Main Theme", author = "David Arnold & Michael Price"),
        Piece(id = 2, name = "Elfen Lied"),
        Piece(id = 3, name = "Let It Be", author = "Beatles"),
    )
    PiecesListUi(PiecesListViewState(pieces = list, selectedPiece = 2)) {}
}

@Preview
@Composable
fun PieceItemPreview() {
    PieceItem(
        piece = Piece(name = "Sherlock (BBC) Main Theme"),
        selected = false,
        modifier = Modifier,
        selectPiece = {},
        openPiece = {},
        deletePiece = {}
    )
}