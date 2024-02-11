package pro.qyoga.tests.assertions

import io.kotest.matchers.Matcher
import io.kotest.matchers.be
import io.kotest.matchers.compose.any
import io.kotest.matchers.shouldBe
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.core.appointments.core.EditAppointmentRequest


infix fun Appointment.shouldMatch(editAppointmentRequest: EditAppointmentRequest) {
    this.clientRef.id shouldBe editAppointmentRequest.client.id
    Matcher.any<Appointment>(
        be(editAppointmentRequest.appointmentType?.id),
        be(editAppointmentRequest.appointmentTypeTitle)
    ).test(this)
    this.therapeuticTaskRef?.id shouldBe editAppointmentRequest.therapeuticTask?.id
    this.dateTime.atZone(this.timeZone).toLocalDateTime() shouldBe editAppointmentRequest.dateTime
    this.timeZone shouldBe editAppointmentRequest.timeZone
    this.place shouldBe editAppointmentRequest.place
    this.cost shouldBe editAppointmentRequest.cost
    this.payed shouldBe (editAppointmentRequest.payed ?: false)
    this.comment shouldBe editAppointmentRequest.comment
}
