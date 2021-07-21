package com.grommade.lazymusicianship.ui_main

sealed class MainActions {
    object OpenStates : MainActions()
    object OpenSettings : MainActions()
}