package com.grommade.lazymusicianship.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.grommade.lazymusicianship.R
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme

@ExperimentalMaterialApi
@Composable
fun SetItemDefault(
    title: String,
    value: String = "",
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    SetItemBody(
        title = title,
        value = value,
        enabled = enabled,
        onClick = onClick
    )
}

@ExperimentalMaterialApi
@Composable
fun SetItemWithClear(
    title: String,
    value: String = "",
    showClear: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onClickClear: () -> Unit = {}
) {
    SetItemBody(
        title = title,
        value = value,
        showClear = showClear,
        enabled = enabled,
        onClick = onClick,
        onClickClear = onClickClear
    )
}


@ExperimentalMaterialApi
@Composable
private fun SetItemBody(
    title: String,
    value: String,
    showSwitch: Boolean = false,
    stateSwitch: Boolean = false,
    showClear: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onClickSwitch: (Boolean) -> Unit = {},
    onClickClear: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp),
        enabled = enabled
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = title,
                    style = when (enabled) {
                        true -> MaterialTheme.typography.h6.copy(fontSize = 16.sp)
                        false -> MaterialTheme.typography.h6.copy(fontSize = 16.sp, color = Color.Gray)
                    }
                )
                Row(
                    modifier = if (showClear) Modifier.fillMaxWidth() else Modifier, // FIXME: WTF?
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (enabled) value else "-",
                        style = MaterialTheme.typography.body2.copy(color = Color.DarkGray)
                    )
                    if (showClear) {
                        Text(
                            text = stringResource(R.string.btn_clear),
                            style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary),
                            modifier = Modifier.selectable(
                                selected = false,
                                onClick = onClickClear
                            )
                        )
                    }
                }
            }
            if (showSwitch) {
                Switch(
                    checked = stateSwitch,
                    onCheckedChange = onClickSwitch,
                    enabled = enabled
                )
            }
        }
    }
    Divider(thickness = 1.dp)
}

@ExperimentalMaterialApi
@Composable
fun SetItemSwitch(
    title: String,
    value: String = "",
    stateSwitch: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onClickSwitch: (Boolean) -> Unit = {},
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.padding(vertical = 8.dp),
        enabled = enabled
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (texts, switch) = createRefs()
            Column(
                Modifier.constrainAs(texts) {
                    start.linkTo(parent.start)
                    end.linkTo(switch.start)
                    width = Dimension.fillToConstraints
                }
            ) {
                Text(
                    text = title,
                    style = when (enabled) {
                        true -> MaterialTheme.typography.h6.copy(fontSize = 16.sp)
                        false -> MaterialTheme.typography.h6.copy(fontSize = 16.sp, color = Color.Gray)
                    }
                )
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = if (enabled) value else "-",
                        style = MaterialTheme.typography.body2.copy(color = Color.DarkGray)
                    )
                }
            }
            Switch(
                checked = stateSwitch,
                onCheckedChange = onClickSwitch,
                enabled = enabled,
                modifier = Modifier.constrainAs(switch) {
                    end.linkTo(parent.end)
                }
            )
        }
    }
    Divider(thickness = 1.dp)
}

@Composable
fun SetItemTitle(text: String) {
    Divider(color = Color.Transparent, thickness = 4.dp)
    Text(
        text = text,
        style = MaterialTheme.typography.body2.copy(
            color = MaterialTheme.colors.secondaryVariant,
            fontWeight = FontWeight.Medium
        )
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SetItemDefaultPreview() {
    LazyMusicianshipTheme {
        SetItemDefault(
            title = "Setting 1",
            value = "01:00"
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SetItemWithClearPreview() {
    LazyMusicianshipTheme {
        SetItemWithClear(
            title = "Setting 2",
            value = "02:50"
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SetItemSwitchPreview() {
    LazyMusicianshipTheme {
        SetItemSwitch(
            title = "Setting 3",
            value = "Very long settings Very long settings Very long settings Very long settings"
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun SetItemTitlePreview() {
    LazyMusicianshipTheme {
        Column {
            SetItemTitle("Title")
            SetItemDefault(
                title = "Setting 4",
                value = "Value 4"
            )
        }

    }
}
