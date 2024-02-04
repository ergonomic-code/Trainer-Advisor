package pro.qyoga.app.therapist.appointments.core

import org.springframework.stereotype.Component
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.appointments.types.AppointmentTypesRepo
import pro.qyoga.core.appointments.types.model.AppointmentType
import pro.qyoga.core.users.therapists.TherapistRef


@Component
class CreateAppointment(
    private val appointmentsRepo: AppointmentsRepo,
    private val appointmentTypesRepo: AppointmentTypesRepo
) : (TherapistRef, EditAppointmentRequest) -> Appointment {

    override fun invoke(therapistRef: TherapistRef, editAppointmentRequest: EditAppointmentRequest): Appointment {
        var typeRef = editAppointmentRequest.appointmentType
        if (typeRef == null) {
            typeRef =
                appointmentTypesRepo.save(AppointmentType(therapistRef, editAppointmentRequest.appointmentTypeTitle))
                    .ref()
        }

        return appointmentsRepo.save(Appointment(therapistRef, editAppointmentRequest, typeRef))
    }

}