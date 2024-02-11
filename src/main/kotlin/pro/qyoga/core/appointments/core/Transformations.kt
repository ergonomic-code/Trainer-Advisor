package pro.qyoga.core.appointments.core

import pro.qyoga.core.appointments.types.model.AppointmentTypeRef

fun Appointment.patchBy(
    editAppointmentRequest: EditAppointmentRequest,
    appointmentType: AppointmentTypeRef
): Appointment =
    Appointment(
        therapistRef,
        editAppointmentRequest.client,
        appointmentType,
        editAppointmentRequest.therapeuticTask,
        editAppointmentRequest.instant,
        editAppointmentRequest.timeZone,
        editAppointmentRequest.place,
        editAppointmentRequest.cost,
        editAppointmentRequest.payed ?: false,
        editAppointmentRequest.comment,
        id,
        createdAt,
        modifiedAt,
        version
    )