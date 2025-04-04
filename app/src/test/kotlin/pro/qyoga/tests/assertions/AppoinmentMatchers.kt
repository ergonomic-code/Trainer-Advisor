package pro.qyoga.tests.assertions

import io.kotest.matchers.Matcher
import io.kotest.matchers.be
import io.kotest.matchers.compose.any
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.qyoga.app.therapist.appointments.core.schedule.AppointmentCard
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.appointments.core.views.LocalizedAppointmentSummary
import pro.qyoga.core.calendar.api.CalendarItem
import pro.qyoga.l10n.russianTimeFormat
import java.time.LocalDateTime
import java.time.ZoneId

infix fun Appointment.shouldMatch(editAppointmentRequest: EditAppointmentRequest) {
    this.clientRef.id shouldBe editAppointmentRequest.client.id
    Matcher.any<Appointment>(
        be(editAppointmentRequest.appointmentType?.id),
        be(editAppointmentRequest.appointmentTypeTitle)
    ).test(this)
    this.therapeuticTaskRef?.id shouldBe editAppointmentRequest.therapeuticTask?.id
    this.dateTime.atZone(this.timeZone).toLocalDateTime() shouldBe editAppointmentRequest.dateTime
    this.timeZone shouldBe editAppointmentRequest.timeZone
    this.duration shouldBe editAppointmentRequest.duration
    this.place shouldBe editAppointmentRequest.place
    this.cost shouldBe editAppointmentRequest.cost
    this.payed shouldBe (editAppointmentRequest.payed ?: false)
    this.status shouldBe editAppointmentRequest.appointmentStatus
    this.comment shouldBe editAppointmentRequest.comment
}

infix fun Appointment.shouldMatch(another: Appointment) {
    this.clientRef.id shouldBe another.clientRef.id
    this.typeRef.id shouldBe another.typeRef.id
    this.therapeuticTaskRef?.id shouldBe another.therapeuticTaskRef?.id
    this.dateTime shouldBe another.dateTime
    this.timeZone shouldBe another.timeZone
    this.duration shouldBe another.duration
    this.place shouldBe another.place
    this.cost shouldBe another.cost
    this.payed shouldBe another.payed
    this.status shouldBe another.status
    this.comment shouldBe another.comment
}

fun CalendarItem<*, LocalDateTime>.shouldMatch(
    editAppointmentRequest: EditAppointmentRequest,
    atTimeZone: ZoneId = editAppointmentRequest.timeZone
) {
    this.shouldBeInstanceOf<LocalizedAppointmentSummary>()
    this.clientName shouldBe editAppointmentRequest.client.resolveOrThrow().fullName()
    Matcher.any<LocalizedAppointmentSummary>(
        be(editAppointmentRequest.appointmentType?.id),
        be(editAppointmentRequest.appointmentTypeTitle)
    ).test(this)
    this.dateTime shouldBe editAppointmentRequest.wallClockDateTimeAt(atTimeZone)
    this.duration shouldBe editAppointmentRequest.duration
    this.status shouldBe editAppointmentRequest.appointmentStatus
}

infix fun CalendarItem<*, LocalDateTime>.shouldMatch(another: Appointment) {
    this.shouldBeInstanceOf<LocalizedAppointmentSummary>()
    this.clientName shouldBe another.clientRef.resolveOrThrow().fullName()
    this.typeName shouldBe another.typeRef.resolveOrThrow().name
    this.dateTime shouldBe another.dateTime
    this.duration shouldBe another.duration
    this.status shouldBe another.status
}

fun EditAppointmentRequest.wallClockDateTimeAt(
    atTimeZone: ZoneId
): LocalDateTime? =
    dateTime.atZone(timeZone).withZoneSameInstant(atTimeZone)
        .toLocalDateTime()

infix fun AppointmentCard.shouldMatch(app: Appointment) {
    this.id shouldBe app.id
    this.title shouldBe app.clientRef.resolveOrThrow().fullName()
    this.description shouldBe app.typeRef.resolveOrThrow().name
    this.period shouldBe app.wallClockDateTime.format(russianTimeFormat) + " - " + app.endWallClockDateTime.format(
        russianTimeFormat
    )
}