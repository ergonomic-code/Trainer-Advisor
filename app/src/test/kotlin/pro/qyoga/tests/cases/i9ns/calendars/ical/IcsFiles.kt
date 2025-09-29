package pro.qyoga.tests.cases.i9ns.calendars.ical

import java.time.LocalDate
import java.time.format.DateTimeFormatter


val singleWeeklyEvent = """
    BEGIN:VCALENDAR
    PRODID:-//Google Inc//Google Calendar 70.9054//EN
    VERSION:2.0
    CALSCALE:GREGORIAN
    METHOD:PUBLISH
    X-WR-CALNAME:Test
    X-WR-TIMEZONE:Asia/Novosibirsk
    BEGIN:VTIMEZONE
    TZID:Asia/Novosibirsk
    X-LIC-LOCATION:Asia/Novosibirsk
    BEGIN:STANDARD
    TZOFFSETFROM:+0700
    TZOFFSETTO:+0700
    TZNAME:GMT+7
    DTSTART:19700101T000000
    END:STANDARD
    END:VTIMEZONE
    BEGIN:VEVENT
    DTSTART;TZID=Asia/Novosibirsk:20250325T140000
    DTEND;TZID=Asia/Novosibirsk:20250325T150000
    RRULE:FREQ=WEEKLY;BYDAY=TU
    DTSTAMP:20250327T034627Z
    UID:15or6l44l5h6uqcntqjttjhj1u@google.com
    CREATED:20250327T034620Z
    LAST-MODIFIED:20250327T034620Z
    SEQUENCE:0
    STATUS:CONFIRMED
    SUMMARY:Повторяющееся событие
    TRANSP:OPAQUE
    END:VEVENT
    END:VCALENDAR
""".trimIndent()

object MovedRecurringEvent {

    const val eventUid = "0hunaov13csvogshr1e7l05nqb@google.com"
    private val movedEventDate = LocalDate.of(2025, 3, 19)
    private val movedEventDateIcsStr = movedEventDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
    val icsFile = """
        BEGIN:VCALENDAR
        PRODID:-//Google Inc//Google Calendar 70.9054//EN
        VERSION:2.0
        CALSCALE:GREGORIAN
        METHOD:PUBLISH
        X-WR-CALNAME:Test
        X-WR-TIMEZONE:Asia/Novosibirsk
        BEGIN:VTIMEZONE
        TZID:Asia/Novosibirsk
        X-LIC-LOCATION:Asia/Novosibirsk
        BEGIN:STANDARD
        TZOFFSETFROM:+0700
        TZOFFSETTO:+0700
        TZNAME:GMT+7
        DTSTART:19700101T000000
        END:STANDARD
        END:VTIMEZONE
        BEGIN:VEVENT
        DTSTART;TZID=Asia/Novosibirsk:${movedEventDateIcsStr}T180000
        DTEND;TZID=Asia/Novosibirsk:${movedEventDateIcsStr}T190000
        RRULE:FREQ=WEEKLY;BYDAY=TU
        DTSTAMP:20250305T042121Z
        ORGANIZER;CN=3a97983a1d3645a89003f9f36a629aac3e74056b9b17fe4a4bac0dd5e302c0
         3a@group.calendar.google.com:mailto:3a97983a1d3645a89003f9f36a629aac3e74056
         b9b17fe4a4bac0dd5e302c03a@group.calendar.google.com
        UID:${eventUid}
        ATTENDEE;CUTYPE=INDIVIDUAL;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;CN=ma
         rina.volovaya@gmail.com;X-NUM-GUESTS=0:mailto:marina.volovaya@gmail.com
        ATTENDEE;CUTYPE=INDIVIDUAL;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;CN=bi
         z@azhidkov.pro;X-NUM-GUESTS=0:mailto:biz@azhidkov.pro
        X-GOOGLE-CONFERENCE:https://meet.google.com/yuz-rdwj-muh
        CREATED:${movedEventDateIcsStr}T102327Z
        DESCRIPTION:Description\n\nПрисоединиться через Google Meet: https://meet.g
         oogle.com/yuz-rdwj-muh.\n\nПодробнее о Google Meet по ссылке: https://suppo
         rt.google.com/a/users/answer/9282720
        LAST-MODIFIED:20250305T025330Z
        SEQUENCE:0
        STATUS:CONFIRMED
        SUMMARY:Test2
        TRANSP:OPAQUE
        END:VEVENT
        BEGIN:VEVENT
        DTSTART;TZID=Asia/Novosibirsk:20250319T180000
        DTEND;TZID=Asia/Novosibirsk:20250319T190000
        DTSTAMP:20250305T042121Z
        ORGANIZER;CN=3a97983a1d3645a89003f9f36a629aac3e74056b9b17fe4a4bac0dd5e302c0
         3a@group.calendar.google.com:mailto:3a97983a1d3645a89003f9f36a629aac3e74056
         b9b17fe4a4bac0dd5e302c03a@group.calendar.google.com
        UID:${eventUid}
        ATTENDEE;CUTYPE=INDIVIDUAL;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;CN=ma
         rina.volovaya@gmail.com;X-NUM-GUESTS=0:mailto:marina.volovaya@gmail.com
        ATTENDEE;CUTYPE=INDIVIDUAL;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;CN=bi
         z@azhidkov.pro;X-NUM-GUESTS=0:mailto:biz@azhidkov.pro
        X-GOOGLE-CONFERENCE:https://meet.google.com/yuz-rdwj-muh
        RECURRENCE-ID;TZID=Asia/Novosibirsk:20250311T180000
        CREATED:${movedEventDateIcsStr}T102327Z
        DESCRIPTION:Description\n\nПрисоединиться через Google Meet: https://meet.g
         oogle.com/yuz-rdwj-muh.\n\nПодробнее о Google Meet по ссылке: https://suppo
         rt.google.com/a/users/answer/9282720
        LAST-MODIFIED:20250305T025330Z
        SEQUENCE:1
        STATUS:CONFIRMED
        SUMMARY:Test2
        TRANSP:OPAQUE
        END:VEVENT
        END:VCALENDAR
    """.trimIndent()
        .replace("\n", "\r\n")

}
