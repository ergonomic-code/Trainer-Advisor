package pro.qyoga.tests.pages.therapist.appointments

import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.java.time.toLocalTimeString
import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.core.appointments.core.AppointmentStatus
import pro.qyoga.core.appointments.core.EditAppointmentRequest
import pro.qyoga.tests.assertions.PageMatcher
import pro.qyoga.tests.platform.html.*

private const val PATH = "/therapist/appointments"

abstract class AppointmentForm(action: FormAction) : QYogaForm("editAppointmentForm", action) {

    val clientInput by component { ComboBox("client", true) }
    val typeInput by component { ComboBox("appointmentType", true) }
    val therapeuticTaskInput by component { ComboBox("therapeuticTask", false) }

    val dateTime by component { Input.dateTimeLocal("dateTime", true) }
    val timeZone by component { ComboBox("timeZone", true) }
    val duration by component { Input.time("duration", true) }
    val place by component { Input.text("place", false) }

    val cost by component { Input.number("cost", false) }
    val payed by component { Input.checkbox("payed", false, "true") }

    val statusPending by component {
        Input.radio("appointmentStatus", true, AppointmentStatus.PENDING.name)
    }
    val statusClientCame by component {
        Input.radio("appointmentStatus", true, AppointmentStatus.CLIENT_CAME.name)
    }
    val statusClientNotCame by component {
        Input.radio("appointmentStatus", true, AppointmentStatus.CLIENT_DO_NOT_CAME.name)
    }

    val comment by component { TextArea("comment", false) }

    val submit by component { Button("save", "") }

    val appointmentsIntersectionErrorMessage = "#appointmentsIntersectionErrorMessage"

}

object CreateAppointmentForm : AppointmentForm(FormAction.hxPost("$PATH/new"))

object EditAppointmentForm : AppointmentForm(FormAction.hxPut("$PATH/{appointmentId}")) {

    fun formPrefilledWith(editAppointmentRequest: EditAppointmentRequest): PageMatcher = PageMatcher { element ->

        val clientInputEl = element.select(clientInput.selector()).single()
        clientInput.value(clientInputEl) shouldBe editAppointmentRequest.client.resolveOrThrow().id.toString()

        val typeInputEl = element.select(typeInput.selector()).single()
        typeInput.value(typeInputEl) shouldBe (editAppointmentRequest.appointmentType?.resolveOrThrow()?.id?.toString()
            ?: "")

        val therapeuticTaskEl = element.select(therapeuticTaskInput.selector()).single()
        therapeuticTaskInput.value(therapeuticTaskEl) shouldBe (editAppointmentRequest.therapeuticTask?.resolveOrThrow()?.id?.toString()
            ?: "")

        val dateTimeEl = element.select(dateTime.selector()).single()
        dateTime.value(dateTimeEl) shouldBe editAppointmentRequest.dateTime.toString()

        val timeZoneEl = element.select(timeZone.selector()).single()
        timeZone.value(timeZoneEl) shouldBe editAppointmentRequest.timeZone.id

        val durationEl = element.select(duration.selector()).single()
        duration.value(durationEl) shouldBe editAppointmentRequest.duration.toLocalTimeString()

        val placeEl = element.select(place.selector()).single()
        place.value(placeEl) shouldBe (editAppointmentRequest.place ?: "")

        val costEl = element.select(cost.selector()).single()
        cost.value(costEl) shouldBe (editAppointmentRequest.cost?.toString() ?: "")

        val payedEl = element.select(payed.selector()).single()
        payed.value(payedEl) shouldBe (editAppointmentRequest.payed == true).toString()

        val commentEl = element.select(comment.selector()).single()
        comment.value(commentEl) shouldBe (editAppointmentRequest.comment ?: "")
    }

}
