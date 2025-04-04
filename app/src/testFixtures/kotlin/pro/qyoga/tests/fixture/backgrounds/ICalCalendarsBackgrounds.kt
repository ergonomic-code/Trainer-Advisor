package pro.qyoga.tests.fixture.backgrounds

import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.returns
import org.springframework.stereotype.Component
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.calendar.ical.ICalCalendarsRepo
import pro.qyoga.core.calendar.ical.commands.CreateICalRq
import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.tests.infra.wiremock.WireMock
import java.net.URI
import java.net.URL

@Component
class ICalCalendarsBackgrounds(
    private val icalCalendarsRepo: ICalCalendarsRepo
) {

    fun aIcsUrl(): URL =
        URI.create(WireMock.wiremock.baseUrl()).resolve("/ics/${UUIDv7.randomUUID()}.ics").toURL()

    fun createICalCalendar(ical: ICalCalendar): ICalCalendar {
        WireMock.wiremock.get {
            urlEqualTo(ical.icsUrl.toString())
        } returns {
            body = ical.icsFile
        }
        return icalCalendarsRepo.addICal(CreateICalRq(ical.ownerRef, ical.icsUrl, ical.name))
    }

    fun updateICalSource(icsUrl: URL, icsFile: String) {
        WireMock.wiremock.get {
            urlEqualTo(icsUrl.toString())
        } returns {
            body = icsFile
        }
    }

}