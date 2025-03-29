package pro.qyoga.app.therapist.appointments.core.edit.ops

import org.springframework.stereotype.Component
import pro.qyoga.app.therapist.appointments.core.edit.errors.AppointmentsIntersectionException
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.appointments.core.findIntersectingAppointment
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.appointments.core.toTimeSpanString
import pro.qyoga.core.appointments.types.AppointmentTypesRepo
import pro.qyoga.core.users.therapists.TherapistRef

@Component
class CreateAppointmentOp(
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