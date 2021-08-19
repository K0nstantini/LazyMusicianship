package com.grommade.lazymusicianship.ui.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog

class AppDialog {

    private val showing: MutableState<Boolean> = mutableStateOf(false)

    fun show() {
        showing.value = true
    }

    fun hide(focusManager: FocusManager? = null) {
        focusManager?.clearFocus()
        showing.value = false
    }

    @Composable
    fun Build(content: @Composable AppDialog.() -> Unit) {
        if (showing.value) {
            Dialog(onDismissRequest = ::hide) {
                this@AppDialog.content()
            }
        }
    }

    companion object Colors {
        val primaryTextColor = Color(0xFFF8F7FA)
        val secondaryTextColor = Color(0xFFC9C8CC)

        val backgroundColor = Color(0xFF282729)
        val lightBackgroundColor = Color(0xFF313133)
        val primaryColor = Color(0xFF328B36)
    }
}