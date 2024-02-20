package pro.qyoga.core.appointments.core

import java.time.LocalDate
import java.time.ZonedDateTime


data class FutureAppointments(
    val baseDateTime: ZonedDateTime,
    val today: List<Appointment>,
    val nextWeek: Map<LocalDate, List<Appointment>>,
    val later: List<Appointment>
) {

    val size = today.size + nextWeek.values.sumOf { it.size } + later.size

    companion object {

        fun of(baseDateTime: ZonedDateTime, appointments: Collection<Appointment>): FutureAppointments {
            val today = baseDateTime.toLocalDate()

            val todayAppointments = appointments.filter { it.wallClockDateTime.toLocalDate() == today }

            val nextWeekDays = generateSequence(LocalDate.now()) { it.plusDays(1) }.drop(1).take(6)

            val nextWeek =
                appointments.filter { it.wallClockDateTime.toLocalDate() in (today.plusDays(1)..today.plusDays(6)) }
                    .groupBy { it.wallClockDateTime.toLocalDate() }

            val nextWeekSchedule = nextWeekDays.map { it to (nextWeek[it] ?: emptyList()) }.toMap()

            val later = appointments.filter { it.wallClockDateTime.toLocalDate() >= today.plusWeeks(1) }

            val futureAppointments = FutureAppointments(baseDateTime, todayAppointments, nextWeekSchedule, later)

            check(futureAppointments.size == appointments.size) {
                "Sum of appointment groups sizes should be equal to source collection size"
            }

            return futureAppointments
        }

    }

}