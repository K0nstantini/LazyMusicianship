package com.grommade.lazymusicianship.ui_pieces_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.BuildConfig
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.MoreVertIcon

@Composable
fun PiecesListUi() {
    PiecesListUi(viewModel = hiltViewModel())
}

@Composable
fun PiecesListUi(
    viewModel: PiecesListViewModel
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PiecesListViewState.Empty)

    PiecesListUi(viewState) { action ->
        when (action) {
            PiecesListActions.AddNew -> {
            }
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
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(viewState.pieces, key = { piece -> piece.id }) { piece ->
                PieceItem(piece, Modifier.fillParentMaxWidth())
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
    modifier: Modifier
) {
    val hasAuthor = piece.author.isNotEmpty()

    ConstraintLayout(
        modifier = Modifier.clickable { /* TODO */ } then modifier
    ) {
        val (divider, pieceTitle, authorTitle, overflow) = createRefs()

        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = piece.title,
            maxLines = 2,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(pieceTitle) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, 16.dp)
                if (!hasAuthor) {
                    bottom.linkTo(parent.bottom, 16.dp)
                }
                width = Dimension.preferredWrapContent
            }
        )
        if (hasAuthor) {
            Text(
                text = piece.author,
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.constrainAs(authorTitle) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = 24.dp,
                        endMargin = 16.dp,
                        bias = 0f
                    )
                    top.linkTo(pieceTitle.bottom, 6.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    width = Dimension.preferredWrapContent
                }
            )
        }
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier.constrainAs(overflow) {
                end.linkTo(parent.end, 8.dp)
                centerVerticallyTo(pieceTitle)
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.cd_more_vert_icon)
            )
        }
    }
}


@Preview
@Composable
fun PiecesListUiPreview() {
    val list = listOf(
        Piece(id = 1, title = "Sherlock (BBC) Main Theme", author = "David Arnold & Michael Price"),
        Piece(id = 2, title = "Elfen Lied"),
        Piece(id = 3, title = "Let It Be", author = "Beatles"),
    )
    PiecesListUi(PiecesListViewState(pieces = list)) {}
}

@Preview
@Composable
fun PieceItemPreview() {
    PieceItem(
        piece = Piece(title = "Sherlock (BBC) Main Theme"),
        modifier = Modifier
    )
}