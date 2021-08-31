package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.theme.DarkWhite
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme
import com.grommade.lazymusicianship.ui.theme.LightPurple2

@Composable
fun TextFieldName(
    text: String,
    changeText: (String) -> Unit
) {
    AppOutlinedTextField(
        text = text,
        label = stringResource(R.string.piece_hint_edit_text_name),
        style = LocalTextStyle.current.copy(fontSize = 18.sp, color = DarkWhite),
        isError = true,
        modifier = Modifier.fillMaxWidth(),
        changeText = changeText
    )
}

@Composable
fun AppOutlinedTextField(
    text: String,
    modifier: Modifier = Modifier,
    label: String = "",
    style: TextStyle =  LocalTextStyle.current.copy(fontSize = 14.sp, color = LightPurple2),
    isError: Boolean = false,
    changeText: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = changeText,
        label = { Text(label, color = Color(0x809290AD)) }, // fixme
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        isError = isError,
        singleLine = true,
        textStyle = style,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview
@Composable
fun TextFieldNamePreview() {
    LazyMusicianshipTheme {
        TextFieldName(text = "") {}
    }
}