package utils.logging

import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger

object Log {
    private val logger = Logger.getLogger("AppLogger").apply {
        level = Level.ALL
        useParentHandlers = false

        val handler = ConsoleHandler().apply {
            level = Level.ALL
            formatter = CustomFormatter()
        }

        addHandler(handler)
    }

    fun i(msg: String) = logger.info(msg)
    fun w(msg: String) = logger.warning(msg)
    fun e(ex: Throwable?, msg: String) {
        logger.severe(msg)
        ex?.printStackTrace()
    }
    fun e(msg: String) = logger.severe(msg)

    fun e(ex: Throwable?) = ex?.printStackTrace()

    fun d(msg: String) = logger.fine(msg)
}