// The code in this file is a convention plugin - a Gradle mechanism for sharing reusable build logic.
// `buildSrc` is a Gradle-recognized directory and every plugin there will be easily available in the rest of the build.
package buildsrc.convention

import org.gradle.api.tasks.testing.logging.TestLogEvent
import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin in JVM projects.
    kotlin("jvm")
    id("com.adarshr.test-logger")
    id("io.kotest")
}

group = "net.paploo"
version = "0.0.1-SNAPSHOT"

kotlin {
    // Use a specific Java version to make it easier to work in different environments.
    jvmToolchain(21)
}

tasks.withType<Test>().configureEach {
    // Configure all test Gradle tasks to use JUnitPlatform.
    useJUnitPlatform()

    testlogger {
        theme = ThemeType.STANDARD_PARALLEL // Pretty colors
        //theme = ThemeType.PLAIN_PARALLEL // Good for automated build systems
    }
}
