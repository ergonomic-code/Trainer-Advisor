package pro.azhidkov.timezones

import java.time.ZoneId


data class LocalizedTimeZone(
    val zone: ZoneId,
    val displayName: String
)