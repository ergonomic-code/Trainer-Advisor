package pro.qyoga.app.therapist.appointments.core.edit.errors

import pro.azhidkov.platform.errors.DomainError
import pro.qyoga.core.appointments.core.model.Appointment

class AppointmentsIntersectionException(
    override val message: String,
    val existingAppointment: Appointment
) : DomainError(errorCode = "appointments.intersection")