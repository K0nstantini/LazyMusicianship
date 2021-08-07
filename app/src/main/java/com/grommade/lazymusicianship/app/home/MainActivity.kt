package com.grommade.lazymusicianship.app.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.insets.ProvideWindowInsets
import com.grommade.lazymusicianship.ui.common.LocalBackPressedDispatcher
import com.grommade.lazymusicianship.ui.theme.LazyMusicianshipTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyMusicianshipTheme {
                ProvideWindowInsets {
                    CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {
                        Home()
                    }
                }
            }
        }
    }
}
