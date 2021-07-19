package com.grommade.lazymusicianship.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun BuiltSimpleOkCancelDialog(
    title: String,
    callback: () -> Unit,
    close: () -> Unit
) {
    AlertDialog(
        onDismissRequest = close,
        title = { Text(title) },
        confirmButton = {
            TextButton(
                onClick = {
                    callback()
                    close()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = close
            ) {
                Text("Cancel")
            }
        }
    )
}