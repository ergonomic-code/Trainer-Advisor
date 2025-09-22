package pro.qyoga.tests.fixture.backgrounds

import com.github.tomakehurst.wiremock.client.WireMock.*
import org.springframework.stereotype.Component
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.i9ns.calendars.ical.ICalCalendarsRepo
import pro.qyoga.i9ns.calendars.ical.commands.CreateICalRq
import pro.qyoga.i9ns.calendars.ical.model.ICalCalendar
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
        WireMock.wiremock.stubFor(
            get(urlEqualTo(ical.icsUrl.path))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "text/calendar")
                        .withBody(ical.icsFile)
                )
        )
        return icalCalendarsRepo.addICal(CreateICalRq(ical.ownerRef, ical.icsUrl, ical.name))
    }

    fun updateICalSource(icsUrl: URL, icsFile: String) {
        WireMock.wiremock.stubFor(
            get(urlEqualTo(icsUrl.path.toString()))
                .willReturn(aResponse().withBody(icsFile))
        )
    }

}
