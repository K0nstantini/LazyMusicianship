// Top-level build file where you can add configuration options common to all sub-projects/modules.

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath (Dependencies.Plugins.androidGradlePlugin)
        classpath (Dependencies.Plugins.kotlinGradlePlugin)
        classpath (Dependencies.Plugins.hiltGradlePlugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}