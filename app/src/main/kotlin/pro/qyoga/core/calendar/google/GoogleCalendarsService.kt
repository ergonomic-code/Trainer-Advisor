package pro.qyoga.core.calendar.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.core.calendar.api.CalendarsService
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


class GoogleCalendarsService : CalendarsService {

    override fun findCalendarItemsInInterval(
        therapist: TherapistRef,
        interval: Interval<ZonedDateTime>
    ): Iterable<CalendarItem<*, LocalDateTime>> {
        val accessToken =
            "access-token"
        val APPLICATION_NAME = "Your Application Name"
        val JSON_FACTORY = GsonFactory.getDefaultInstance()
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        val service = Calendar.Builder(httpTransport, JSON_FACTORY, null)
            .setApplicationName(APPLICATION_NAME)
            .build()

        service.CalendarList().list()
            .setOauthToken(accessToken)
            .execute().items.forEach {
                println(it.id)
                println(it.summary)
                println(it)
                println()
            }

        val now = Date()
        val events =
            service.events().list("marina.volovaya@gmail.com") // "primary" refers to the user's primary calendar
                .setTimeMin(com.google.api.client.util.DateTime(now.toInstant().toEpochMilli()))
                .setMaxResults(10) // Example: Get the next 10 events
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .setOauthToken(accessToken) // Set the access token
                .execute()
                .items
                .forEach {
                    println(it.summary + "\n")
                }

        return emptyList()
    }

}

fun main() {
    GoogleCalendarsService().findCalendarItemsInInterval(
        TherapistRef.to(UUIDv7.randomUUID()),
        Interval.of(ZonedDateTime.now(), Duration.ofHours(1))
    )
}