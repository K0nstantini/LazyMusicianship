package com.grommade.lazymusicianship.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowSnackBar(
    error: String?,
    modifier: Modifier,
    onDismiss: () -> Unit
) {
    val snackHostState = remember { SnackbarHostState() }
    SnackbarHost(
        hostState = snackHostState,
        snackbar = {
            SwipeDismissSnackBar(
                data = it,
                onDismiss = onDismiss
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 48.dp) then modifier
    )

    LaunchedEffect(error) {
        error?.let {
            snackHostState.showSnackbar(error)
        }
    }
}