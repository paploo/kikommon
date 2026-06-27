package net.paploo.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

/**
 * A library of standardized Json configurations.
 */
object JsonLibrary {

    /**
     * Best configuration for application use.
     *
     * This sets `ignoreUnknownKeys` to true, allowing for decoupled deployments, especially
     * with microservices.
     */
    fun application(jsonBuilder: JsonBuilder.() -> Unit): Json = Json {
        ignoreUnknownKeys = true
        jsonBuilder(this)
    }

    val application = application {}

    /**
     * Suggested configuration for pretty printing.
     */
    fun pretty(jsonBuilder: JsonBuilder.() -> Unit): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        jsonBuilder(this)
    }

    val pretty = pretty {}

}