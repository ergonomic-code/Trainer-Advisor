package pro.qyoga.app.therapist.appointments.core.edit.ops

import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.appointments.types.AppointmentTypesRepo
import pro.qyoga.core.appointments.types.model.AppointmentType
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.users.therapists.TherapistRef

fun AppointmentTypesRepo.createTypeIfNew(
    therapistRef: TherapistRef,
    editAppointmentRequest: EditAppointmentRequest
): AppointmentTypeRef {
    var typeRef = editAppointmentRequest.appointmentType
    if (typeRef == null) {
        val appointmentType = AppointmentType(therapistRef, editAppointmentRequest.appointmentTypeTitle)
        typeRef = this.save(appointmentType).ref()
    }

    return typeRef
}