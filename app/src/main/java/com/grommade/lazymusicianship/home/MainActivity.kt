package com.grommade.lazymusicianship.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.insets.ProvideWindowInsets
import com.grommade.lazymusicianship.ui.common.LocalBackPressedDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
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
