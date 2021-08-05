package com.grommade.lazymusicianship.ui.components.diapogs

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.components.IconChevronLeft
import com.grommade.lazymusicianship.ui.components.IconChevronRight
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.background
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.lightBackground
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.primary
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.primaryText
import com.grommade.lazymusicianship.ui.components.diapogs.AppDialog.Colors.secondaryText

// FIXME: Rename package

@Composable
fun AppDialog.BuildPeriodDialog() {
    Build {
        DrawPeriod()
    }
}

@Composable
private fun AppDialog.DrawPeriod() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 16.dp)
            .clipToBounds(),
        color = Color.Transparent,
        contentColor = primaryText
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Presets()

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    Column {
                        MonthBody()
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
                        )
                        MonthBody()
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
                        )
                        Confirmation()
                    }
                }
            }
        }
    }
}

@Composable
fun Presets() {
    Column {
        PresetText(R.string.preset_last_7_days)
        PresetText(R.string.preset_last_30_days)
        PresetText(R.string.preset_last_12_months)
        PresetText(R.string.preset_week_to_date)
        PresetText(R.string.preset_month_to_date)
        PresetText(R.string.preset_year_to_date)
        PresetText(R.string.preset_all_time)
    }
}

@Composable
fun MonthBody() {
    MonthYearHeader()
    MonthGrid()
}

@Composable
fun Confirmation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(end = 8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = lightBackground,
                contentColor = secondaryText
            ),
        ) {
            Text(text = "Cancel")
        }
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(backgroundColor = primary),
        ) {
            Text(text = "Set date")
        }
    }
}

@Composable
fun PresetText(@StringRes res: Int) {
    Text(
        text = stringResource(res),
        color = secondaryText,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun MonthYearHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconChevronLeft(0.3f)
        Text("August 2021")
        IconChevronRight(0.3f)
    }
}

@Composable
fun MonthGrid() {
    val numbers = (26..31).toList() + (1..31).toList() + (1..5).toList() // FIXME
    Column() {
        LazyVerticalGrid(cells = GridCells.Fixed(7)) {
            items(numbers) {
                DateSelectionBox(it) {}
            }
        }
    }
}

@Composable
private fun DateSelectionBox(date: Int, onClick: () -> Unit) {
    Box(
        Modifier
            .size(40.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                onClick = onClick,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            date.toString(),
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .wrapContentSize(Alignment.Center),
            style = TextStyle(
                color = secondaryText,
                fontSize = 12.sp
            )
        )
    }
}

@Preview
@Composable
fun DrawPeriodPreview() {
    AppDialog().DrawPeriod()
//    AppDialog().DrawPeriod(
//        period = LocalDate.MIN to LocalDate.MAX
//    ) { _, _ -> }
}