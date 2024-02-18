package pro.qyoga.tests.cases.app.components

import io.kotest.matchers.Matcher
import io.kotest.matchers.compose.all
import io.kotest.matchers.should
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.app.publc.components.TimeZonesComboBoxController
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.platform.html.ComboBox
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class TimeZonesComboBoxTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `TimeZonesComboBox should return items that has either the same id, contain title or the same offset`() {
        val timeZoneId = "Asia/Novosibirsk"
        val timeZoneTitle = "Новосибирск"

        val document = Given {
            queryParam("timeZoneId", timeZoneId)
            queryParam("timeZoneTitle", timeZoneTitle)
        } When {
            get(TimeZonesComboBoxController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }

        document shouldHaveComponent ComboBox.itemFor(ComboBoxItem(timeZoneId, timeZoneTitle))
    }

    @Test
    fun `TimeZonesComboBox should return first 5 items, when no request parameters provided at all`() {
        val firstItems = getBean<TimeZones>().search(null, null, 5)

        val document = Given {
            this
        } When {
            get(TimeZonesComboBoxController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }

        document should Matcher.all(
            *firstItems.map { ComboBox.itemFor(ComboBoxItem(it.zone, it.displayName)).matcher() }.toTypedArray()
        )
    }

}
