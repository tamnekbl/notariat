package utils.res

import utils.logging.Log
import java.util.*

object StringsRes {
    private const val BASE_NAME = "strings"
    private var locale: Locale = Locale.getDefault()
    private var bundle: ResourceBundle = ResourceBundle.getBundle(BASE_NAME, locale)

    fun setLocale(newLocale: Locale) {
        locale = newLocale
        bundle = ResourceBundle.getBundle(BASE_NAME, locale)
    }

    fun get(key: String): String = try {
        ResourceBundle.getBundle(BASE_NAME, locale).getString(key)
    } catch (e: Throwable) {
        Log.e(e, "String resource access error. KEY: $key")
        key
    }
}

