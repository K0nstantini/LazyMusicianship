package com.grommade.lazymusicianship.ui_piece

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.grommade.lazymusicianship.data.entity.Piece
import com.grommade.lazymusicianship.ui.common.rememberFlowWithLifecycle
import com.grommade.lazymusicianship.ui.components.NavigationCloseIcon
import com.grommade.lazymusicianship.ui.components.SaveIcon

@Composable
fun PieceUi(Close: () -> Unit) {
    PieceUi(
        viewModel = hiltViewModel(),
        close = Close
    )
}

@Composable
fun PieceUi(
    viewModel: PieceViewModel,
    close: () -> Unit
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = PieceViewState.Empty)

    PieceUi(viewState) { action ->
        when (action) {
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
            PieceTopBar(viewState.piece.title,
                { actioner(PieceActions.Close) },
                { actioner(PieceActions.Save) }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

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


@Preview
@Composable
fun PieceItemPreview() {
    val piece = Piece(
        title = "Sweet Harmony",
        author = "The Beloved",
        arranger = "Eiro Nareth"
    )
    PieceUi(
        viewState = PieceViewState(piece),
        actioner = {}
    )
}