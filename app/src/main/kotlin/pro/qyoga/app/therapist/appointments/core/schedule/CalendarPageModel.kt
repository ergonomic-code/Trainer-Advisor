package pro.qyoga.app.therapist.appointments.core.schedule

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.core.appointments.core.AppointmentStatus
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
 * * Контрла быстрого выбора дня
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
    val calendarDays: Collection<CalendarDay>,
    val appointmentId: Long?
) : ModelAndView("therapist/appointments/schedule.html") {

    init {
        addObject("date", date)
        addObject("timeMarks", timeMarks)
        addObject("calendarDays", calendarDays)
        addObject("selectedDayLabel", date.format(russianDayOfMonthLongFormat))
        addObject("focusAppointmentId", appointmentId)
    }

    companion object {

        fun of(date: LocalDate, appointments: Collection<Appointment>, appointment: Long?): CalendarPageModel {
            val timeMarks = generateTimeMarks(appointments, date)
            val weekCalendar = generateDaysAround(date)
            return CalendarPageModel(date, timeMarks, weekCalendar, appointment)
        }

        private fun generateTimeMarks(
            appointments: Collection<Appointment>,
            date: LocalDate
        ): List<TimeMark> {
            val minHour = (appointments.minOfOrNull { it.wallClockDateTime.toLocalTime().hour } ?: DEFAULT_START_HOUR)
                .coerceAtMost(DEFAULT_START_HOUR)
            val maxHour = (appointments.maxOfOrNull { it.endWallClockDateTime.toLocalTime().hour } ?: DEFAULT_END_HOUR)
                .coerceAtLeast(DEFAULT_END_HOUR)

            val timesMarks = generateSequence(LocalTime.of(minHour, 0)) {
                it + TimeMark.length
            }
                .take((maxHour - minHour + 1) * (TimeMark.marksPerHour))

            val days = generateSequence(date.minusDays(1)) {
                it.plusDays(1)
            }
                .take(3)

            val timeMarkRows = timesMarks.map { time ->
                val timeMarkAppointments = days
                    .map { day ->
                        val dateTimeAppointment = appointments.find { it.fallsIn(day.atTime(time), TimeMark.length) }
                        day to dateTimeAppointment?.let { AppointmentCard(it) }
                    }
                    .toList()

                TimeMark(time, timeMarkAppointments)
            }
                .toList()

            return timeMarkRows
        }

        private fun Appointment.fallsIn(wallClockDateTime: LocalDateTime, span: Duration) =
            this.wallClockDateTime >= wallClockDateTime && this.wallClockDateTime < wallClockDateTime + span

        private fun generateDaysAround(date: LocalDate) =

            generateSequence(date.minusDays(3)) {
                it.plusDays(1)
            }
                .map {
                    CalendarDay(
                        it,
                        it.dayOfWeek.getDisplayName(TextStyle.SHORT, systemLocale),
                        it.dayOfMonth,
                        it.dayOfWeek in setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                        it == date
                    )
                }
                .take(DAYS_IN_WEEK)
                .toList()

        const val DAYS_IN_CALENDAR = 3
        const val DAYS_IN_WEEK = 7
        const val DEFAULT_START_HOUR = 7
        const val DEFAULT_END_HOUR = 22

    }

}

/**
 * Строка календаря - время дня (+15 минут) и приёмы, начинающиеся в это время в
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

data class AppointmentCard(
    val id: Long,
    val period: String,
    val client: String,
    val type: String,
    val statusClass: String,
    val timeMarkOffsetPercent: Double,
    val timeMarkLengthPercent: Double,
) {

    constructor(app: Appointment) : this(
        app.id,
        "${app.wallClockDateTime.format(russianTimeFormat)} - ${app.endWallClockDateTime.format(russianTimeFormat)}",
        app.clientRef.resolveOrThrow().fullName(),
        app.typeRef.resolveOrThrow().name,
        appointmentStatusClasses.getValue(app.status),
        (app.wallClockDateTime.minute % 15) / 15.0 * 3,
        (app.duration.toMinutes() / 5.0)
    )

    companion object {
        val appointmentStatusClasses = mapOf(
            AppointmentStatus.PENDING to "pending",
            AppointmentStatus.CLIENT_CAME to "client-came",
            AppointmentStatus.CLIENT_DO_NOT_CAME to "client-do-not-came",
        )
    }

}

data class CalendarDay(
    val date: LocalDate,
    val label: String,
    val day: Int,
    val holiday: Boolean,
    val selected: Boolean
)

