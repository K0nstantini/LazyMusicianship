package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme

@Composable
fun TextFieldName(
    text: String,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = changeText,
        label = { Text(stringResource(R.string.piece_hint_edit_text_name), color = Color(0x809290AD)) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        isError = text.isEmpty(),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, color = Color(0xFF9290AD)),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun TextFieldNamePreview() {
    LazyMusicianshipTheme {
        TextFieldName(text = "") {}
    }
}