package pro.qyoga.tests.cases.app.components

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import io.kotest.matchers.should
import org.junit.jupiter.api.Test
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.clients.PublicClient
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.platform.html.ComboBox


class TimeZonesComboBoxTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `TimeZonesComboBox should return items that has either the same id, contain title or the same offset`() {
        // Given
        val timeZoneId = "Asia/Novosibirsk"
        val timeZoneTitle = "Новосибирск"

        // When
        val document = PublicClient.getTimeZones(timeZoneId, timeZoneTitle)

        // Then
        document shouldHaveComponent ComboBox.itemFor(ComboBoxItem(timeZoneId, timeZoneTitle))
    }

    @Test
    fun `TimeZonesComboBox should return first 5 items, when no request parameters provided at all`() {
        // Given
        val firstItems = getBean<TimeZones>().search(null, null, 5)

        // When
        val document = PublicClient.getTimeZones()

        // Then
        document should Matcher.all(
            *firstItems.map { ComboBox.itemFor(ComboBoxItem(it.zone, it.displayName)).matcher() }.toList()
                .toTypedArray()
        )
    }

}
