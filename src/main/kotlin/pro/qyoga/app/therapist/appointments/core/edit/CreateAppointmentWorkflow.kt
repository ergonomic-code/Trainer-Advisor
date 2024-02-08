package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.appointments.types.AppointmentTypesRepo
import pro.qyoga.core.users.therapists.TherapistRef


@Component
class CreateAppointmentWorkflow(
    private val appointmentsRepo: AppointmentsRepo,
    private val appointmentTypesRepo: AppointmentTypesRepo
) : (TherapistRef, EditAppointmentRequest) -> Appointment {

    override fun invoke(therapistRef: TherapistRef, editAppointmentRequest: EditAppointmentRequest): Appointment {
        val typeRef = appointmentTypesRepo.createTypeIfNew(therapistRef, editAppointmentRequest)
        return appointmentsRepo.save(Appointment(therapistRef, editAppointmentRequest, typeRef))
    }

}