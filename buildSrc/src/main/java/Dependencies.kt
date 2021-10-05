object Dependencies {

    object Compose {
        const val version = "1.1.0-alpha04"
        const val ui = "androidx.compose.ui:ui:$version"
        const val material = "androidx.compose.material:material:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val lifecycle = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        const val junit = "androidx.compose.ui:ui-test-junit4:$version"
        const val icons = "androidx.compose.material:material-icons-extended:$version"

        private const val accompanist = "0.19.0"
        const val insets = "com.google.accompanist:accompanist-insets:$accompanist"
        const val insetsUi = "com.google.accompanist:accompanist-insets-ui:$accompanist"
        const val pager = "com.google.accompanist:accompanist-pager:$accompanist"
    }

    object Kotlin {
        const val version = "1.5.30"
    }

    object Plugins {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.0-alpha13"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.version}"
    }

    object Hilt {
        const val version = "2.38.1"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
        const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
    }

    object Room {
        private const val version = "2.4.0-alpha05"
        const val  roomKtx = "androidx.room:room-ktx:$version"
        const val  roomCompiler = "androidx.room:room-compiler:$version"
    }

}