plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).

    // We are an opinionated framework: export these transitive dependencies
    api(libs.kotlinxCoroutines)
    api(libs.logback)

    // Test dependencies
    testImplementation(libs.bundles.kotest)
}