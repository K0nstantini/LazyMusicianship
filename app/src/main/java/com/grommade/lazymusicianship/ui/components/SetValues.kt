package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.ui.components.material_dialogs.core.MaterialDialog
import com.grommade.lazymusicianship.ui.theme.DarkBlue2
import com.grommade.lazymusicianship.ui.theme.DarkYellow
import com.grommade.lazymusicianship.ui.theme.LightPurple2
import java.time.LocalDate

@Composable
fun SetDefaultValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, DarkBlue2, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LightPurple2,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = DarkYellow,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun SetDateValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (LocalDate) -> Unit
) {
    val dateDialog = remember { MaterialDialog() }.apply {
        BuiltDateDialog(onClick)
    }
    SetDefaultValue(title, value, modifier, dateDialog::show)
}


@Composable
fun SetValueBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, DarkBlue2, RoundedCornerShape(8.dp))
    ) {
        content()
    }
}

@Composable
fun SetTitleWithValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(start = 16.dp) then modifier
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = LightPurple2,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = DarkYellow,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )
    }
}