package net.paploo.core.extensions

/**
 * Converts a string containing a valid identifier to snake_case.
 */
fun String.snakeCase(): String =
    replace("(([a-z])([A-Z]))|(([A-Z])([A-Z][a-z]))".toRegex(), "$2$5_$3$6").replace('-', '_').lowercase()

/**
 * Converts a string containing a valid identifier to kebab-case.
 */
fun String.kebabCase(): String =
    snakeCase().replace('_', '-')

/**
 * Converts a string containing a valid identifier to CONST_CASE.
 */
fun String.constCase(): String =
    snakeCase().uppercase()

/**
 * Converts a string containing a valid identifier to camelCase.
 */
fun String.camelCase(): String =
    snakeCase().replace("(\\w)(_)(\\w)".toRegex()) { it.groupValues[1].lowercase() + it.groupValues[3].uppercase() }

/**
 * Converts a string containing a valid identifier to PascalCase.
 */
fun String.pascalCase(): String =
    camelCase().replaceFirstChar { it.uppercase() }