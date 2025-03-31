package pro.azhidkov.timezones

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*


class TimeZones(
    private val locale: Locale
) {

    val allTimeZones = ZoneId.getAvailableZoneIds()
        .filter { !it.contains("ical", true) } // ical зачем-то подсовывает сюда пачку еле рабочих зон
        .map { ZoneId.of(it) }
        .map { LocalizedTimeZone(it, it.getDisplayName(TextStyle.FULL, locale)) }
        .sortedBy { it.displayName }

    fun search(zoneId: String?, timeZoneName: String?, limit: Int = Int.MAX_VALUE): Slice<LocalizedTimeZone> {
        val timezoneById = allTimeZones.find { it.zone.id == zoneId }
        val searchResultByName = allTimeZones.filter {
            it != timezoneById && it.displayName.contains(timeZoneName ?: "", ignoreCase = true)
        }

        val now = Instant.now()
        val searchResultByOffset =
            if (timezoneById != null) {
                allTimeZones.filter {
                    it != timezoneById &&
                            it.zone.rules.getOffset(now) == timezoneById.zone.rules.getOffset(now)
                }
            } else {
                emptyList()
            }

        val baseTimeZone: List<LocalizedTimeZone> = listOfNotNull(timezoneById)

        val searchResult = listOf(*(baseTimeZone + searchResultByName + searchResultByOffset).toTypedArray())
            .take(limit)

        return SliceImpl(searchResult, Pageable.ofSize(limit), false)
    }

    fun findById(zoneId: ZoneId): LocalizedTimeZone? =
        allTimeZones.find { it.zone == zoneId }

}