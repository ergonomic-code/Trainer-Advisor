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