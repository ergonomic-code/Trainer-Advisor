package pro.qyoga.tests.fixture.backgrounds

import org.springframework.stereotype.Component
import pro.qyoga.core.appointments.types.AppointmentTypesRepo
import pro.qyoga.core.appointments.types.model.AppointmentType
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentTypesObjectMother

@Component
class AppointmentTypesBackgrounds(
    private val appointmentTypesRepo: AppointmentTypesRepo
) {

    fun createAppointmentTypes(count: Int): Iterable<AppointmentType> {
        return (1..count).map { createAppointmentType() }
    }

    fun createAppointmentType(appointmentType: AppointmentType = AppointmentTypesObjectMother.randomAppointmentType()): AppointmentType {
        return appointmentTypesRepo.save(appointmentType)
    }

}
