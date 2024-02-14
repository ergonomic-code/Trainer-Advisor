package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.appointments.core.*
import pro.qyoga.core.appointments.types.AppointmentTypesRepo
import pro.qyoga.core.appointments.types.model.AppointmentType
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.users.therapists.TherapistRef


@Component
class CreateAppointmentWorkflow(
    private val appointmentsRepo: AppointmentsRepo,
    private val appointmentTypesRepo: AppointmentTypesRepo
) : (TherapistRef, EditAppointmentRequest) -> Appointment {

    override fun invoke(therapistRef: TherapistRef, editAppointmentRequest: EditAppointmentRequest): Appointment {
        val intersectingAppointment = appointmentsRepo.findIntersectingAppointment(
            therapistRef,
            editAppointmentRequest.instant,
            editAppointmentRequest.duration
        )

        if (intersectingAppointment != null) {
            throw AppointmentsIntersectionException(
                "There are exist appointment with intersecting time span: ${intersectingAppointment.toTimeSpanString()}",
                intersectingAppointment
            )
        }

        val typeRef = appointmentTypesRepo.createTypeIfNew(therapistRef, editAppointmentRequest)
        return appointmentsRepo.save(Appointment(therapistRef, editAppointmentRequest, typeRef))
    }

}

@Component
class UpdateAppointmentWorkflow(
    private val appointmentsRepo: AppointmentsRepo,
    private val appointmentTypesRepo: AppointmentTypesRepo
) : (TherapistRef, AppointmentRef, EditAppointmentRequest) -> Appointment? {

    override fun invoke(
        therapistRef: TherapistRef,
        appointmentRef: AppointmentRef,
        editAppointmentRequest: EditAppointmentRequest
    ): Appointment? {
        val intersectingAppointment = appointmentsRepo.findIntersectingAppointment(
            therapistRef,
            editAppointmentRequest.instant,
            editAppointmentRequest.duration
        )

        if (intersectingAppointment != null && intersectingAppointment.id != appointmentRef.id) {
            throw AppointmentsIntersectionException(
                "There are exist appointment with intersecting time span: ${intersectingAppointment.toTimeSpanString()}",
                intersectingAppointment
            )
        }

        val typeRef = appointmentTypesRepo.createTypeIfNew(therapistRef, editAppointmentRequest)

        return appointmentsRepo.update(appointmentRef.id!!) { appointment ->
            appointment.patchBy(editAppointmentRequest, typeRef)
        }
    }

}


private fun AppointmentTypesRepo.createTypeIfNew(
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
