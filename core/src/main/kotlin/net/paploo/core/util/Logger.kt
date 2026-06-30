package net.paploo.core.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Extension property to get a default configured logger for the receiver.
 */
val <T : Any> T.logger: Logger get() = LoggerFactory.getLogger(this::class.java)