package pro.qyoga.tests.fixture.object_mothers.appointments

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.appointments.types.model.AppointmentType
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF


object AppointmentTypesObjectMother {

    fun randomAppointmentType(
        therapistRef: TherapistRef = THE_THERAPIST_REF,
        name: String = randomCyrillicWord()
    ) = AppointmentType(therapistRef, name)

    fun fakeAppointmentType() = AggregateReference.to<AppointmentType, Long>(-1)

}
