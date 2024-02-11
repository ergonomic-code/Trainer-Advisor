package pro.qyoga.tests.clients.pages.therapist.appointments

import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.appointments.core.Appointment
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.infra.html.*

private const val PATH = "/therapist/appointments"

abstract class AppointmentForm(action: FormAction) : QYogaForm("editAppointmentForm", action) {

    val clientInput by component { ComboBox("client", true) }
    val typeInput by component { ComboBox("appointmentType", true) }
    val therapeuticTaskInput by component { ComboBox("therapeuticTask", false) }

    val dateTime by component { Input.dateTimeLocal("dateTime", true) }
    val timeZone by component { ComboBox("timeZone", true) }
    val place by component { Input.text("place", false) }

    val cost by component { Input.number("cost", false) }
    val payed by component { Input.checkbox("payed", false, "true") }

    val comment by component { TextArea("comment", false) }

}

object CreateAppointmentForm : AppointmentForm(FormAction.hxPost("$PATH/new"))

object EditAppointmentForm : AppointmentForm(FormAction.hxPut("$PATH/{appointmentId}")) {

    fun formPrefilledWith(appointment: Appointment): PageMatcher = PageMatcher { element ->

        val clientInputEl = element.select(clientInput.selector()).single()
        clientInput.value(clientInputEl) shouldBe appointment.clientRef.resolveOrThrow().id.toString()

        val typeInputEl = element.select(typeInput.selector()).single()
        typeInput.value(typeInputEl) shouldBe appointment.typeRef.resolveOrThrow().id.toString()

        val therapeuticTaskEl = element.select(therapeuticTaskInput.selector()).single()
        therapeuticTaskInput.value(therapeuticTaskEl) shouldBe (appointment.therapeuticTaskRef?.resolveOrThrow()?.id?.toString()
            ?: "")

        val dateTimeEl = element.select(dateTime.selector()).single()
        dateTime.value(dateTimeEl) shouldBe appointment.wallClockDateTime.toString()

        val timeZoneEl = element.select(timeZone.selector()).single()
        timeZone.value(timeZoneEl) shouldBe appointment.timeZone.id

        val placeEl = element.select(place.selector()).single()
        place.value(placeEl) shouldBe (appointment.place ?: "")

        val costEl = element.select(cost.selector()).single()
        cost.value(costEl) shouldBe (appointment.cost?.toString() ?: "")

        val payedEl = element.select(payed.selector()).single()
        payed.value(payedEl) shouldBe appointment.payed.toString()

        val commentEl = element.select(comment.selector()).single()
        comment.value(commentEl) shouldBe (appointment.comment ?: "")
    }

}
