package pro.qyoga.core.appointments.core

import pro.azhidkov.platform.spring.sdj.erpo.hydration.resolveOrNull
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import java.time.ZoneId

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
        editAppointmentRequest.duration,
        editAppointmentRequest.place,
        editAppointmentRequest.cost,
        editAppointmentRequest.payed ?: false,
        editAppointmentRequest.appointmentStatus,
        editAppointmentRequest.comment,
        id,
        createdAt,
        modifiedAt,
        version
    )

fun Appointment.toTimeSpanString() = "$wallClockDateTime - ${wallClockDateTime.plus(duration)} ($timeZone)"

fun Appointment.toEditRequest(resolveTimeZone: (ZoneId) -> LocalizedTimeZone?) = EditAppointmentRequest(
    clientRef,
    clientRef.resolveOrNull()?.fullName() ?: clientRef.id.toString(),
    typeRef,
    typeRef.resolveOrNull()?.name ?: typeRef.id?.toString() ?: "",
    therapeuticTaskRef,
    therapeuticTaskRef?.resolveOrNull()?.name ?: therapeuticTaskRef?.id?.toString() ?: "",
    wallClockDateTime,
    timeZone,
    resolveTimeZone(timeZone)?.displayName ?: timeZone.id,
    duration,
    place,
    cost,
    payed,
    status,
    comment
)