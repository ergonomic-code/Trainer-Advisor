package pro.azhidkov.tests.cases.domain.timezones

import io.kotest.matchers.collections.shouldBeUnique
import org.junit.jupiter.api.Test
import pro.azhidkov.timezones.TimeZones
import java.util.*


class TimeZonesTest {

    private val russianTimeZones = TimeZones(Locale.of("ru"))

    @Test
    fun `Search result should not contain duplicates when a time zone matches both id and title`() {
        // Given
        val timeZoneId = "Asia/Novosibirsk"
        val timeZoneTitle = "Нововсибирск"

        // When
        val searchResult = russianTimeZones.search(timeZoneId, timeZoneTitle)

        // Then
        searchResult.shouldBeUnique()
    }

}