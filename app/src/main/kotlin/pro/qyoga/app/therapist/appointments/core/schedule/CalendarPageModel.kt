package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.web.servlet.ModelAndView
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel.Companion.DAYS_IN_CALENDAR
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel.Companion.DAYS_IN_WEEK
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel.Companion.DEFAULT_END_HOUR
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel.Companion.DEFAULT_START_HOUR
import pro.qyoga.core.appointments.core.AppointmentStatus
import pro.qyoga.core.appointments.core.LocalizedAppointmentSummary
import pro.qyoga.l10n.russianDayOfMonthLongFormat
import pro.qyoga.l10n.russianTimeFormat
import pro.qyoga.l10n.systemLocale
import java.time.*
import java.time.format.TextStyle

/**
 * Модель страницы календаря расписания приёмов.
 *
 * Состоит из:
 * * Контрола выбора дня в нативном календаре
 * * Сетки календаря с приёмами
 * * Контрола быстрого выбора дня
 *
 * Календарь состоит из трёх столбцов - выбранный день +/- 1 день
 * и пачки строк - от мин(7 утра, минимальный час начала приём) до макс(22, максимальный час конца приёма).
 * Поверх сетки отображаются карточки приёмов
 *
 * Контрол быстрого выбора дня состоит из строки с 7 днями - выбранный день +/- 3 дня
 *
 */
data class CalendarPageModel(
    val date: LocalDate,
    val timeMarks: List<TimeMark>,
    val calendarDays: Collection<CalendarDay>
) : ModelAndView("therapist/appointments/schedule.html") {

    init {
        addObject("date", date)
        addObject("timeMarks", timeMarks)
        addObject("calendarDays", calendarDays)
        addObject("selectedDayLabel", date.format(russianDayOfMonthLongFormat))
    }

    companion object {

        fun of(
            date: LocalDate,
            appointments: Iterable<LocalizedAppointmentSummary>
        ): CalendarPageModel {
            val timeMarks = generateTimeMarks(appointments, date)
            val weekCalendar = generateDaysAround(date)
            return CalendarPageModel(date, timeMarks, weekCalendar)
        }

        const val DAYS_IN_CALENDAR = 3
        const val DAYS_IN_WEEK = 7
        const val DEFAULT_START_HOUR = 7
        const val DEFAULT_END_HOUR = 22

    }

}

private fun generateTimeMarks(
    appointments: Iterable<LocalizedAppointmentSummary>,
    date: LocalDate
): List<TimeMark> {
    val days = generateSequence(date.minusDays(DAYS_IN_CALENDAR / 2L)) { it.plusDays(DAYS_IN_CALENDAR / 2L) }
        .take(DAYS_IN_CALENDAR)
        .toList()
    check(days.size == DAYS_IN_CALENDAR)
    val daysRange = days.first()..days.last()

    val (minHour, maxHour) = determineTimeBoundaries(appointments, daysRange)

    val timesMarks = generateSequence(LocalTime.of(minHour, 0)) { it + TimeMark.length }
        .take((maxHour - minHour + 1) * (TimeMark.marksPerHour))
        .toList()

    val timeMarkRows = timesMarks.map { time ->
        val timeMarkAppointments = days
            .map { day ->
                val dateTimeAppointment = appointments
                    .find { it.shouldHasCardInCell(day.atTime(time), TimeMark.length) }
                    ?.let { AppointmentCard(it, day) }
                day to dateTimeAppointment
            }

        TimeMark(time, timeMarkAppointments)
    }

    return timeMarkRows
}

private fun determineTimeBoundaries(
    appointments: Iterable<LocalizedAppointmentSummary>,
    daysRange: ClosedRange<LocalDate>
): Pair<Int, Int> {
    val minHour = (appointments.minOfOrNull {
        if (it.spansMidnight && it.endsWithin(daysRange)) 0
        else it.dateTime.hour
    } ?: DEFAULT_START_HOUR)
        .coerceAtMost(DEFAULT_START_HOUR)

    val maxHour = (appointments.maxOfOrNull {
        if (it.spansMidnight && it.startsWithin(daysRange)) 23
        else it.endDateTime.hour
    } ?: DEFAULT_END_HOUR)
        .coerceAtLeast(DEFAULT_END_HOUR)
    return Pair(minHour, maxHour)
}

private fun LocalizedAppointmentSummary.startsWithin(daysRange: ClosedRange<LocalDate>): Boolean =
    this.dateTime.toLocalDate() in daysRange

private fun LocalizedAppointmentSummary.endsWithin(daysRange: ClosedRange<LocalDate>): Boolean =
    this.endDateTime.toLocalDate() in daysRange

private fun LocalizedAppointmentSummary.shouldHasCardInCell(wallClockDateTime: LocalDateTime, span: Duration) =
    (this.dateTime >= wallClockDateTime && this.dateTime < wallClockDateTime + span) ||
            (wallClockDateTime.toLocalTime() == LocalTime.MIDNIGHT && this.spansMidnight && this.endDateTime.toLocalDate() == wallClockDateTime.toLocalDate())

private val LocalizedAppointmentSummary.spansMidnight
    get() = this.dateTime.toLocalTime().isAfter(this.endDateTime.toLocalTime())

private fun generateDaysAround(date: LocalDate) =
    generateSequence(date.minusDays(3)) { it.plusDays(1) }
        .map { CalendarDay(it, selected = it == date) }
        .take(DAYS_IN_WEEK)
        .toList()

/**
 * Строка календаря - 15-минутный интервал начинающийся в определённое время дня и приёмы, начинающиеся в это время в
 * выбранные дни
 */
data class TimeMark(
    val time: LocalTime,
    val days: List<Pair<LocalDate, AppointmentCard?>>
) {

    companion object {
        val length: Duration = Duration.ofMinutes(15)
        val marksPerHour = Duration.ofHours(1).dividedBy(length).toInt()
    }

}

/**
 * Карточка приёма в календаре.
 * Позиция карточки определяется как оффсет от начала строки календаря, в которую попадает карточка в процентах от
 * высоты строки.
 * Высота карточки определяется как процент от высоты строки календаря.
 */
data class AppointmentCard(
    val id: Long,
    val period: String,
    val client: String,
    val type: String,
    val statusClass: String,
    val timeMarkOffsetPercent: Double,
    val timeMarkLengthPercent: Double,
) {

    constructor(
        app: LocalizedAppointmentSummary,
        day: LocalDate,
    ) : this(
        app.id,
        app.dateTime.format(russianTimeFormat) + " - " + app.endDateTime.format(russianTimeFormat),
        app.clientName,
        app.typeName,
        appointmentStatusClasses.getValue(app.status),
        timeMarkOffsetPercent(app, day),
        timeMarkLengthPercent(app, day)
    )

    companion object {
        val appointmentStatusClasses = mapOf(
            AppointmentStatus.PENDING to "pending",
            AppointmentStatus.CLIENT_CAME to "client-came",
            AppointmentStatus.CLIENT_DO_NOT_CAME to "client-do-not-came",
        )

    }

}

private fun timeMarkOffsetPercent(app: LocalizedAppointmentSummary, day: LocalDate) =
    if (app.dateTime.dayOfMonth == day.dayOfMonth)
        (app.dateTime.minute % 15) / TimeMark.length.toMinutes().toDouble()
    else
        0.0

private fun timeMarkLengthPercent(app: LocalizedAppointmentSummary, day: LocalDate) =
    app.durationAtDay(day).toMinutes() / TimeMark.length.toMinutes().toDouble()

private fun LocalizedAppointmentSummary.durationAtDay(day: LocalDate) =
    when {
        this.dateTime.dayOfMonth == this.endDateTime.dayOfMonth ->
            duration

        this.dateTime.toLocalDate() == day ->
            Duration.between(this.dateTime.toLocalTime(), LocalTime.MAX).plusNanos(1)

        this.endDateTime.toLocalDate() == day ->
            Duration.between(LocalTime.MIDNIGHT, this.endDateTime.toLocalTime())

        else -> error("Appointment spans more than two days")
    }

data class CalendarDay(
    val date: LocalDate,
    val label: String,
    val day: Int,
    val holiday: Boolean,
    val selected: Boolean
) {
    constructor(date: LocalDate, selected: Boolean) : this(
        date,
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, systemLocale),
        date.dayOfMonth,
        date.dayOfWeek in setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
        selected
    )
}

