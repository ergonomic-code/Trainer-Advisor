package pro.qyoga.tests.clients.pages.therapist.appointments

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
    val payed by component { Input.checkbox("payed", false) }

    val comment by component { TextArea("comment", false) }

}

object CreateAppointmentForm : AppointmentForm(FormAction.hxPost("$PATH/new"))

object EditAppointmentForm : AppointmentForm(FormAction.hxPost("$PATH/{appointmentId}"))
