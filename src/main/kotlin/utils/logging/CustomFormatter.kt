package utils.logging

import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.Formatter
import java.util.logging.Level
import java.util.logging.LogRecord

class CustomFormatter : Formatter() {
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS")

    override fun format(record: LogRecord): String {
        val timestamp = dateFormat.format(Date(record.millis))

        val (color, levelLabel) = when (record.level) {
            Level.SEVERE -> "\u001B[0m" to "E"
            Level.WARNING -> "\u001B[33m" to "W"
            Level.INFO -> "\u001B[32m" to "I"
            Level.FINE -> "\u001B[36m" to "D"
            else -> "\u001B[0m" to record.level.name.take(1)
        }

        val reset = "\u001B[0m"

        val callerInfo = getRealCaller()

        val message = formatMessage(record)

        return "\u001B[97m$timestamp $color[$levelLabel] ($callerInfo): $message$reset\n"
    }

    private fun getRealCaller(): String {
        val trace = Throwable().stackTrace
        return trace
            .firstOrNull {
                it.className != Log::class.java.name &&
                        !it.className.startsWith("java.util.logging") &&
                        !it.className.contains(this::class.java.name)
            }?.let {
                val className = it.className.substringAfterLast('.')
                val methodName = it.methodName.substringBefore("$") // ← удаляем всё после $
                "$className:$methodName"
            } ?: "Unknown"
    }
}