package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.R

@Composable
fun TextFieldName(
    text: String,
    changeText: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = changeText,
        label = { Text(stringResource(R.string.piece_hint_edit_text_name)) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        isError = text.isEmpty(),
        singleLine = true,
        textStyle = MaterialTheme.typography.h6.copy(
            fontSize = 16.sp,
            color = MaterialTheme.colors.secondaryVariant
        ),
        modifier = Modifier.fillMaxWidth()
    )
}