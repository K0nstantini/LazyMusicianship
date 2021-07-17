package com.grommade.lazymusicianship.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.insets.ProvideWindowInsets
import com.grommade.lazymusicianship.ui.common.LocalBackPressedDispatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets {
                CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {
                    Home()
                }
            }
        }
    }
}
