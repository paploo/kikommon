package net.paploo.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

object JsonLibrary {

    fun standard(jsonBuilder: JsonBuilder.() -> Unit): Json = Json {
        ignoreUnknownKeys = true
        jsonBuilder(this)
    }

    val standard = standard {}

    fun pretty(jsonBuilder: JsonBuilder.() -> Unit): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        jsonBuilder(this)
    }

    val pretty = pretty {}

}