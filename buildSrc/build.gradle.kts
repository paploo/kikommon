plugins {
    // The Kotlin DSL plugin provides a convenient way to develop convention plugins.
    // Convention plugins are located in `src/main/kotlin`, with the file extension `.gradle.kts`,
    // and are applied in the project's `build.gradle.kts` files as required.
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Add a dependency on the Kotlin Gradle plugin, so that convention plugins can apply it.
    implementation(libs.kotlinGradlePlugin)
    // Plugin jars must be on the implementation classpath so precompiled script plugins
    // can apply them by id without needing a version (version is baked into the jar on the classpath).
    implementation(libs.plugins.kotestPlugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" })
    implementation(libs.plugins.testLogger.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" })
}
