package pro.qyoga.core.appointments.core

import pro.azhidkov.platform.errors.DomainError


class AppointmentsIntersectionException(
    override val message: String,
    val existingAppointment: Appointment
) : DomainError()